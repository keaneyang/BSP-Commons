package com.bloom.runtime;

import com.bloom.distribution.Partitionable;

class cacheRecordKey
  extends RecordKey
  implements Partitionable
{
  public cacheRecordKey(Object[] arg)
  {
    super(arg);
  }
  
  public boolean usePartitionId()
  {
    return false;
  }
  
  public Object getPartitionKey()
  {
    return this.singleField == null ? this.fields[0] : this.singleField;
  }
  
  public int getPartitionId()
  {
    return 0;
  }
}
