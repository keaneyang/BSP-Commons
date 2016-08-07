package com.bloom.runtime.containers;

import com.bloom.runtime.RecordKey;

public abstract interface IRange
{
  public abstract IBatch<WAEvent> all();
  
  public abstract IBatch<WAEvent> lookup(int paramInt, RecordKey paramRecordKey);
  
  public abstract IRange update(IRange paramIRange);
  
  public abstract void beginTransaction();
  
  public abstract void endTransaction();
}

