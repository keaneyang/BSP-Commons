package com.bloom.common.exc;

public class TargetShutdownException
  extends Exception
{
  private static final long serialVersionUID = -7596411329248881658L;
  
  public TargetShutdownException()
  {
    super("Target going to shutdown");
  }
  
  public TargetShutdownException(String message)
  {
    super(message);
  }
  
  public TargetShutdownException(Throwable cause)
  {
    super(cause);
  }
  
  public TargetShutdownException(String message, Throwable cause)
  {
    super(message, cause);
  }
  
  public TargetShutdownException(String message, Throwable cause, boolean enableSuppression, boolean writeableStackTrace)
  {
    super(message, cause, enableSuppression, writeableStackTrace);
  }
}
