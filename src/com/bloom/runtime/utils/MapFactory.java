package com.bloom.runtime.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

public class MapFactory
{
  public static <K, V> HashMap<K, V> makeMap()
  {
    return new HashMap();
  }
  
  public static TreeMap<String, Object> makeCaseInsensitiveMap()
  {
    return new TreeMap(String.CASE_INSENSITIVE_ORDER);
  }
  
  public static <K, V> LinkedHashMap<K, V> makeLinkedMap()
  {
    return new LinkedHashMap();
  }
  
  public static <T> List<T> makeList()
  {
    return new ArrayList();
  }
}
