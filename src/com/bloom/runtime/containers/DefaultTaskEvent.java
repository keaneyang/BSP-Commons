package com.bloom.runtime.containers;

import com.bloom.uuid.UUID;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.bloom.runtime.RecordKey;

public class DefaultTaskEvent
  implements ITaskEvent
{
  private WAEvent containedEvent;
  private UUID queryId = null;
  
  public DefaultTaskEvent(WAEvent xevent)
  {
    this.containedEvent = xevent;
  }
  
  public void write(Kryo kryo, Output output)
  {
    kryo.writeClassAndObject(output, this.containedEvent);
  }
  
  public void read(Kryo kryo, Input input)
  {
    this.containedEvent = ((WAEvent)kryo.readClassAndObject(input));
  }
  
  public boolean snapshotUpdate()
  {
    return (batch().isEmpty()) && (removedBatch().isEmpty());
  }
  
  public IBatch batch()
  {
    return new DefaultBatch(this.containedEvent);
  }
  
  public IBatch removedBatch()
  {
    return DefaultBatch.EMPTY_BATCH;
  }
  
  public IRange snapshot()
  {
    return DefaultRange.EMPTY_RANGE;
  }
  
  public int getFlags()
  {
    return 1;
  }
  
  public UUID getQueryID()
  {
    return this.queryId;
  }
  
  public void setQueryID(UUID queryID)
  {
    this.queryId = queryID;
  }
  
  public IBatch filterBatch(int indexID, RecordKey key)
  {
    return DefaultBatch.EMPTY_BATCH;
  }
}
