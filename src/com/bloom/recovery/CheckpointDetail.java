package com.bloom.recovery;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.apache.log4j.Logger;

public class CheckpointDetail
  implements Serializable, Comparable<CheckpointDetail>
{
  private static final long serialVersionUID = 5177066802079053229L;
  private Long seekPosition = Long.valueOf(0L);
  private Long creationTime = Long.valueOf(0L);
  private Long recordBeginOffset = Long.valueOf(0L);
  private Long recordEndOffset = Long.valueOf(0L);
  private Long recordLength = Long.valueOf(0L);
  private String sourceName = "";
  private Boolean recovery = Boolean.valueOf(false);
  private long bytesRead = 0L;
  private String actualName = "";
  
  public void writeObject(ObjectOutputStream out)
    throws IOException
  {
    out.writeLong(this.seekPosition.longValue());
    out.writeLong(this.creationTime.longValue());
    out.writeLong(this.recordBeginOffset.longValue());
    out.writeLong(this.recordEndOffset.longValue());
    out.writeLong(this.recordLength.longValue());
    out.writeLong(this.bytesRead);
    out.writeBoolean(this.recovery.booleanValue());
    out.writeUTF(this.sourceName);
    out.writeUTF(this.actualName);
  }
  
  public String getStringVal()
  {
    StringBuilder result = new StringBuilder();
    
    result.append(this.seekPosition);
    result.append(",");
    result.append(this.creationTime);
    result.append(",");
    result.append(this.recordBeginOffset);
    result.append(",");
    result.append(this.recordEndOffset);
    result.append(",");
    result.append(this.recordLength);
    result.append(",");
    result.append(this.bytesRead);
    result.append(",");
    result.append(this.recovery);
    result.append(",");
    result.append(this.sourceName);
    result.append(",");
    result.append(this.actualName);
    
    return result.toString();
  }
  
  public void setStringVal(String instr)
  {
    String[] parts = instr.split(",");
    this.seekPosition = Long.valueOf(Long.parseLong(parts[0]));
    this.creationTime = Long.valueOf(Long.parseLong(parts[1]));
    this.recordBeginOffset = Long.valueOf(Long.parseLong(parts[2]));
    this.recordEndOffset = Long.valueOf(Long.parseLong(parts[3]));
    this.recordLength = Long.valueOf(Long.parseLong(parts[4]));
    this.bytesRead = Long.parseLong(parts[5]);
    this.recovery = Boolean.valueOf(Boolean.parseBoolean(parts[6]));
    this.sourceName = parts[7];
    if (parts.length == 9) {
      this.actualName = parts[8];
    }
  }
  
  public void readObject(ObjectInputStream in)
    throws IOException, ClassNotFoundException
  {
    this.seekPosition = Long.valueOf(in.readLong());
    this.creationTime = Long.valueOf(in.readLong());
    this.recordBeginOffset = Long.valueOf(in.readLong());
    this.recordEndOffset = Long.valueOf(in.readLong());
    this.recordLength = Long.valueOf(in.readLong());
    this.bytesRead = in.readLong();
    this.recovery = Boolean.valueOf(in.readBoolean());
    this.sourceName = in.readUTF();
    this.actualName = in.readUTF();
  }
  
  public CheckpointDetail() {}
  
  public CheckpointDetail(CheckpointDetail obj)
  {
    if (obj == null) {
      return;
    }
    this.seekPosition = obj.seekPosition;
    this.creationTime = obj.creationTime;
    this.recordBeginOffset = obj.recordBeginOffset;
    this.recordEndOffset = obj.recordEndOffset;
    this.recordLength = obj.recordLength;
    this.sourceName = obj.sourceName;
    this.recovery = obj.recovery;
    this.bytesRead = obj.bytesRead;
    this.actualName = obj.actualName;
  }
  
  public void seekPosition(long seekPosition)
  {
    this.seekPosition = Long.valueOf(seekPosition);
  }
  
  public Long seekPosition()
  {
    return this.seekPosition;
  }
  
  public void setSourceCreationTime(long creationTime)
  {
    this.creationTime = Long.valueOf(creationTime);
  }
  
  public Long getSourceCreationTime()
  {
    return this.creationTime;
  }
  
  public void setRecordBeginOffset(long recordBeginOffset)
  {
    this.recordBeginOffset = Long.valueOf(recordBeginOffset);
  }
  
  public Long getRecordBeginOffset()
  {
    return this.recordBeginOffset;
  }
  
  public void setRecordEndOffset(long recordEndOffset)
  {
    this.recordEndOffset = Long.valueOf(recordEndOffset);
  }
  
  public Long getRecordEndOffset()
  {
    return this.recordEndOffset;
  }
  
  public void setRecordLength(long recordLength)
  {
    this.recordLength = Long.valueOf(recordLength);
  }
  
  public Long getRecordLength()
  {
    return this.recordLength;
  }
  
  public void setSourceName(String sourceName)
  {
    this.sourceName = sourceName;
  }
  
  public String getSourceName()
  {
    return this.sourceName;
  }
  
  public void setActualName(String actualName)
  {
    this.actualName = actualName;
  }
  
  public String getActualName()
  {
    return this.actualName;
  }
  
  public void setRecovery(boolean recovery)
  {
    this.recovery = Boolean.valueOf(recovery);
  }
  
  public void setBytesRead(long bytesRead)
  {
    this.bytesRead = bytesRead;
  }
  
  public long getBytesRead()
  {
    return this.bytesRead;
  }
  
  public boolean isRecovery()
  {
    return this.recovery.booleanValue();
  }
  
  public void dump()
  {
    Logger logger = Logger.getLogger(CheckpointDetail.class);
    if (logger.isTraceEnabled())
    {
      logger.trace("********************* Checkpoint Details **************************");
      logger.trace("seekPosition : [" + this.seekPosition + "]");
      logger.trace("creationTime : [" + this.creationTime + "]");
      logger.trace("recordBeginOffset : [" + this.recordBeginOffset + "]");
      logger.trace("recordEndOffset : [" + this.recordEndOffset + "]");
      logger.trace("recordLength : [" + this.recordLength + "]");
      logger.trace("sourceName : [" + this.sourceName + "]");
      logger.trace("actualName : [" + this.actualName + "]");
      logger.trace("********************************************************************");
    }
  }
  
  public String toString()
  {
    return getStringVal();
  }
  
  public int compareTo(CheckpointDetail arg0)
  {
    int retValue = 0;
    if ((retValue = this.sourceName.compareTo(arg0.getSourceName())) == 0) {
      retValue = (int)(getRecordEndOffset().longValue() - arg0.getRecordEndOffset().longValue());
    }
    return retValue;
  }
}
