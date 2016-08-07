package com.bloom.recovery;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class NSKSourcePosition
  extends SourcePosition
{
  private static final long serialVersionUID = -6042045728463138984L;
  private Map<String, String> positionMap;
  private String currentAuditTrailName;
  
  public NSKSourcePosition()
  {
    this.currentAuditTrailName = null;
    this.positionMap = new ConcurrentHashMap();
  }
  
  public NSKSourcePosition(NSKSourcePosition sp)
  {
    this.positionMap = new ConcurrentHashMap();
    this.positionMap.putAll(sp.positionMap);
    this.currentAuditTrailName = sp.currentAuditTrailName;
  }
  
  public synchronized void updateAuditTrail(String auditTrailName, String position)
  {
    this.currentAuditTrailName = auditTrailName;
    if (this.positionMap.containsKey(this.currentAuditTrailName))
    {
      String[] oldPos = ((String)this.positionMap.get(this.currentAuditTrailName)).split(":");
      String[] newPos = position.split(":");
      int compare = Long.valueOf(newPos[0]).compareTo(Long.valueOf(oldPos[0]));
      if (compare > 0) {
        this.positionMap.put(this.currentAuditTrailName, position);
      }
    }
    else
    {
      this.positionMap.put(this.currentAuditTrailName, position);
    }
  }
  
  public String getCurrentAuditTrailName()
  {
    return this.currentAuditTrailName;
  }
  
  public ConcurrentHashMap<String, String> getPositionMap()
  {
    return (ConcurrentHashMap)this.positionMap;
  }
  
  public int compareTo(SourcePosition arg0)
  {
    String atName = this.currentAuditTrailName;
    if ((this.positionMap.containsKey(atName)) && (((NSKSourcePosition)arg0).positionMap.containsKey(atName)))
    {
      String[] thisPos = ((String)this.positionMap.get(atName)).split(":");
      String[] thatPos = ((String)((NSKSourcePosition)arg0).positionMap.get(atName)).split(":");
      int compare = Long.valueOf(thisPos[0]).compareTo(Long.valueOf(thatPos[0]));
      return compare;
    }
    return 1;
  }
  
  public String toString()
  {
    String position = "";
    if (!this.positionMap.isEmpty()) {
      for (Map.Entry<String, String> entry : this.positionMap.entrySet())
      {
        String key = (String)entry.getKey();
        String value = (String)entry.getValue();
        position = position + key + "-" + value + ";";
      }
    }
    return position;
  }
}
