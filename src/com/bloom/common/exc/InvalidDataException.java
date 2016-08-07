package com.bloom.common.exc;

public class InvalidDataException
  extends Exception
{
  private static final long serialVersionUID = -1963390711417089291L;
  
  public InvalidDataException()
  {
    super("Input data is either malformed or has an incorrect format");
  }
  
  public InvalidDataException(String message)
  {
    super(message);
  }
  
  public InvalidDataException(Throwable cause)
  {
    super(cause);
  }
  
  public InvalidDataException(String message, Throwable cause)
  {
    super(message, cause);
  }
  
  public InvalidDataException(String message, Throwable cause, boolean enableSuppression, boolean writeableStackTrace)
  {
    super(message, cause, enableSuppression, writeableStackTrace);
  }
}
