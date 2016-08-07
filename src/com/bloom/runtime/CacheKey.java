package com.bloom.runtime;

import com.bloom.distribution.Partitionable;

import java.io.Serializable;

public class CacheKey
  extends RecordKey
  implements Partitionable, Serializable
{
  private static final long serialVersionUID = -1425225889130955124L;
  private static final Integer NULL_VALUE = new Integer(0);
  
  public CacheKey(Object K)
  {
    this.singleField = K;
    this.fields = null;
  }
  
  public boolean usePartitionId()
  {
    return false;
  }
  
  public Object getPartitionKey()
  {
    return this.singleField == null ? NULL_VALUE : this.singleField;
  }
  
  public int getPartitionId()
  {
    return hashCode();
  }
  
  public boolean equals(Object o)
  {
    CacheKey that = (CacheKey)o;
    if (that.singleField == null) {
      return false;
    }
    if (that.singleField.equals(this.singleField)) {
      return true;
    }
    return false;
  }
  
  public int hashCode()
  {
    if (this.singleField != null) {
      return this.singleField.hashCode();
    }
    return 0;
  }
}
