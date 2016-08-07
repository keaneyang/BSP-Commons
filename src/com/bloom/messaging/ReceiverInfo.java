package com.bloom.messaging;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.io.Serializable;

public class ReceiverInfo
  implements Serializable, KryoSerializable
{
  private String name;
  private String host;
  private Address address;
  
  public ReceiverInfo() {}
  
  public ReceiverInfo(String name, String host)
  {
    this.name = name;
    this.host = host;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public String getHost()
  {
    return this.host;
  }
  
  public Address getAddress()
  {
    return this.address;
  }
  
  public void setName(String name)
  {
    this.name = name;
  }
  
  public void setHost(String host)
  {
    this.host = host;
  }
  
  public void setAddress(Address address)
  {
    this.address = address;
  }
  
  public String getTcpURI()
  {
    return this.address.getTcp();
  }
  
  public String getIpcURI()
  {
    return this.address.getIpc();
  }
  
  public String getInprocURI()
  {
    return this.address.getInproc();
  }
  
  public void write(Kryo kryo, Output output)
  {
    output.writeString(this.name);
    output.writeString(this.host);
    kryo.writeClassAndObject(output, this.address);
  }
  
  public void read(Kryo kryo, Input input)
  {
    this.name = input.readString();
    this.host = input.readString();
    this.address = ((Address)kryo.readClassAndObject(input));
  }
}
