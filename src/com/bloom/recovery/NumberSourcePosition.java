package com.bloom.recovery;

public class NumberSourcePosition
  extends SourcePosition
{
  private static final long serialVersionUID = -6945135854956636769L;
  public long value;
  
  public NumberSourcePosition(long value)
  {
    this.value = value;
  }
  
  public int compareTo(SourcePosition arg0)
  {
    if (!(arg0 instanceof NumberSourcePosition)) {
      throw new IllegalArgumentException("Require LongSourcePosition but got " + arg0);
    }
    NumberSourcePosition lsp = (NumberSourcePosition)arg0;
    long diff = this.value - lsp.value;
    if (diff < 0L) {
      return -1;
    }
    if (diff > 0L) {
      return 1;
    }
    return 0;
  }
  
  public String toString()
  {
    return "LongSourcePosition value=" + this.value;
  }
}
