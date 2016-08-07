package com.bloom.runtime.utils;

import java.util.List;
import java.util.Map;

public class StringUtils
{
  public static String join(List<?> list, String separator)
  {
    if (list.size() == 1) {
      return list.get(0).toString();
    }
    StringBuilder sb = new StringBuilder();
    String sep = "";
    for (Object o : list)
    {
      sb.append(sep).append(o);
      sep = separator;
    }
    return sb.toString();
  }
  
  public static String join(List<?> list)
  {
    return join(list, ",");
  }
  
  public static <T> String join(T[] list)
  {
    return join(list, ",");
  }
  
  public static String join(Object[] list, String separator)
  {
    StringBuilder sb = new StringBuilder();
    String sep = "";
    for (Object o : list)
    {
      sb.append(sep).append(o);
      sep = separator;
    }
    return sb.toString();
  }
  
  public static Map<String, Object> getKeyValuePairs(String inputStr, String itemSplitter, String keyValSplitter)
  {
    Map<String, Object> results = MapFactory.makeCaseInsensitiveMap();
    if ((inputStr == null) || (inputStr.isEmpty())) {
      return results;
    }
    if (itemSplitter == null) {
      itemSplitter = " ";
    }
    String[] items = inputStr.split(itemSplitter);
    if (keyValSplitter == null) {
      keyValSplitter = "=";
    }
    for (String item : items)
    {
      String[] parts = item.split(keyValSplitter);
      if ((parts != null) && (parts.length == 2) && 
        (parts[0] != null) && (parts[1] != null)) {
        results.put(parts[0].trim(), parts[1].trim());
      }
    }
    return results;
  }
  
  public static int getInt(Map<String, Object> map, String key)
    throws ClassCastException, NumberFormatException, NullPointerException
  {
    int c = 0;
    
    Object val = map.get(key);
    if ((val instanceof Number)) {
      c = ((Number)val).intValue();
    } else if ((val instanceof String)) {
      c = Integer.parseInt((String)val);
    }
    return c;
  }
}
