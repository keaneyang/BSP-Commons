package com.bloom.recovery;

import org.apache.log4j.Logger;

public class MySQLSourcePosition
  extends SourcePosition
{
  private static final long serialVersionUID = 291836330502171234L;
  private Long binLogPosition = Long.valueOf(0L);
  private String binLogName = null;
  private String previousBinlogName = null;
  private Integer rowSequenceCount = Integer.valueOf(0);
  private Long beginRecordPosition = Long.valueOf(0L);
  private static final Logger logger = Logger.getLogger(MySQLSourcePosition.class);
  
  public MySQLSourcePosition() {}
  
  public MySQLSourcePosition(String name, long position, int count, String previousBinlogName, Long txnBeginPos)
  {
    this.binLogName = name;
    this.binLogPosition = Long.valueOf(position);
    this.rowSequenceCount = Integer.valueOf(count);
    this.previousBinlogName = previousBinlogName;
    this.beginRecordPosition = txnBeginPos;
  }
  
  public int compareTo(SourcePosition arg0)
  {
    MySQLSourcePosition that = (MySQLSourcePosition)arg0;
    if (this.binLogName.equals(that.binLogName))
    {
      int compare = this.binLogPosition.compareTo(that.binLogPosition);
      if (compare == 0) {
        return this.rowSequenceCount.compareTo(that.rowSequenceCount);
      }
      return compare;
    }
    String thisSeqStr = this.binLogName.substring(this.binLogName.lastIndexOf(".") + 1);
    String thatSeqStr = that.binLogName.substring(that.binLogName.lastIndexOf(".") + 1);
    
    Long thisLogIndex = Long.valueOf(thisSeqStr);
    Long thatLogIndex = Long.valueOf(thatSeqStr);
    int compare = thisLogIndex.compareTo(thatLogIndex);
    if (compare < 0)
    {
      String thisPreviousSeq = this.previousBinlogName.substring(this.previousBinlogName.lastIndexOf(".") + 1);
      Long thisPreviousSeqIndex = Long.valueOf(thisPreviousSeq);
      
      int sequenceCompare = thisPreviousSeqIndex.compareTo(thatLogIndex);
      if (sequenceCompare == 0) {
        return 1;
      }
    }
    return compare;
  }
  
  public String toString()
  {
    String position = "BinlogName : " + this.binLogName + "\nBinLogPosition : " + this.binLogPosition + "\nPreviousBinlogName : " + this.previousBinlogName + "\nRestartPosition : " + this.beginRecordPosition;
    return position;
  }
  
  public String getBinLogName()
  {
    return this.binLogName;
  }
  
  public String getPreviousBinLogName()
  {
    return this.previousBinlogName;
  }
  
  public long getRestartPosition()
  {
    return this.beginRecordPosition.longValue();
  }
}
