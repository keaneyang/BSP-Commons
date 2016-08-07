package com.bloom.runtime.containers;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class DefaultBatch
  implements IBatch
{
  private final WAEvent containedEvent;
  
  public DefaultBatch(WAEvent xevent)
  {
    this.containedEvent = xevent;
  }
  
  public Iterator<WAEvent> iterator()
  {
   return new Iterator()
    {
      private boolean hasNext = true;
      
      public boolean hasNext()
      {
        return this.hasNext;
      }
      
      public WAEvent next()
      {
        if (this.hasNext)
        {
          this.hasNext = false;
          return DefaultBatch.this.containedEvent;
        }
        throw new NoSuchElementException();
      }
      
      public void remove()
      {
        throw new UnsupportedOperationException();
      }
    };
  }
  
  public int size()
  {
    return 1;
  }
  
  public boolean isEmpty()
  {
    return false;
  }
  
  public WAEvent first()
  {
    return this.containedEvent;
  }
  
  public WAEvent last()
  {
    return this.containedEvent;
  }
  
  public static final IBatch EMPTY_BATCH = new IBatch()
  {
    public int size()
    {
      return 0;
    }
    
    public WAEvent last()
    {
      return null;
    }
    
    public Iterator<WAEvent> iterator()
    {
      return Collections.emptyIterator();
    }
    
    public boolean isEmpty()
    {
      return true;
    }
    
    public WAEvent first()
    {
      return null;
    }
  };
}
