package com.bloom.recovery;

public class JSONSourcePosition
  extends SourcePosition
{
  private static final long serialVersionUID = 8373700063769046220L;
  public final CheckpointDetail checkpoint;
  
  public JSONSourcePosition(CheckpointDetail checkpoint)
  {
    this.checkpoint = checkpoint;
  }
  
  public int compareTo(SourcePosition arg0)
  {
    JSONSourcePosition that = (JSONSourcePosition)arg0;
    return this.checkpoint.getSourceCreationTime().compareTo(that.checkpoint.getSourceCreationTime());
  }
  
  public String toString()
  {
    return this.checkpoint.getStringVal();
  }
}
