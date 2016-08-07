package com.bloom.runtime.containers;

import com.bloom.uuid.UUID;
import com.esotericsoftware.kryo.KryoSerializable;
import com.bloom.runtime.RecordKey;
import java.io.Serializable;

public abstract interface ITaskEvent
  extends Serializable, KryoSerializable
{
  public abstract boolean snapshotUpdate();
  
  public abstract IBatch<WAEvent> batch();
  
  public abstract IBatch<WAEvent> filterBatch(int paramInt, RecordKey paramRecordKey);
  
  public abstract IBatch<WAEvent> removedBatch();
  
  public abstract IRange snapshot();
  
  public abstract int getFlags();
  
  public abstract UUID getQueryID();
  
  public abstract void setQueryID(UUID paramUUID);
}

