package com.bloom.common.exc;

import java.io.Serializable;

public class SystemException
  extends Exception
  implements Serializable
{
  private static final long serialVersionUID = 1383389949889394868L;
  
  public SystemException()
  {
    super("Resource not available. Source is going to Shutdown");
  }
  
  public SystemException(String message)
  {
    super(message);
  }
  
  public SystemException(Throwable cause)
  {
    super(cause);
  }
  
  public SystemException(String message, Throwable cause)
  {
    super(message, cause);
  }
  
  public SystemException(String message, Throwable cause, boolean enableSuppression, boolean writeableStackTrace)
  {
    super(message, cause, enableSuppression, writeableStackTrace);
  }
}
