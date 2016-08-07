package com.bloom.event;

import com.bloom.uuid.UUID;

import java.util.Map;

public abstract class Event
{
  public abstract UUID get_wa_SimpleEvent_ID();
  
  public void setID(UUID ID) {}
  
  public abstract long getTimeStamp();
  
  public void setTimeStamp(long timeStamp) {}
  
  public abstract void setPayload(Object[] paramArrayOfObject);
  
  public abstract Object[] getPayload();
  
  public abstract String getKey();
  
  public abstract void setKey(String paramString);
  
  public boolean setFromContextMap(Map<String, Object> map)
  {
    return false;
  }
}
