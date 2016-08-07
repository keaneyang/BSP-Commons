package com.bloom.messaging;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.io.Serializable;

public class Address
  implements Serializable, KryoSerializable
{
  private String name = null;
  private String tcp = null;
  private String ipc = null;
  private String inproc = null;
  
  public Address() {}
  
  public Address(String name, String tcp, String ipc, String inproc)
  {
    this.name = name;
    this.tcp = tcp;
    this.ipc = ipc;
    this.inproc = inproc;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public String getInproc()
  {
    return this.inproc;
  }
  
  public String getIpc()
  {
    return this.ipc;
  }
  
  public String getTcp()
  {
    return this.tcp;
  }
  
  public void setName(String name)
  {
    this.name = name;
  }
  
  public void setTcp(String tcp)
  {
    this.tcp = tcp;
  }
  
  public void setIpc(String ipc)
  {
    this.ipc = ipc;
  }
  
  public void setInproc(String inproc)
  {
    this.inproc = inproc;
  }
  
  public String toString()
  {
    return "TCP: " + this.tcp + "\n" + "IPC: " + this.ipc + "\n" + "INPROC: " + this.inproc;
  }
  
  public void write(Kryo kryo, Output output)
  {
    output.writeString(this.name);
    output.writeString(this.inproc);
    output.writeString(this.ipc);
    output.writeString(this.tcp);
  }
  
  public void read(Kryo kryo, Input input)
  {
    this.name = input.readString();
    this.inproc = input.readString();
    this.ipc = input.readString();
    this.tcp = input.readString();
  }
}
