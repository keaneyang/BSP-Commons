package com.bloom.jmqmessaging;

import com.bloom.messaging.ReceiverInfo;
import com.bloom.uuid.UUID;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.bloom.runtime.DistLink;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class StreamInfoResponse
  implements KryoSerializable, Serializable
{
  private static final long serialVersionUID = -4603793648273123481L;
  private UUID peerID;
  private ReceiverInfo streamReceiverInfo;
  private List<DistLink> subscriberList;
  private boolean encrypted = false;
  
  public StreamInfoResponse() {}
  
  public StreamInfoResponse(UUID peer, ReceiverInfo stream, List<DistLink> subList, boolean encrypted)
  {
    this.peerID = peer;
    this.streamReceiverInfo = stream;
    this.subscriberList = subList;
    this.encrypted = encrypted;
  }
  
  public UUID getPeerID()
  {
    return this.peerID;
  }
  
  public ReceiverInfo getStreamReceiverInfo()
  {
    return this.streamReceiverInfo;
  }
  
  public List<DistLink> getSubscriberList()
  {
    return this.subscriberList;
  }
  
  public boolean isEncrypted()
  {
    return this.encrypted;
  }
  
  public void write(Kryo kryo, Output output)
  {
    if ((this.subscriberList != null) && (this.subscriberList.size() > 0))
    {
      if (this.subscriberList.size() == 1)
      {
        output.writeByte(1);
        kryo.writeObject(output, this.subscriberList.get(0));
      }
      else
      {
        output.writeByte(2);
        kryo.writeClassAndObject(output, this.subscriberList);
      }
    }
    else {
      output.writeByte(0);
    }
    if (this.streamReceiverInfo != null)
    {
      output.writeByte(0);
      kryo.writeClassAndObject(output, this.streamReceiverInfo);
    }
    else
    {
      output.writeByte(1);
    }
    if (this.peerID != null)
    {
      output.writeByte(0);
      kryo.writeClassAndObject(output, this.peerID);
    }
    else
    {
      output.writeByte(1);
    }
    output.writeBoolean(this.encrypted);
  }
  
  public void read(Kryo kryo, Input input)
  {
    byte hasSubList = input.readByte();
    if (hasSubList == 0)
    {
      this.subscriberList = Collections.emptyList();
    }
    else if (hasSubList == 1)
    {
      DistLink dl = (DistLink)kryo.readObject(input, DistLink.class);
      this.subscriberList = Collections.singletonList(dl);
    }
    else
    {
      Object o = kryo.readClassAndObject(input);
      if ((o instanceof List)) {
        this.subscriberList = ((List)o);
      }
    }
    byte hasStreamInfo = input.readByte();
    if (hasStreamInfo == 0) {
      this.streamReceiverInfo = ((ZMQReceiverInfo)kryo.readClassAndObject(input));
    } else {
      this.streamReceiverInfo = null;
    }
    byte hasStreamUUID = input.readByte();
    if (hasStreamUUID == 0) {
      this.peerID = ((UUID)kryo.readClassAndObject(input));
    } else {
      this.peerID = null;
    }
    this.encrypted = input.readBoolean();
  }
  
  public String toString()
  {
    return "StreamInfoResponse (" + this.peerID + ", \n " + this.streamReceiverInfo.toString() + ", \n " + this.subscriberList + " )";
  }
}
