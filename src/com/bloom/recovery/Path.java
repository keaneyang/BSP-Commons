package com.bloom.recovery;

import com.bloom.uuid.UUID;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public class Path
  implements Serializable, Comparable<Path>
{
  private static final long serialVersionUID = 1680308749357411041L;
  private static Logger logger = Logger.getLogger(Path.class);
  public int id = 0;
  public UUID applicationUuid = null;
  private final ItemList pathItems;
  private final SourcePosition sourcePosition;
  
  private Path()
  {
    this.pathItems = null;
    this.sourcePosition = null;
  }
  
  public Path(UUID sourceUUID, String distributionID, SourcePosition sourcePosition)
  {
    ArrayList<Item> items = new ArrayList();
    items.add(new Item(sourceUUID, distributionID));
    this.pathItems = new ItemList(items);
    this.sourcePosition = sourcePosition;
  }
  
  public Path(ItemList pathItems, SourcePosition sourcePosition)
  {
    this.pathItems = pathItems;
    this.sourcePosition = sourcePosition;
  }
  
  public Path copyAugmentedWith(Item item)
  {
    ItemList augmentedItemList = this.pathItems.copyAugmentedWith(item);
    Path result = new Path(augmentedItemList, this.sourcePosition);
    return result;
  }
  
  public Path(Path that)
  {
    this.id = that.id;
    this.applicationUuid = that.applicationUuid;
    List<Item> addItems = new ArrayList();
    synchronized (that.pathItems)
    {
      for (Item thatItem : that.pathItems.list) {
        addItems.add(thatItem);
      }
    }
    List<Item> its = new ArrayList();
    for (Item item : addItems) {
      its.add(item);
    }
    this.pathItems = new ItemList(its);
    this.sourcePosition = that.sourcePosition;
  }
  
  public SourcePosition getSourcePosition()
  {
    return this.sourcePosition;
  }
  
  public int pathComponentCount()
  {
    return this.pathItems.size();
  }
  
  public Item getPathComponent(int index)
  {
    return this.pathItems.get(index);
  }
  
  public ItemList getPathItems()
  {
    return this.pathItems;
  }
  
  public int getPathHash()
  {
    return this.pathItems.hashCode();
  }
  
  public boolean contains(UUID uuid)
  {
    for (Item candidateItem : this.pathItems.list) {
      if (candidateItem.componentUuid.equals(uuid)) {
        return true;
      }
    }
    return false;
  }
  
  public boolean contains(UUID uuid, String distributionID)
  {
    for (Item candidateItem : this.pathItems.list) {
      if ((candidateItem.componentUuid.equals(uuid)) && (candidateItem.distributionID.equals(distributionID))) {
        return true;
      }
    }
    return false;
  }
  
  public boolean contains(Item item)
  {
    for (Item candidate : this.pathItems.list) {
      if (candidate.equals(item)) {
        return true;
      }
    }
    return false;
  }
  
  public boolean startsWith(Path path)
  {
    if (path.size() > size()) {
      return false;
    }
    for (int i = 0; i < path.size(); i++) {
      if (!getPathComponent(i).equals(path.getPathComponent(i))) {
        return false;
      }
    }
    return true;
  }
  
  private int size()
  {
    return this.pathItems.size();
  }
  
  public String toString()
  {
    StringBuilder result = new StringBuilder();
    
    result.append("Path:");
    result.append(this.pathItems.toString());
    result.append("@");
    result.append(this.sourcePosition);
    
    return result.toString();
  }
  
  public boolean equals(Object object)
  {
    if (!(object instanceof Path)) {
      return false;
    }
    Path that = (Path)object;
    if (this.pathItems.size() != that.size()) {
      return false;
    }
    for (int i = 0; i < this.pathItems.size(); i++)
    {
      Item thisItem = getPathComponent(i);
      Item thatItem = that.getPathComponent(i);
      if (!thisItem.equals(thatItem)) {
        return false;
      }
    }
    if (this.sourcePosition.compareTo(that.sourcePosition) != 0) {
      return false;
    }
    return true;
  }
  
  public static class Item
    implements Serializable, KryoSerializable
  {
    private static final long serialVersionUID = 2362934934515089472L;
    private static final String NULL_VAL_STRING = "*";
    private UUID componentUuid;
    private String distributionID;
    
    private Item()
    {
      this(null, null);
    }
    
    public Item(UUID componentUuid, String distributionID)
    {
      this.componentUuid = componentUuid;
      this.distributionID = distributionID;
    }
    
    public UUID getComponentUUID()
    {
      return this.componentUuid;
    }
    
    public String getDistributionID()
    {
      return this.distributionID;
    }
    
    public byte[] toBytes()
    {
      byte[] uidbytes = this.componentUuid.toBytes();
      byte[] keybytes = (this.distributionID != null) && (!this.distributionID.isEmpty()) ? this.distributionID.getBytes() : "*".getBytes();
      ByteBuffer buffer = ByteBuffer.allocate(uidbytes.length + keybytes.length);
      buffer.put(uidbytes);
      buffer.put(keybytes);
      return buffer.array();
    }
    
    public void read(Kryo kryo, Input input)
    {
      boolean uuidNotNull = input.readBoolean();
      this.componentUuid = (uuidNotNull ? (UUID)kryo.readClassAndObject(input) : null);
      
      boolean distIdNotNull = input.readBoolean();
      this.distributionID = (distIdNotNull ? input.readString() : null);
    }
    
    public void write(Kryo kryo, Output output)
    {
      try
      {
        output.writeBoolean(this.componentUuid != null);
        if (this.componentUuid != null) {
          kryo.writeClassAndObject(output, this.componentUuid);
        }
        output.writeBoolean(this.distributionID != null);
        if (this.distributionID != null) {
          output.writeString(this.distributionID);
        }
      }
      catch (Exception e)
      {
        Path.logger.error("Path Item can't write out");
      }
    }
    
    public String toString()
    {
      String s1 = this.componentUuid != null ? this.componentUuid.toString() : "*";
      
      String s2 = (this.distributionID != null) && (!this.distributionID.isEmpty()) ? this.distributionID : "*";
      
      return s1 + "+" + s2;
    }
    
    public static Item fromString(String dataString)
    {
      String[] parts = dataString.split("\\+");
      UUID componentUuid = (parts[0].isEmpty()) || (parts[0].equals("*")) ? null : new UUID(parts[0]);
      String distIdString = (parts[1].isEmpty()) || (parts[1].equals("*")) ? null : parts[1];
      
      Item result = new Item(componentUuid, distIdString);
      return result;
    }
    
    public boolean equals(Object object)
    {
      if (!(object instanceof Item)) {
        return false;
      }
      Item that = (Item)object;
      if ((this.componentUuid != null ? 1 : 0) != (that.componentUuid != null ? 1 : 0)) {
        return false;
      }
      if ((this.componentUuid != null) && (!this.componentUuid.equals(that.componentUuid))) {
        return false;
      }
      if ((this.distributionID != null ? 1 : 0) != (that.distributionID != null ? 1 : 0)) {
        return false;
      }
      if ((this.distributionID != null) && (!this.distributionID.equals(that.distributionID))) {
        return false;
      }
      return true;
    }
  }
  
  public static class ItemList
    implements Serializable
  {
    private static final long serialVersionUID = -7594420778365227945L;
    private final List<Path.Item> list;
    
    public ItemList()
    {
      this.list = new ArrayList();
    }
    
    public ItemList(List<Path.Item> list)
    {
      this.list = new ArrayList(list);
    }
    
    public ItemList copyAugmentedWith(Path.Item item)
    {
      ArrayList<Path.Item> augmentedItems = new ArrayList(this.list.size() + 1);
      augmentedItems.addAll(this.list);
      augmentedItems.add(item);
      ItemList result = new ItemList(augmentedItems);
      return result;
    }
    
    public byte[] getBytes()
    {
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      for (Path.Item u : this.list) {
        try
        {
          os.write(u.toBytes());
        }
        catch (IOException e) {}
      }
      return os.toByteArray();
    }
    
    public int size()
    {
      return this.list.size();
    }
    
    public Path.Item get(int index)
    {
      return (Path.Item)this.list.get(index);
    }
    
    public String toString()
    {
      StringBuilder result = new StringBuilder();
      for (Path.Item u : this.list)
      {
        result.append(u.toString());
        result.append(",");
      }
      result.delete(result.length() - 1, result.length());
      
      return result.toString();
    }
    
    public int hashCode()
    {
      return toString().hashCode();
    }
    
    public static ItemList fromString(String dataValue)
    {
      List<Path.Item> items = new ArrayList();
      String[] parts = dataValue.split(",");
      for (int i = 0; i < parts.length; i++) {
        items.add(Path.Item.fromString(parts[i]));
      }
      ItemList result = new ItemList(items);
      return result;
    }
  }
  
  public int compareTo(Path that)
  {
    if (that == null) {
      return -1;
    }
    return toString().compareTo(that.toString());
  }
  
  public Item getFirstPathItem()
  {
    Item result = getPathComponent(0);
    return result;
  }
  
  public Item getLastPathItem()
  {
    Item result = getPathComponent(size() - 1);
    return result;
  }
  
  public Path segmentEndingWithUuid(UUID uuid)
  {
    List<Item> resultItems = new ArrayList();
    for (Item item : this.pathItems.list)
    {
      resultItems.add(item);
      if (item.componentUuid.equals(uuid))
      {
        ItemList resultItemList = new ItemList(resultItems);
        Path result = new Path(resultItemList, this.sourcePosition);
        return result;
      }
    }
    return null;
  }
}
