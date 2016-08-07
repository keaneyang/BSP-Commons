package com.bloom.concurrency;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PartitionedBucketExecutor
  extends AbstractExecutorService
{
  private final ExecutorService delegatedExecutor;
  private final ReentrantLock lock = new ReentrantLock();
  private final Condition terminatingCondition = this.lock.newCondition();
  private final Map<Object, InorderExecutor> executorMap = new IdentityHashMap();
  private static final ThreadLocal<Object> partitions = new ThreadLocal();
  private AtomicBoolean isTerminated = new AtomicBoolean(Boolean.FALSE.booleanValue());
  
  private PartitionedBucketExecutor(ExecutorService service)
  {
    this.delegatedExecutor = service;
  }
  
  public PartitionedBucketExecutor(String name)
  {
    this(Executors.newCachedThreadPool(new NamedThreadFactory(name)));
  }
  
  public PartitionedBucketExecutor(String name, int numThreads)
  {
    this(Executors.newFixedThreadPool(numThreads, new NamedThreadFactory(name)));
  }
  
  public PartitionedBucketExecutor(String name, int numThreads, ThreadExceptionObserver observer)
  {
    this(new ExceptionNotifyingThreadPool(numThreads, numThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(), new NamedThreadFactory(name), observer));
  }
  
  protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value)
  {
    savePartition(runnable);
    return super.newTaskFor(runnable, value);
  }
  
  protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable)
  {
    savePartition(callable);
    return super.newTaskFor(callable);
  }
  
  private void savePartition(Object task)
  {
    if ((task instanceof Partitioner)) {
      partitions.set(((Partitioner)task).getBucket());
    }
  }
  
  public void execute(Runnable command)
  {
    this.lock.lock();
    try
    {
      if (this.isTerminated.get()) {
        throw new RejectedExecutionException("Executor not running");
      }
      Object partitionForCommand = getPartition(command);
      if (partitionForCommand != null)
      {
        InorderExecutor partitionedExecutor = (InorderExecutor)this.executorMap.get(partitionForCommand);
        if (partitionedExecutor == null)
        {
          partitionedExecutor = new InorderExecutor(partitionForCommand);
          this.executorMap.put(partitionForCommand, partitionedExecutor);
        }
        partitionedExecutor.execute(command);
      }
      else
      {
        this.delegatedExecutor.execute(command);
      }
    }
    finally
    {
      this.lock.unlock();
    }
  }
  
  private Object getPartition(Runnable command)
  {
    Object ret;
    if ((command instanceof Partitioner)) {
      ret = ((Partitioner)command).getBucket();
    } else {
      ret = partitions.get();
    }
    partitions.remove();
    return ret;
  }
  
  public void shutdown()
  {
    this.lock.lock();
    try
    {
      this.isTerminated.set(true);
      if (this.executorMap.isEmpty()) {
        this.delegatedExecutor.shutdown();
      }
    }
    finally
    {
      this.lock.unlock();
    }
  }
  
  public List<Runnable> shutdownNow()
  {
    this.lock.lock();
    try
    {
      shutdown();
      List<Runnable> result = new ArrayList();
      for (InorderExecutor ex : this.executorMap.values()) {
        ex.tasks.drainTo(result);
      }
      result.addAll(this.delegatedExecutor.shutdownNow());
      return result;
    }
    finally
    {
      this.lock.unlock();
    }
  }
  
  public boolean isShutdown()
  {
    this.lock.lock();
    try
    {
      return this.isTerminated.get();
    }
    finally
    {
      this.lock.unlock();
    }
  }
  
  public boolean isTerminated()
  {
    this.lock.lock();
    try
    {
      if (!this.isTerminated.get()) {
        return false;
      }
      for (InorderExecutor ex : this.executorMap.values()) {
        if (!ex.isAvailable()) {
          return false;
        }
      }
      return this.delegatedExecutor.isTerminated();
    }
    finally
    {
      this.lock.unlock();
    }
  }
  
  public boolean awaitTermination(long timeout, TimeUnit unit)
    throws InterruptedException
  {
    this.lock.lock();
    try
    {
      long waitTime = System.nanoTime() + unit.toNanos(timeout);
      long remainingWaitTime;
      while (((remainingWaitTime = waitTime - System.nanoTime()) > 0L) && (!this.executorMap.isEmpty())) {
        this.terminatingCondition.awaitNanos(remainingWaitTime);
      }
      boolean bool;
      if (remainingWaitTime <= 0L) {
        return false;
      }
      if (this.executorMap.isEmpty()) {
        return this.delegatedExecutor.awaitTermination(remainingWaitTime, TimeUnit.NANOSECONDS);
      }
      return false;
    }
    finally
    {
      this.lock.unlock();
    }
  }
  
  public Future<?> submit(Runnable task)
  {
    return submit(task, null);
  }
  
  public <T> Future<T> submit(Callable<T> task)
  {
    this.lock.lock();
    try
    {
      if (this.isTerminated.get()) {
        throw new RejectedExecutionException("Executor not running");
      }
      Future localFuture;
      if ((task instanceof Partitioner)) {
        return super.submit(task);
      }
      return this.delegatedExecutor.submit(task);
    }
    finally
    {
      this.lock.unlock();
    }
  }
  
  public <T> Future<T> submit(Runnable task, T result)
  {
    this.lock.lock();
    try
    {
      if (this.isTerminated.get()) {
        throw new RejectedExecutionException("Executor not running");
      }
      Future localFuture;
      if ((task instanceof Partitioner)) {
        return super.submit(task, result);
      }
      return this.delegatedExecutor.submit(task, result);
    }
    finally
    {
      this.lock.unlock();
    }
  }
  
  private class InorderExecutor
    implements Executor
  {
    private final BlockingQueue<Runnable> tasks = new LinkedBlockingQueue();
    private Runnable currentActiveTask;
    private final Object partition;
    
    private InorderExecutor(Object partition)
    {
      this.partition = partition;
    }
    
    public void execute(final Runnable r)
    {
      PartitionedBucketExecutor.this.lock.lock();
      try
      {
        if ((r instanceof FutureTask))
        {
          final FutureTask ft = (FutureTask)r;
          Callable c = new Callable()
          {
            public Object call()
              throws Exception
            {
              ft.run();
              if (!ft.isCancelled()) {
                return ft.get();
              }
              return null;
            }
          };
          this.tasks.add(new FutureTask(c)
          {
            public void run()
            {
              try
              {
                super.run();
              }
              catch (Throwable t)
              {
                setException(t);
              }
              finally
              {
                PartitionedBucketExecutor.InorderExecutor.this.executeNext();
              }
            }
          });
        }
        else
        {
          this.tasks.add(new Runnable()
          {
            public void run()
            {
              try
              {
                r.run();
              }
              finally
              {
                PartitionedBucketExecutor.InorderExecutor.this.executeNext();
              }
            }
          });
        }
        if (this.currentActiveTask == null) {
          executeNext();
        }
      }
      finally
      {
        PartitionedBucketExecutor.this.lock.unlock();
      }
    }
    
    private void executeNext()
    {
      PartitionedBucketExecutor.this.lock.lock();
      try
      {
        if ((this.currentActiveTask = (Runnable)this.tasks.poll()) != null)
        {
          PartitionedBucketExecutor.this.delegatedExecutor.execute(this.currentActiveTask);
          PartitionedBucketExecutor.this.terminatingCondition.signalAll();
        }
      }
      finally
      {
        PartitionedBucketExecutor.this.lock.unlock();
      }
    }
    
    public boolean isAvailable()
    {
      PartitionedBucketExecutor.this.lock.lock();
      try
      {
        return (this.currentActiveTask == null) && (this.tasks.isEmpty());
      }
      finally
      {
        PartitionedBucketExecutor.this.lock.unlock();
      }
    }
  }
}
