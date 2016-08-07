package com.bloom.runtime.containers;

import java.io.Serializable;
import java.util.Iterator;

public abstract interface IBatch<T>
  extends Iterable<T>, Serializable
{
  public abstract Iterator<T> iterator();
  
  public abstract int size();
  
  public abstract boolean isEmpty();
  
  public abstract T first();
  
  public abstract T last();
}

