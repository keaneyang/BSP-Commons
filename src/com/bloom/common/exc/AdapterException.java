package com.bloom.common.exc;

import com.bloom.common.errors.Error;
import java.io.Serializable;
import java.text.DecimalFormat;

public class AdapterException
  extends Exception
  implements Serializable
{
  private static final long serialVersionUID = -3162936038255700823L;
  private static final String WASCCODE = "Web Action Source Component";
  private Error error;
  DecimalFormat df = new DecimalFormat("1000");
  String errorMessage;
  String componentName;
  
  public AdapterException()
  {
    this(Error.GENERIC_EXCEPTION);
  }
  
  public AdapterException(String message)
  {
    this(Error.GENERIC_EXCEPTION, message);
  }
  
  public AdapterException(Error error)
  {
    this.error = error;
    this.errorMessage = ("WA-" + error.toString());
  }
  
  public AdapterException(Error error, String message)
  {
    super(message);
    this.error = error;
    if (message != null) {
      this.errorMessage = ("WA-" + error.toString() + ". Cause: " + message);
    } else {
      this.errorMessage = ("WA-" + error.toString());
    }
  }
  
  public AdapterException(Error error, Throwable cause)
  {
    super(cause);
    this.error = error;
    if (cause.getMessage() != null) {
      this.errorMessage = ("WA-" + error.toString() + ". Cause: " + cause.getMessage());
    } else {
      this.errorMessage = ("WA-" + error.toString());
    }
  }
  
  public AdapterException(Error error, String message, Throwable cause)
  {
    super(message, cause);
    this.error = error;
  }
  
  public AdapterException(String message, Throwable cause)
  {
    super(message, cause);
    if (cause.getMessage() != null) {
      this.errorMessage = (message + ". Cause: " + cause.getMessage());
    } else if ((cause.getCause() != null) && (cause.getCause().getMessage() != null)) {
      this.errorMessage = (message + ". Cause: " + cause.getCause().getMessage());
    } else {
      this.errorMessage = message;
    }
  }
  
  public String getCode()
  {
    return "Web Action Source Component";
  }
  
  public String getErrorMessage()
  {
    return this.errorMessage;
  }
  
  public String getMessage()
  {
    return this.errorMessage;
  }
  
  public Error getType()
  {
    return this.error;
  }
}
