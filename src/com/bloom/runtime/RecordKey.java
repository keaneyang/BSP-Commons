package com.bloom.runtime;

import com.bloom.runtime.utils.StringUtils;
import java.util.Arrays;
import org.apache.log4j.Logger;

public class RecordKey
  implements Comparable<RecordKey>
{
  private static final Logger logger = Logger.getLogger(RecordKey.class.getName());
  protected Object[] fields;
  public Object singleField;
  protected int hashCode;
  
  public RecordKey()
  {
    this(null);
  }
  
  public RecordKey(Object[] fields)
  {
    if ((fields != null) && (fields.length == 1))
    {
      this.singleField = fields[0];
      this.fields = null;
    }
    else
    {
      this.singleField = null;
      this.fields = fields;
    }
    this.hashCode = 0;
  }
  
  public String toString()
  {
    if (this.singleField != null) {
      return this.singleField.toString();
    }
    return StringUtils.join(this.fields);
  }
  
  public String toPartitionKey()
  {
    if ((this.fields == null) && (this.singleField != null)) {
      return String.valueOf(this.singleField);
    }
    return StringUtils.join(this.fields, "#");
  }
  
  public int hashCode()
  {
    if (this.hashCode == 0) {
      if (this.singleField != null) {
        this.hashCode = this.singleField.hashCode();
      } else {
        this.hashCode = Arrays.hashCode(this.fields);
      }
    }
    return this.hashCode;
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (!(o instanceof RecordKey)) {
      return false;
    }
    RecordKey other = (RecordKey)o;
    if (this.singleField != null) {
      return this.singleField.equals(other.singleField);
    }
    return Arrays.equals(this.fields, other.fields);
  }
  
  public int compareTo(RecordKey o)
  {
    assert (this.fields.length == o.fields.length);
    if (this.singleField != null) {
      return compare(this.singleField, o.singleField);
    }
    for (int i = 0; i < this.fields.length; i++)
    {
      int r = compare(this.fields[i], o.fields[i]);
      if (r != 0) {
        return r;
      }
    }
    return 0;
  }
  
  public boolean isEmpty()
  {
    return this.fields.length == 0;
  }
  
  private static int compare(Object a, Object b)
  {
    if (a == null) {
      return b == null ? 0 : 1;
    }
    if (b == null) {
      return a == null ? 0 : -1;
    }
    return ((Comparable)a).compareTo(b);
  }
  
  public static final RecordKey emptyKey = createKey(new Object[0]);
  
  public static String getObjArrayKeyFactory()
  {
    return RecordKey.class.getName() + ".createKeyFromObjArray";
  }
  
  public static RecordKey createKeyFromObjArray(Object[] args)
  {
    return new RecordKey(args);
  }
  
  public static RecordKey createRecordKey(Object args)
  {
    return new CacheKey(args);
  }
  
  public static RecordKey cacheRecordKeyCreator(Object[] arg)
  {
    return new cacheRecordKey(arg);
  }
  
  public static RecordKey createKey(Object... args)
  {
    return new RecordKey(args);
  }
}
