package com.bloom.jmqmessaging;

import com.bloom.messaging.ReceiverInfo;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.io.Serializable;
import org.apache.log4j.Logger;

public class KafkaReceiverInfo
  extends ReceiverInfo
  implements Serializable, KryoSerializable
{
  private static final Logger logger = Logger.getLogger(KafkaReceiverInfo.class);
  private static final long serialVersionUID = 88218429128226711L;
  private String rcvr_name;
  private String topic_name;
  
  public KafkaReceiverInfo() {}
  
  public KafkaReceiverInfo(String name, String s)
  {
    super(name, null);
    this.rcvr_name = name;
    this.topic_name = s;
  }
  
  public String toString()
  {
    return this.rcvr_name + " on topic " + this.topic_name;
  }
  
  public boolean equals(KafkaReceiverInfo that)
  {
    return (this.rcvr_name.equals(that.rcvr_name)) && (this.topic_name.equals(that.topic_name));
  }
  
  public String getRcvr_name()
  {
    return this.rcvr_name;
  }
  
  public String getTopic_name()
  {
    return this.topic_name;
  }
  
  public void write(Kryo kryo, Output output)
  {
    super.write(kryo, output);
    output.writeString(this.topic_name);
    output.writeString(this.rcvr_name);
  }
  
  public void read(Kryo kryo, Input input)
  {
    super.read(kryo, input);
    this.topic_name = input.readString();
    this.rcvr_name = input.readString();
  }
}
