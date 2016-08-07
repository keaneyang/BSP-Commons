package com.bloom.event;

import com.bloom.uuid.UUID;

import java.util.Map;

public abstract interface WactionConvertible
{
  public abstract void convertFromWactionToEvent(long paramLong, UUID paramUUID, String paramString, Map<String, Object> paramMap);
}


