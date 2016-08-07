package com.bloom.proc.events;

import com.bloom.anno.EventType;
import com.bloom.anno.EventTypeData;
import com.bloom.event.SourceEvent;
import com.bloom.uuid.UUID;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.util.HashMap;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

@EventType(schema="Internal", classification="All", uri="com.bloom.proc.events:WAEvent:1.0")
public class WAEvent
  extends SourceEvent
{
  private static final long serialVersionUID = -861041374336281417L;
  @EventTypeData
  public Object[] data;
  public HashMap<String, Object> metadata;
  public Object[] before;
  public byte[] dataPresenceBitMap;
  public byte[] beforePresenceBitMap;
  public UUID typeUUID = null;
  
  public void setData(int index, Object o)
  {
    this.data[index] = o;
    int pos = index / 7;
    int offset = index % 7;
    byte bitset = (byte)(1 << offset);
    byte b = this.dataPresenceBitMap[pos];
    this.dataPresenceBitMap[pos] = ((byte)(b | bitset));
  }
  
  public void setBefore(int index, Object o)
  {
    this.before[index] = o;
    int pos = index / 7;
    int offset = index % 7;
    byte bitset = (byte)(1 << offset);
    byte b = this.beforePresenceBitMap[pos];
    this.beforePresenceBitMap[pos] = ((byte)(b | bitset));
  }
  
  public WAEvent() {}
  
  public WAEvent(int dataFieldCount, UUID sourceUUID)
  {
    super(sourceUUID);
    int bitmapSize = dataFieldCount / 7 + 1;
    this.dataPresenceBitMap = new byte[bitmapSize];
    this.beforePresenceBitMap = new byte[bitmapSize];
  }
  
  public void setPayload(Object[] payload)
  {
    this.data = payload;
  }
  
  public Object[] getPayload()
  {
    return new Object[] { this.data, this.metadata };
  }
  
  public JSONObject getJSONObject()
  {
    JSONObject superJSON = super.getJSONObject();
    JSONArray jArray = new JSONArray();
    for (Object object : this.data) {
      jArray.put(object);
    }
    try
    {
      superJSON.put("data", jArray);
      superJSON.put("meta", this.metadata);
    }
    catch (JSONException e) {}
    return superJSON;
  }
  
  public void write(Kryo kryo, Output output)
  {
    super.write(kryo, output);
    output.writeBoolean(this.data != null);
    if (this.data != null)
    {
      output.writeInt(this.data.length);
      for (int i = 0; i < this.data.length; i++) {
        kryo.writeClassAndObject(output, this.data[i]);
      }
    }
    kryo.writeClassAndObject(output, this.metadata);
    output.writeBoolean(this.before != null);
    if (this.before != null)
    {
      output.writeInt(this.before.length);
      for (int i = 0; i < this.before.length; i++) {
        kryo.writeClassAndObject(output, this.before[i]);
      }
    }
    output.writeBoolean(this.dataPresenceBitMap != null);
    if (this.dataPresenceBitMap != null)
    {
      output.writeInt(this.dataPresenceBitMap.length);
      output.writeBytes(this.dataPresenceBitMap);
    }
    output.writeBoolean(this.beforePresenceBitMap != null);
    if (this.beforePresenceBitMap != null)
    {
      output.writeInt(this.beforePresenceBitMap.length);
      output.writeBytes(this.beforePresenceBitMap);
    }
  }
  
  public void read(Kryo kryo, Input input)
  {
    super.read(kryo, input);
    boolean dataNotNull = input.readBoolean();
    if (dataNotNull)
    {
      int dataLen = input.readInt();
      this.data = new Object[dataLen];
      for (int i = 0; i < dataLen; i++) {
        this.data[i] = kryo.readClassAndObject(input);
      }
    }
    this.metadata = ((HashMap)kryo.readClassAndObject(input));
    boolean beforeNotNull = input.readBoolean();
    if (beforeNotNull)
    {
      int beforeLen = input.readInt();
      this.before = new Object[beforeLen];
      for (int i = 0; i < beforeLen; i++) {
        this.before[i] = kryo.readClassAndObject(input);
      }
    }
    boolean dataPresenceBitMapNotNull = input.readBoolean();
    if (dataPresenceBitMapNotNull)
    {
      int len = input.readInt();
      this.dataPresenceBitMap = new byte[len];
      input.readBytes(this.dataPresenceBitMap);
    }
    boolean beforePresenceBitMapNotNull = input.readBoolean();
    if (beforePresenceBitMapNotNull)
    {
      int len = input.readInt();
      this.beforePresenceBitMap = new byte[len];
      input.readBytes(this.beforePresenceBitMap);
    }
  }
}
