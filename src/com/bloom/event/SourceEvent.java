package com.bloom.event;

import com.bloom.anno.SpecialEventAttribute;
import com.bloom.uuid.UUID;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class SourceEvent
  extends SimpleEvent
{
  private static final long serialVersionUID = -1311015713656728401L;
  private static final Logger logger = Logger.getLogger(SourceEvent.class);
  @SpecialEventAttribute
  public UUID sourceUUID;
  
  public SourceEvent()
  {
    this.sourceUUID = null;
  }
  
  public SourceEvent(UUID sourceUUID)
  {
    super(System.currentTimeMillis());
    this.sourceUUID = sourceUUID;
  }
  
  public JSONObject getJSONObject()
  {
    JSONObject superJSON = super.getJSONObject();
    try
    {
      superJSON.put("sourceUUID", this.sourceUUID);
    }
    catch (JSONException e) {}
    return superJSON;
  }
  
  public void write(Kryo kryo, Output output)
  {
    super.write(kryo, output);
    if (this.sourceUUID != null)
    {
      output.writeBoolean(true);
      this.sourceUUID.write(kryo, output);
    }
    else
    {
      output.writeBoolean(false);
    }
  }
  
  public void read(Kryo kryo, Input input)
  {
    super.read(kryo, input);
    boolean hasSouceUuid = input.readBoolean();
    if (hasSouceUuid)
    {
      this.sourceUUID = new UUID();
      this.sourceUUID.read(kryo, input);
    }
    else
    {
      this.sourceUUID = null;
    }
  }
}
