package com.bloom.recovery;

import org.apache.log4j.Logger;

public class KafkaSourcePosition
  extends SourcePosition
{
  private static final long serialVersionUID = -6042045728463138984L;
  private Long kafkaPosition = Long.valueOf(0L);
  private Long waEventOffset = Long.valueOf(0L);
  private String currentPartitionID;
  private int partitionID;
  private String topic;
  private static final Logger logger = Logger.getLogger(KafkaSourcePosition.class);
  
  public KafkaSourcePosition()
  {
    this.currentPartitionID = null;
  }
  
  public KafkaSourcePosition(KafkaSourcePosition sp)
  {
    this.kafkaPosition = sp.kafkaPosition;
    this.waEventOffset = sp.waEventOffset;
    this.currentPartitionID = sp.currentPartitionID;
  }
  
  public KafkaSourcePosition(String topic, int partitionID, long waEventOffset, long kafkaReadOffset)
  {
    this.topic = topic;
    this.partitionID = partitionID;
    this.currentPartitionID = (topic + ":" + partitionID);
    this.waEventOffset = Long.valueOf(waEventOffset);
    this.kafkaPosition = Long.valueOf(kafkaReadOffset);
  }
  
  @Deprecated
  public synchronized void updatePartionOffset(String topic, int partitionID, long waEventOffset, long kafkaReadOffset)
  {
    this.currentPartitionID = (topic + ":" + partitionID);
    if (this.kafkaPosition.longValue() != -1L)
    {
      int compareKafkaReadOffset = Long.valueOf(kafkaReadOffset).compareTo(this.kafkaPosition);
      if (compareKafkaReadOffset == 0)
      {
        long compareWAEventEndOffset = Long.valueOf(waEventOffset).compareTo(Long.valueOf(waEventOffset));
        if (compareWAEventEndOffset > 0L) {
          this.waEventOffset = Long.valueOf(waEventOffset);
        }
      }
      else
      {
        if (compareKafkaReadOffset < 0)
        {
          if (logger.isInfoEnabled()) {
            logger.info("Ignoring the position update for the event with kafka position " + kafkaReadOffset + " and WA record end offset " + waEventOffset);
          }
          return;
        }
        if (compareKafkaReadOffset > 0)
        {
          this.kafkaPosition = Long.valueOf(kafkaReadOffset);
          this.waEventOffset = Long.valueOf(waEventOffset);
        }
      }
    }
    else
    {
      this.kafkaPosition = Long.valueOf(kafkaReadOffset);
      this.waEventOffset = Long.valueOf(waEventOffset);
    }
  }
  
  public int compareTo(SourcePosition arg0)
  {
    KafkaSourcePosition that = (KafkaSourcePosition)arg0;
    
    int compareKafkaReadOffset = this.kafkaPosition.compareTo(that.kafkaPosition);
    if (compareKafkaReadOffset == 0)
    {
      int compare = this.waEventOffset.compareTo(that.waEventOffset);
      
      return compare;
    }
    return compareKafkaReadOffset;
  }
  
  public String toString()
  {
    String result = this.currentPartitionID + " Offset=" + this.kafkaPosition + "/" + this.waEventOffset;
    return result;
  }
  
  public String getCurrentPartitionID()
  {
    return this.currentPartitionID;
  }
  
  public long getRecordEndOffset()
  {
    return this.waEventOffset.longValue();
  }
  
  public long getKafkaReadOffset()
  {
    return this.kafkaPosition.longValue();
  }
  
  public int getPartitionID()
  {
    return this.partitionID;
  }
  
  public String getTopic()
  {
    return this.topic;
  }
}
