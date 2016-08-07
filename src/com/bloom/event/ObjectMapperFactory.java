package com.bloom.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;

public class ObjectMapperFactory
{
  private static ObjectMapper mapper = new ObjectMapper();
  
  private ObjectMapperFactory()
  {
    mapper.registerModule(new JodaModule());
    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
  }
  
  public static ObjectMapper getInstance()
  {
    return mapper;
  }
  
  public static ObjectMapper newInstance()
  {
    ObjectMapper jsonMapper = new ObjectMapper();
    jsonMapper.registerModule(new JodaModule());
    jsonMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
    return jsonMapper;
  }
}
