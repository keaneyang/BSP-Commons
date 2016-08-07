package com.bloom.runtime;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.bloom.runtime.containers.ITaskEvent;
import java.io.Serializable;

public class StreamEvent
  implements Serializable, KryoSerializable
{
  private static final long serialVersionUID = -8855223767719326349L;
  private ITaskEvent taskEvent;
  private DistLink link;
  
  public StreamEvent() {}
  
  public StreamEvent(ITaskEvent te, DistLink link)
  {
    this.taskEvent = te;
    this.link = link;
  }
  
  public ITaskEvent getTaskEvent()
  {
    return this.taskEvent;
  }
  
  public void setTaskEvent(ITaskEvent taskEvent)
  {
    this.taskEvent = taskEvent;
  }
  
  public DistLink getLink()
  {
    return this.link;
  }
  
  public void setLink(DistLink link)
  {
    this.link = link;
  }
  
  public void read(Kryo kr, Input input)
  {
    byte hasTE = input.readByte();
    if (hasTE == 0) {
      this.taskEvent = ((ITaskEvent)kr.readClassAndObject(input));
    }
    byte hasLink = input.readByte();
    if (hasLink == 0)
    {
      this.link = new DistLink();
      this.link.read(kr, input);
    }
  }
  
  public void write(Kryo kr, Output output)
  {
    if (this.taskEvent != null)
    {
      output.writeByte(0);
      kr.writeClassAndObject(output, this.taskEvent);
    }
    else
    {
      output.writeByte(1);
    }
    if (this.link != null)
    {
      output.writeByte(0);
      this.link.write(kr, output);
    }
    else
    {
      output.writeByte(1);
    }
  }
  
  public String toString()
  {
    return this.taskEvent.toString() + ":" + this.link.toString();
  }
}
