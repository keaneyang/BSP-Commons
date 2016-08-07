package com.bloom.common.exc;

import java.io.Serializable;

public class ConnectionException
  extends SystemException
  implements Serializable
{
  private static final long serialVersionUID = -868743265768367780L;
  
  public ConnectionException()
  {
    super("Resource not available. Source is going to Shutdown");
  }
  
  public ConnectionException(String message)
  {
    super(message);
  }
  
  public ConnectionException(Throwable cause)
  {
    super(cause);
  }
  
  public ConnectionException(String message, Throwable cause)
  {
    super(message, cause);
  }
  
  public ConnectionException(String message, Throwable cause, boolean enableSuppression, boolean writeableStackTrace)
  {
    super(message, cause, enableSuppression, writeableStackTrace);
  }
}
