package com.bloom.recovery;

import java.util.Arrays;

public class CDCReaderSourcePosition
  extends SourcePosition
{
  private static final long serialVersionUID = -6042045728463138984L;
  public byte[] checkpointdetails = new byte[128];
  
  private CDCReaderSourcePosition() {}
  
  public CDCReaderSourcePosition(byte[] recordposition)
  {
    this.checkpointdetails = Arrays.copyOf(recordposition, recordposition.length);
  }
  
  public int compareTo(SourcePosition arg0)
  {
    if (!(arg0 instanceof CDCReaderSourcePosition)) {
      throw new IllegalArgumentException("Attempted to compare a CDCReaderSourcePosition to a " + arg0.getClass().getSimpleName());
    }
    CDCReaderSourcePosition that = (CDCReaderSourcePosition)arg0;
    
    return byteArraycompareTo(this.checkpointdetails, this.checkpointdetails.length, that.checkpointdetails, that.checkpointdetails.length);
  }
  
  public int byteArraycompareTo(byte[] buffer1, int length1, byte[] buffer2, int length2)
  {
    if ((buffer1 == buffer2) && (length1 == length2)) {
      return 0;
    }
    if (length1 != length2) {
      return length1 - length2;
    }
    int i = 0;
    for (int j = 0; (i < length1) && (j < length2); j++)
    {
      int a = buffer1[i] & 0xFF;
      int b = buffer2[j] & 0xFF;
      if (a != b) {
        return a - b;
      }
      i++;
    }
    return 0;
  }
  
  public String toString()
  {
    return "CDCReaderPosition:" + new String(this.checkpointdetails);
  }
}
