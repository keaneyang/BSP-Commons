package com.bloom.distribution;

public abstract interface Partitionable
{
  public static final int NUM_PARTITIONS = 1023;
  
  public abstract boolean usePartitionId();
  
  public abstract Object getPartitionKey();
  
  public abstract int getPartitionId();
}


