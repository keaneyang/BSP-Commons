package com.bloom.recovery;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import java.util.Map;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME)
public class PartitionedSourcePosition
  extends SourcePosition
{
  private static final long serialVersionUID = 5411945471054888685L;
  public static final String NULL_PARTITION_KEY = "*";
  private final Map<String, SourcePosition> sourcePositions;
  
  public PartitionedSourcePosition(Map<String, SourcePosition> sourcePositions)
  {
    this.sourcePositions = sourcePositions;
  }
  
  public SourcePosition get(String partID)
  {
    return (SourcePosition)this.sourcePositions.get(partID);
  }
  
  public int compareTo(SourcePosition arg0)
  {
    throw new RuntimeException("PartitionedSourcePosition objects cannot be compared. Instead use compareTo(SourcePosition, String)");
  }
  
  public int compareTo(SourcePosition arg0, String partID)
    throws IllegalArgumentException
  {
    if (!this.sourcePositions.containsKey(partID)) {
      throw new IllegalArgumentException("This PartitionedSourcePosition object does not contain a SourcePosition for partition " + partID);
    }
    return ((SourcePosition)this.sourcePositions.get(partID)).compareTo(arg0);
  }
  
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    for (String key : this.sourcePositions.keySet())
    {
      SourcePosition value = (SourcePosition)this.sourcePositions.get(key);
      sb.append(key).append("=").append(value).append(",");
    }
    sb.deleteCharAt(sb.length() - 1);
    sb.append("]");
    
    String result = sb.toString();
    return result;
  }
}
