package com.bloom.concurrency;

public abstract interface ThreadExceptionObserver
{
  public abstract void throwThreadExeption(Throwable paramThrowable);
}

