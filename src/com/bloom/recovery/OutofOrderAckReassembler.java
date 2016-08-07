package com.bloom.recovery;


import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;

public class OutofOrderAckReassembler
{
  private static class Packet
  {
    private final int sequenceNo;
    private final Object object;
    private boolean isAcknowledged;
    
    public Packet(int seq, Object obj)
    {
      this.sequenceNo = seq;
      this.object = obj;
      this.isAcknowledged = false;
    }
    
    public synchronized void setAcknowledged()
    {
      this.isAcknowledged = true;
    }
    
    public synchronized boolean isAcknowledged()
    {
      return this.isAcknowledged;
    }
    
    public boolean equals(Object obj)
    {
      if (obj == this) {
        return true;
      }
      if ((obj == null) || (obj.getClass() != getClass())) {
        return false;
      }
      Packet passedPacket = (Packet)obj;
      return (this.sequenceNo == passedPacket.sequenceNo) && ((this.object == passedPacket.object) || ((this.object != null) && (this.object.equals(passedPacket.object))));
    }
    
    public int hashCode()
    {
      int prime = 31;
      int returnValue = 1;
      returnValue = 31 * returnValue + this.sequenceNo;
      returnValue = 31 * returnValue + (this.object == null ? 0 : this.object.hashCode());
      return returnValue;
    }
    
    public String toString()
    {
      return "Packet: {" + this.sequenceNo + " , " + this.object.toString() + "" + this.isAcknowledged + "}";
    }
  }
  
  private Deque<Packet> packetDeque = new ConcurrentLinkedDeque();
  
  public void appendWrittenObject(int seqNo, Object object)
  {
    this.packetDeque.addLast(new Packet(seqNo, object));
  }
  
  public Object acknowledgeReceive(int sequenceNo)
  {
    Iterator<Packet> iterator = this.packetDeque.iterator();
    while (iterator.hasNext())
    {
      Packet packet = (Packet)iterator.next();
      if (packet.sequenceNo == sequenceNo) {
        packet.setAcknowledged();
      }
    }
    Object returnValue = null;
    iterator = this.packetDeque.iterator();
    while (iterator.hasNext())
    {
      Packet packet = (Packet)iterator.next();
      if (!packet.isAcknowledged()) {
        break;
      }
      returnValue = packet.object;
      this.packetDeque.removeFirstOccurrence(packet);
    }
    return returnValue;
  }
  
  public int size()
  {
    return this.packetDeque.size();
  }
}