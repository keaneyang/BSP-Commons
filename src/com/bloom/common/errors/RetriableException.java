package com.bloom.common.errors;

public class RetriableException
  extends Exception
{
  public RetriableException() {}
  
  public RetriableException(String message)
  {
    super(message);
  }
  
  public RetriableException(Throwable cause)
  {
    super(cause);
  }
  
  public RetriableException(String message, Throwable cause)
  {
    super(message, cause);
  }
}
