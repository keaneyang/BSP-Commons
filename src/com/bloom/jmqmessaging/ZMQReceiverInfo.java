package com.bloom.jmqmessaging;

import com.bloom.messaging.ReceiverInfo;
import com.esotericsoftware.kryo.KryoSerializable;
import java.io.Serializable;

public class ZMQReceiverInfo
  extends ReceiverInfo
  implements Serializable, KryoSerializable
{
  private static final long serialVersionUID = 88218429128226711L;
  
  public ZMQReceiverInfo() {}
  
  public ZMQReceiverInfo(String name, String host)
  {
    super(name, host);
  }
  
  public String toString()
  {
    return super.getName() + "'s " + "inprocURI : " + super.getInprocURI() + "\n" + "ipcURI : " + super.getIpcURI() + "\n" + "tcpURI : " + super.getTcpURI() + "\n" + "--------------------------------------------\n";
  }
  
  public boolean equals(ZMQReceiverInfo that)
  {
    boolean hasEqual = hashCode() == that.hashCode();
    boolean nameEqual = (getName().equals(that.getName())) && (getHost().equals(that.getHost()));
    return (hasEqual) && (nameEqual);
  }
}
