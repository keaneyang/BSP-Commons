package com.bloom.concurrency;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExceptionNotifyingThreadPool
  extends ThreadPoolExecutor
{
  private final ThreadExceptionObserver observer;
  
  public ExceptionNotifyingThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, ThreadExceptionObserver observer)
  {
    super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    
    this.observer = observer;
  }
  
  protected void afterExecute(Runnable r, Throwable t)
  {
    super.afterExecute(r, t);
    if ((t == null) && ((r instanceof Future))) {
      try
      {
        Future<?> future = (Future)r;
        if (future.isDone()) {
          future.get();
        }
      }
      catch (CancellationException ce)
      {
        t = ce;
      }
      catch (ExecutionException ee)
      {
        t = ee.getCause();
      }
      catch (InterruptedException ie)
      {
        Thread.currentThread().interrupt();
      }
    }
    if (t != null) {
      this.observer.throwThreadExeption(t);
    }
  }
}
