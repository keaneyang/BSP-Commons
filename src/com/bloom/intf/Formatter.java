package com.bloom.intf;

public abstract interface Formatter
{
  public abstract byte[] format(Object paramObject)
    throws Exception;
  
  public abstract byte[] addHeader()
    throws Exception;
  
  public abstract byte[] addFooter()
    throws Exception;
}


