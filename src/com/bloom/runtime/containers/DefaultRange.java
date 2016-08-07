package com.bloom.runtime.containers;

import com.bloom.runtime.RecordKey;

public class DefaultRange
  implements IRange
{
  public static final IRange EMPTY_RANGE = new DefaultRange();
  
  public IBatch all()
  {
    return DefaultBatch.EMPTY_BATCH;
  }
  
  public IBatch lookup(int indexID, RecordKey key)
  {
    return DefaultBatch.EMPTY_BATCH;
  }
  
  public IRange update(IRange r)
  {
    return r;
  }
  
  public void beginTransaction() {}
  
  public void endTransaction() {}
}
