package com.bloom.recovery;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class AvroCheckpointDetail
  extends CheckpointDetail
{
  private static final long serialVersionUID = 3544878361207130863L;
  private Long blockCount = Long.valueOf(0L);
  private Long recordCount = Long.valueOf(0L);
  private String previousFileName = null;
  
  public AvroCheckpointDetail(Long blkCount, Long recCount, String fileName, String previousFileName)
  {
    this.blockCount = blkCount;
    this.recordCount = recCount;
    super.setSourceName(fileName);
    this.previousFileName = previousFileName;
  }
  
  public AvroCheckpointDetail(CheckpointDetail obj)
  {
    super(obj);
  }
  
  public void readObject(ObjectInputStream in)
    throws IOException, ClassNotFoundException
  {
    super.readObject(in);
    this.blockCount = Long.valueOf(in.readLong());
    this.recordCount = Long.valueOf(in.readLong());
    this.previousFileName = in.readUTF();
  }
  
  public void writeObject(ObjectOutputStream out)
    throws IOException
  {
    super.writeObject(out);
    out.writeLong(this.blockCount.longValue());
    out.writeLong(this.recordCount.longValue());
    out.writeUTF(this.previousFileName);
  }
  
  public int compareTo(CheckpointDetail arg0)
  {
    AvroCheckpointDetail that = (AvroCheckpointDetail)arg0;
    if (getSourceName().equals(that.getSourceName()))
    {
      int compare = this.blockCount.compareTo(that.blockCount);
      if (compare == 0) {
        return this.recordCount.compareTo(that.recordCount);
      }
      return compare;
    }
    if (this.previousFileName.equals(that.getSourceName())) {
      return 1;
    }
    return 0;
  }
  
  public Long getBlockCount()
  {
    return this.blockCount;
  }
  
  public Long getRecordCount()
  {
    return this.recordCount;
  }
  
  public String toString()
  {
    return "FileName : " + getSourceName() + "\nBlock Count : " + this.blockCount + "\nRecord Count : " + this.recordCount;
  }
}
