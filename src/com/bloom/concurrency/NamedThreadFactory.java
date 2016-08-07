package com.bloom.concurrency;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory
  implements ThreadFactory
{
  private final AtomicInteger id = new AtomicInteger();
  private final String prefix;
  
  public NamedThreadFactory(String prefix)
  {
    this.prefix = prefix;
  }
  
  public Thread newThread(Runnable r)
  {
    int newThreadId = this.id.getAndIncrement();
    Thread t = new Thread(r);
    t.setName(this.prefix + "-" + newThreadId);
    return t;
  }
}
