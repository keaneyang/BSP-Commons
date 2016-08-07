package com.bloom.recovery;

public class CSVReaderSourcePosition
  extends SourcePosition
{
  private static final long serialVersionUID = -3461856176551140717L;
  public final CheckpointDetail recordCheckpoint;
  
  public CSVReaderSourcePosition(CheckpointDetail checkpoint)
  {
    this.recordCheckpoint = new CheckpointDetail(checkpoint);
  }
  
  public int compareTo(SourcePosition arg0)
  {
    CSVReaderSourcePosition that = (CSVReaderSourcePosition)arg0;
    return this.recordCheckpoint.getRecordEndOffset().compareTo(that.recordCheckpoint.getRecordEndOffset());
  }
  
  public String toString()
  {
    return "CSVReader_1_0.Position:" + this.recordCheckpoint.toString();
  }
}
