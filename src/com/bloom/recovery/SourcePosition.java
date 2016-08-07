package com.bloom.recovery;


import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import java.io.Serializable;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME)
public abstract class SourcePosition
  implements Serializable, Comparable<SourcePosition>
{
  private static final long serialVersionUID = 1657905561025367794L;
  
  public abstract int compareTo(SourcePosition paramSourcePosition);
}