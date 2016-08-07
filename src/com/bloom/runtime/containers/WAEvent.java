package com.bloom.runtime.containers;

import com.bloom.event.SimpleEvent;
import com.bloom.recovery.Path;
import com.bloom.recovery.Position;
import com.bloom.recovery.SourcePosition;
import com.bloom.recovery.Path.Item;
import com.bloom.recovery.Path.ItemList;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WAEvent
  implements KryoSerializable, Serializable
{
  private static final long serialVersionUID = -6202211360415942342L;
  public Object data;
  public Position position = null;
  
  public WAEvent() {}
  
  public WAEvent(Object data)
  {
    this.data = data;
  }
  
  public WAEvent(Object data, Position pos)
  {
    this(data);
  }
  
  public void initValues(WAEvent that)
  {
    this.data = that.data;
    this.position = (that.position == null ? null : new Position(that.position));
  }
  
  public String toString()
  {
    return this.data.toString();
  }
  
  public void write(Kryo kryo, Output output)
  {
    kryo.writeClassAndObject(output, this.data);
    output.writeBoolean(this.position != null);
    if (this.position != null)
    {
      output.writeInt(this.position.size());
      for (Path p : this.position.values())
      {
        output.writeInt(p.pathComponentCount());
        for (int c = 0; c < p.pathComponentCount(); c++)
        {
          Path.Item item = p.getPathComponent(c);
          kryo.writeClassAndObject(output, item);
        }
        kryo.writeClassAndObject(output, p.getSourcePosition());
      }
    }
  }
  
  public void read(Kryo kryo, Input input)
  {
    this.data = kryo.readClassAndObject(input);
    boolean positionNotNull = input.readBoolean();
    if (positionNotNull)
    {
      Set<Path> paths = new HashSet();
      int numPaths = input.readInt();
      for (int c = 0; c < numPaths; c++)
      {
        List<Path.Item> items = new ArrayList();
        int numUuids = input.readInt();
        for (int u = 0; u < numUuids; u++)
        {
          Path.Item item = (Path.Item)kryo.readClassAndObject(input);
          items.add(item);
        }
        Path.ItemList pathItems = new Path.ItemList(items);
        SourcePosition sourcePosition = (SourcePosition)kryo.readClassAndObject(input);
        Path path = new Path(pathItems, sourcePosition);
        paths.add(path);
      }
      this.position = new Position(paths);
    }
  }
  
  public int hashCode()
  {
    if ((this.data instanceof SimpleEvent))
    {
      SimpleEvent e = (SimpleEvent)this.data;
      
      return Arrays.hashCode(e.payload);
    }
    return this.data.hashCode();
  }
  
  public boolean equals(Object o)
  {
    if ((o instanceof WAEvent))
    {
      WAEvent e = (WAEvent)o;
      if (((this.data instanceof SimpleEvent)) && ((e.data instanceof SimpleEvent)))
      {
        SimpleEvent e1 = (SimpleEvent)this.data;
        SimpleEvent e2 = (SimpleEvent)e.data;
        return Arrays.equals(e1.payload, e2.payload);
      }
      return this.data == null ? false : e.data == null ? true : this.data.equals(e.data);
    }
    return false;
  }
}
