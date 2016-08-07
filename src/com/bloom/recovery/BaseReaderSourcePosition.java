package com.bloom.recovery;

public class BaseReaderSourcePosition
  extends SourcePosition
{
  private static final long serialVersionUID = 5501761434305045684L;
  public final CheckpointDetail recordCheckpoint;
  
  public BaseReaderSourcePosition()
  {
    this.recordCheckpoint = null;
  }
  
  public BaseReaderSourcePosition(CheckpointDetail checkpoint)
  {
    this.recordCheckpoint = checkpoint;
  }
  
  public int compareTo(SourcePosition arg0)
  {
    BaseReaderSourcePosition that = (BaseReaderSourcePosition)arg0;
    return this.recordCheckpoint.compareTo(that.recordCheckpoint);
  }
  
  public String toString()
  {
    return this.recordCheckpoint.toString();
  }
}
