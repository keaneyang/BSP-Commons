package com.bloom.proc.events;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.bloom.runtime.StreamEvent;
import java.io.Serializable;

public class PublishableEvent
  implements KryoSerializable, Serializable
{
  private static final long serialVersionUID = -5718860713267311590L;
  private short versionId;
  private short partitionId;
  private long authToken;
  private StreamEvent streamEvent;
  
  public PublishableEvent()
  {
    this(null);
  }
  
  public PublishableEvent(StreamEvent event)
  {
    this(event, 0L, (short)0, (short)0);
  }
  
  public PublishableEvent(StreamEvent event, long authToken, short partId, short verId)
  {
    this.streamEvent = event;
    this.authToken = authToken;
    this.partitionId = partId;
    this.versionId = verId;
  }
  
  public short getVersionId()
  {
    return this.versionId;
  }
  
  public short getPartitionId()
  {
    return this.partitionId;
  }
  
  public long getAuthToken()
  {
    return this.authToken;
  }
  
  public StreamEvent getStreamEvent()
  {
    return this.streamEvent;
  }
  
  public void write(Kryo kryo, Output output)
  {
    if (this.streamEvent != null)
    {
      output.writeBoolean(true);
      kryo.writeClassAndObject(output, this.streamEvent);
    }
    else
    {
      output.writeBoolean(false);
    }
    output.writeLong(this.authToken);
    output.writeShort(this.partitionId);
    output.writeShort(this.versionId);
  }
  
  public void read(Kryo kryo, Input input)
  {
    boolean hasEvent = input.readBoolean();
    if (hasEvent) {
      this.streamEvent = ((StreamEvent)kryo.readClassAndObject(input));
    } else {
      this.streamEvent = null;
    }
    this.authToken = input.readLong();
    this.partitionId = input.readShort();
    this.versionId = input.readShort();
  }
  
  public String toString()
  {
    StringBuffer buffer = new StringBuffer("PublishableEvent(");
    buffer.append((this.streamEvent == null ? "NullEvent" : this.streamEvent.toString()) + " , ");
    buffer.append(this.partitionId + " , ");
    buffer.append(this.versionId + " , ");
    buffer.append(this.authToken + ")");
    
    return buffer.toString();
  }
  
  public boolean equals(Object o)
  {
    if (!(o instanceof PublishableEvent)) {
      return false;
    }
    PublishableEvent evt = (PublishableEvent)o;
    boolean ret = false;
    if (this.streamEvent == null) {
      ret = evt.getStreamEvent() == null;
    } else {
      this.streamEvent.equals(evt.getStreamEvent());
    }
    ret &= this.partitionId == evt.getPartitionId();
    ret &= this.versionId == evt.getVersionId();
    ret &= this.authToken == evt.getAuthToken();
    
    return ret;
  }
}
