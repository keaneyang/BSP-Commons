 package com.bloom.recovery;

 import org.joda.time.DateTime;

 
 
 
 public class SalesforceReaderSourcePosition
   extends SourcePosition
 {
   private static final long serialVersionUID = -5432586375077517449L;
   private boolean enabledInitialLoad;
   private boolean initializationDone;
   private String lastEpochDataReceived;
   
   public SalesforceReaderSourcePosition(String lastEpoc, boolean enableInitialLoad, boolean initializationDone)
   {
     this.lastEpochDataReceived = lastEpoc;
     this.enabledInitialLoad = enableInitialLoad;
     this.initializationDone = initializationDone;
   }
   
   public String getLastEpochDataReceived() {
     return this.lastEpochDataReceived;
   }
   
   public void setLastEpochDataReceived(String lastEpochDataReceived) {
     this.lastEpochDataReceived = lastEpochDataReceived;
   }
   
   public boolean isEnabledInitialLoad() {
     return this.enabledInitialLoad;
   }
   
   public void setEnabledInitialLoad(boolean enabledInitialLoad) {
     this.enabledInitialLoad = enabledInitialLoad;
   }
   
   public boolean isInitializationDone() {
     return this.initializationDone;
   }
   
   public void setInitializationDone(boolean initializationDone) {
     this.initializationDone = initializationDone;
   }
   
   public int compareTo(SourcePosition that)
   {
     if ((that != null) && ((that instanceof SalesforceReaderSourcePosition))) {
       SalesforceReaderSourcePosition thatPosition = (SalesforceReaderSourcePosition)that;
       return DateTime.parse(this.lastEpochDataReceived).compareTo(DateTime.parse(thatPosition.lastEpochDataReceived));
     }
     return Integer.MIN_VALUE;
   }
   
 
   public String toString()
   {
     return "SalesforceReaderSourcePosition [enabledInitialLoad=" + this.enabledInitialLoad + ", initializationDone=" + this.initializationDone + ", lastFailedPollingDateTime=" + this.lastEpochDataReceived + "]";
   }
 }

