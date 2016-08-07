package com.bloom.event;

import com.bloom.anno.SpecialEventAttribute;
import com.bloom.distribution.Partitionable;
import com.bloom.uuid.UUID;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import flexjson.JSON;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class SimpleEvent
  extends Event
  implements Serializable, Partitionable, KryoSerializable
{
  private static final long serialVersionUID = -4431260571600094020L;
  public static transient ObjectMapper jsonMapper = ObjectMapperFactory.newInstance();
  @SpecialEventAttribute
  public String _id;
  @SpecialEventAttribute
  @JSON(include=false)
  @JsonIgnore
  public UUID _wa_SimpleEvent_ID;
  @SpecialEventAttribute
  public long timeStamp;
  @SpecialEventAttribute
  public long originTimeStamp;
  @SpecialEventAttribute
  public String key;
  @JSON(include=false)
  @JsonIgnore
  @SpecialEventAttribute
  public Object[] payload;
  @JSON(include=false)
  @JsonIgnore
  @SpecialEventAttribute
  public List<Object[]> linkedSourceEvents;
  @JSON(include=false)
  @JsonIgnore
  @SpecialEventAttribute
  public byte[] fieldIsSet;
  @JSON(include=false)
  @JsonIgnore
  @SpecialEventAttribute
  public boolean allFieldsSet = true;
  
  public SimpleEvent() {}
  
  public SimpleEvent(long timestamp)
  {
    this.timeStamp = timestamp;
    this._wa_SimpleEvent_ID = new UUID(this.timeStamp);
  }
  
  public void init(long timestamp)
  {
    this.timeStamp = timestamp;
    this._wa_SimpleEvent_ID = new UUID(this.timeStamp);
  }
  
  @JSON(include=false)
  @JsonIgnore
  public UUID get_wa_SimpleEvent_ID()
  {
    return this._wa_SimpleEvent_ID;
  }
  
  public String getIDString()
  {
    return this._wa_SimpleEvent_ID == null ? "<NOTSET>" : this._wa_SimpleEvent_ID.getUUIDString();
  }
  
  public void setIDString(String IDstring)
  {
    this._wa_SimpleEvent_ID = new UUID(IDstring);
  }
  
  public long getTimeStamp()
  {
    return this.timeStamp;
  }
  
  public void setTimeStamp(long timestamp)
  {
    this.timeStamp = timestamp;
  }
  
  public void setPayload(Object[] payload)
  {
    this.payload = payload;
  }
  
  public Object[] getPayload()
  {
    return this.payload;
  }
  
  public String getKey()
  {
    return this.key;
  }
  
  public void setKey(String key)
  {
    this.key = key;
  }
  
  public Object fromJSON(String json)
  {
    try
    {
      return jsonMapper.readValue(json, getClass());
    }
    catch (Exception e) {}
    return "<Undeserializable>";
  }
  
  public String toJSON()
  {
    try
    {
      return jsonMapper.writeValueAsString(this);
    }
    catch (Exception e) {}
    return "<Undeserializable>";
  }
  
  public String toString()
  {
    return toJSON();
  }
  
  public boolean usePartitionId()
  {
    return false;
  }
  
  @JSON(include=false)
  @JsonIgnore
  public Object getPartitionKey()
  {
    return this.key == null ? this._wa_SimpleEvent_ID : this.key;
  }
  
  @JSON(include=false)
  @JsonIgnore
  public int getPartitionId()
  {
    return 0;
  }
  
  public void setSourceEvents(List<Object[]> sourceEvents)
  {
    this.linkedSourceEvents = sourceEvents;
  }
  
  @JSON(include=false)
  @JsonIgnore
  public JSONObject getJSONObject()
  {
    JSONObject jsonObject = new JSONObject();
    try
    {
      jsonObject.put("_id", this._wa_SimpleEvent_ID);
      jsonObject.put("timeStamp", this.timeStamp);
      jsonObject.put("key", this.key);
    }
    catch (JSONException e) {}
    return jsonObject;
  }
  
  public void write(Kryo kryo, Output output)
  {
    if (this._wa_SimpleEvent_ID != null)
    {
      output.writeByte(0);
      this._wa_SimpleEvent_ID.write(kryo, output);
    }
    else
    {
      output.writeByte(1);
    }
    output.writeLong(this.timeStamp);
    output.writeString(this.key);
    output.writeBoolean(this.payload != null);
    if (this.payload != null)
    {
      output.writeInt(this.payload.length);
      for (int p = 0; p < this.payload.length; p++) {
        kryo.writeClassAndObject(output, this.payload[p]);
      }
    }
    output.writeBoolean(this.linkedSourceEvents != null);
    if (this.linkedSourceEvents != null)
    {
      output.writeInt(this.linkedSourceEvents.size());
      for (Object[] oa : this.linkedSourceEvents) {
        kryo.writeClassAndObject(output, oa);
      }
    }
    output.writeBoolean(this.fieldIsSet != null);
    if (this.fieldIsSet != null)
    {
      output.writeInt(this.fieldIsSet.length);
      output.write(this.fieldIsSet);
    }
  }
  
  public void read(Kryo kryo, Input input)
  {
    byte has_wa_SimpleEvent_ID = input.readByte();
    if (has_wa_SimpleEvent_ID == 0)
    {
      this._wa_SimpleEvent_ID = new UUID();
      this._wa_SimpleEvent_ID.read(kryo, input);
    }
    this.timeStamp = input.readLong();
    this.key = input.readString();
    boolean payloadNotNull = input.readBoolean();
    if (payloadNotNull)
    {
      int numPayloadObjects = input.readInt();
      this.payload = new Object[numPayloadObjects];
      for (int p = 0; p < numPayloadObjects; p++) {
        this.payload[p] = kryo.readClassAndObject(input);
      }
    }
    boolean linkedSourceEventsNotNull = input.readBoolean();
    if (linkedSourceEventsNotNull)
    {
      int numLinkedSourceEvents = input.readInt();
      this.linkedSourceEvents = new ArrayList(numLinkedSourceEvents);
      for (int l = 0; l < numLinkedSourceEvents; l++)
      {
        Object[] oa = (Object[])kryo.readClassAndObject(input);
        this.linkedSourceEvents.add(oa);
      }
    }
    boolean fieldIsSetNotNull = input.readBoolean();
    if (fieldIsSetNotNull)
    {
      int fieldIsSetLength = input.readInt();
      this.fieldIsSet = new byte[fieldIsSetLength];
      input.read(this.fieldIsSet);
    }
  }
  
  public boolean isFieldSet(int index)
  {
    if (this.allFieldsSet) {
      return true;
    }
    if (this.fieldIsSet == null) {
      return true;
    }
    int pos = index / 7;
    int offset = index % 7;
    byte b = (byte)(1 << offset);
    int val = (this.fieldIsSet[pos] & b) >> offset;
    return val == 1;
  }
  
  public int hashCode()
  {
    return this._wa_SimpleEvent_ID.hashCode();
  }
  
  public boolean equals(Object obj)
  {
    if ((obj instanceof SimpleEvent)) {
      return this._wa_SimpleEvent_ID.equals(((SimpleEvent)obj)._wa_SimpleEvent_ID);
    }
    return false;
  }
}
