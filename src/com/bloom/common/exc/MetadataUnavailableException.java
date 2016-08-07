package com.bloom.common.exc;

import java.io.Serializable;

public class MetadataUnavailableException
  extends SystemException
  implements Serializable
{
  private static final long serialVersionUID = 1671729232291071244L;
  
  public MetadataUnavailableException()
  {
    super("Resource not available. Source is going to Shutdown");
  }
  
  public MetadataUnavailableException(String message)
  {
    super(message);
  }
  
  public MetadataUnavailableException(Throwable cause)
  {
    super(cause);
  }
  
  public MetadataUnavailableException(String message, Throwable cause)
  {
    super(message, cause);
  }
  
  public MetadataUnavailableException(String message, Throwable cause, boolean enableSuppression, boolean writeableStackTrace)
  {
    super(message, cause, enableSuppression, writeableStackTrace);
  }
}
