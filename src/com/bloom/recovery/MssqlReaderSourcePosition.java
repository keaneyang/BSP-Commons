package com.bloom.recovery;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MssqlReaderSourcePosition
  extends SourcePosition
{
  private static final long serialVersionUID = -6042045728463138984L;
  ConcurrentMap<String, String> positionMap;
  String currentTableName;
  
  public MssqlReaderSourcePosition()
  {
    this.currentTableName = null;
    this.positionMap = new ConcurrentHashMap();
  }
  
  public MssqlReaderSourcePosition(MssqlReaderSourcePosition recordposition)
  {
    this.positionMap = new ConcurrentHashMap();
    
    this.positionMap.putAll(recordposition.positionMap);
    this.currentTableName = recordposition.currentTableName;
  }
  
  public void updateTablePosition(String tableName, String position)
  {
    this.currentTableName = tableName;
    if (this.positionMap.containsKey(this.currentTableName))
    {
      int compare = position.compareTo((String)this.positionMap.get(tableName));
      if (compare > 0) {
        this.positionMap.put(this.currentTableName, position);
      }
    }
    else
    {
      this.positionMap.put(this.currentTableName, position);
    }
  }
  
  public String getCurrentTableName()
  {
    return this.currentTableName;
  }
  
  public ConcurrentMap<String, String> getPositionMap()
  {
    return this.positionMap;
  }
  
  public String getMinPosition(String currentTableName)
  {
    return (String)this.positionMap.get(currentTableName);
  }
  
  public int compareTo(SourcePosition arg0)
  {
    if (!(arg0 instanceof MssqlReaderSourcePosition)) {
      throw new IllegalArgumentException("Attempted to compare a MssqlReaderSourcePosition to a " + arg0.getClass().getSimpleName());
    }
    MssqlReaderSourcePosition that = (MssqlReaderSourcePosition)arg0;
    
    String tableName = this.currentTableName;
    int result;
    if ((this.positionMap.containsKey(tableName)) && (that.positionMap.containsKey(tableName))) {
      result = ((String)this.positionMap.get(tableName)).compareTo((String)that.positionMap.get(tableName));
    } else {
      result = 1;
    }
    return result;
  }
  
  public String toString()
  {
    String position = "";
    if (!this.positionMap.isEmpty()) {
      for (Map.Entry entry : this.positionMap.entrySet())
      {
        String key = (String)entry.getKey();
        String value = (String)entry.getValue();
        position = " " + position + key + "=" + value + ";";
      }
    }
    return "MssqlReaderSourcePosition:" + position;
  }
}
