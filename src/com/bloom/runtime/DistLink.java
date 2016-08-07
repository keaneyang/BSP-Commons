package com.bloom.runtime;

import com.bloom.uuid.UUID;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.io.Serializable;

public class DistLink
  implements Serializable, KryoSerializable
{
  private static final long serialVersionUID = 4032047921091787606L;
  private UUID subID;
  private int linkID;
  public String name;
  private int hash;
  
  public DistLink()
  {
    this.subID = null;
    this.linkID = -1;
    this.name = null;
    this.hash = 0;
  }
  
  public DistLink(UUID subID, int linkID, String name)
  {
    this.subID = subID;
    this.linkID = linkID;
    this.name = name;
    this.hash = (subID.hashCode() ^ linkID);
  }
  
  public final boolean equals(Object o)
  {
    if (!(o instanceof DistLink)) {
      return false;
    }
    DistLink other = (DistLink)o;
    return (getSubID().equals(other.getSubID())) && (getLinkID() == other.getLinkID());
  }
  
  public final int hashCode()
  {
    return this.hash;
  }
  
  public final String toString()
  {
    return "(" + this.name + ":" + this.subID + "-" + this.linkID + ":" + this.hash + ")";
  }
  
  public void write(Kryo kryo, Output output)
  {
    if (this.subID != null)
    {
      output.writeByte(0);
      this.subID.write(kryo, output);
    }
    else
    {
      output.writeByte(1);
    }
    output.writeInt(this.linkID);
    output.writeString(this.name);
    output.writeInt(this.hash);
  }
  
  public void read(Kryo kryo, Input input)
  {
    byte hasSubID = input.readByte();
    if (hasSubID == 0)
    {
      this.subID = new UUID();
      this.subID.read(kryo, input);
    }
    this.linkID = input.readInt();
    this.name = input.readString();
    this.hash = input.readInt();
  }
  
  public UUID getSubID()
  {
    return this.subID;
  }
  
  public void setSubID(UUID subID)
  {
    this.subID = subID;
  }
  
  public int getLinkID()
  {
    return this.linkID;
  }
  
  public void setLinkID(int linkID)
  {
    this.linkID = linkID;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public void setName(String name)
  {
    this.name = name;
  }
  
  public int getHash()
  {
    return this.hash;
  }
  
  public void setHash(int hash)
  {
    this.hash = hash;
  }
}
