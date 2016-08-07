package com.bloom.recovery;

import com.bloom.uuid.UUID;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.log4j.Logger;

public class Position
  implements Serializable, KryoSerializable
{
  private static final long serialVersionUID = 539311015135199997L;
  private static Logger logger = Logger.getLogger(Position.class);
  protected HashMap<Integer, Path> paths = new HashMap();
  
  public Position() {}
  
  public Position(Position that)
  {
    synchronized (that)
    {
      for (Path path : that.values()) {
        this.paths.put(Integer.valueOf(path.getPathHash()), path);
      }
    }
  }
  
  public Position(Set<Path> paths)
  {
    if (paths != null) {
      for (Path path : paths) {
        this.paths.put(Integer.valueOf(path.getPathHash()), path);
      }
    }
  }
  
  public Position(Path path)
  {
    if (path != null) {
      this.paths.put(Integer.valueOf(path.getPathHash()), path);
    }
  }
  
  public Set<Integer> keySet()
  {
    return this.paths.keySet();
  }
  
  public Path get(Integer pathHash)
  {
    return (Path)this.paths.get(pathHash);
  }
  
  public boolean containsKey(int pathHash)
  {
    return this.paths.containsKey(Integer.valueOf(pathHash));
  }
  
  public int size()
  {
    return this.paths.size();
  }
  
  public Collection<Path> values()
  {
    return this.paths.values();
  }
  
  public synchronized void mergeLowerPositions(Position that)
  {
    if ((that == null) || (that.isEmpty())) {
      return;
    }
    synchronized (this.paths)
    {
      for (Integer pathHash : that.keySet())
      {
        Path thatPath = that.get(pathHash);
        Path thisPath = get(pathHash);
        if (thatPath == null) {
          logger.error("Unexpected null path found in merging position: " + (thisPath == null ? pathHash : thisPath.toString()));
        } else if (thatPath.getSourcePosition() != null) {
          if ((!containsKey(pathHash.intValue())) || (thatPath.getSourcePosition().compareTo(thisPath.getSourcePosition()) < 0)) {
            this.paths.put(Integer.valueOf(thatPath.getPathHash()), thatPath);
          }
        }
      }
    }
  }
  
  public synchronized void mergeHigherPositions(Position that)
  {
    if ((that == null) || (that.isEmpty())) {
      return;
    }
    synchronized (this.paths)
    {
      for (Integer pathHash : that.keySet())
      {
        Path thatPath = that.get(pathHash);
        Path thisPath = get(pathHash);
        if (thatPath == null) {
          logger.error("Unexpected null path found in merging position: " + (thisPath == null ? pathHash : thisPath.toString()));
        } else if ((!containsKey(pathHash.intValue())) || (thatPath.getSourcePosition().compareTo(thisPath.getSourcePosition()) > 0)) {
          this.paths.put(Integer.valueOf(thatPath.getPathHash()), thatPath);
        }
      }
    }
  }
  
  public Position createAugmentedPosition(UUID componentUuid, String distributionID)
  {
    Path.Item item = new Path.Item(componentUuid, distributionID);
    
    Set<Path> augmentedPaths = new HashSet();
    synchronized (this.paths)
    {
      for (Path path : this.paths.values())
      {
        Path augmentedPath = path.copyAugmentedWith(item);
        augmentedPaths.add(augmentedPath);
      }
    }
    Position result = new Position(augmentedPaths);
    return result;
  }
  
  public Position createPositionWithoutPath(Path withoutThis)
  {
    Set<Path> resultPaths = new HashSet();
    synchronized (this.paths)
    {
      for (Path path : this.paths.values()) {
        if (!path.equals(withoutThis)) {
          resultPaths.add(path);
        }
      }
    }
    Position result = new Position(resultPaths);
    return result;
  }
  
  public Position createPositionWithoutPaths(Set<Integer> withoutThese)
  {
    Set<Path> resultPaths = new HashSet();
    synchronized (this.paths)
    {
      for (Path path : this.paths.values()) {
        if (!withoutThese.contains(Integer.valueOf(path.getPathHash()))) {
          resultPaths.add(path);
        }
      }
    }
    Position result = new Position(resultPaths);
    return result;
  }
  
  public boolean equals(Object obj)
  {
    if (!(obj instanceof Position)) {
      return false;
    }
    Position that = (Position)obj;
    if (size() != that.size()) {
      return false;
    }
    for (Integer key : that.keySet())
    {
      if (!containsKey(key.intValue())) {
        return false;
      }
      SourcePosition thisSP = get(key).getSourcePosition();
      SourcePosition thatSP = that.get(key).getSourcePosition();
      int comp = thisSP.compareTo(thatSP);
      if (comp != 0) {
        return false;
      }
    }
    return true;
  }
  
  public synchronized void mergeLowerPath(Path p)
  {
    synchronized (this.paths)
    {
      if (containsKey(p.getPathHash()))
      {
        SourcePosition thisSP = get(Integer.valueOf(p.getPathHash())).getSourcePosition();
        if (p.getSourcePosition().compareTo(thisSP) < 0) {
          this.paths.put(Integer.valueOf(p.getPathHash()), p);
        }
      }
      else
      {
        this.paths.put(Integer.valueOf(p.getPathHash()), p);
      }
    }
  }
  
  public synchronized void mergeHigherPath(Path p)
  {
    synchronized (this.paths)
    {
      if (containsKey(p.getPathHash()))
      {
        SourcePosition thisSP = get(Integer.valueOf(p.getPathHash())).getSourcePosition();
        if (p.getSourcePosition().compareTo(thisSP) > 0) {
          this.paths.put(Integer.valueOf(p.getPathHash()), p);
        }
      }
      else
      {
        this.paths.put(Integer.valueOf(p.getPathHash()), p);
      }
    }
  }
  
  public SourcePosition getLowPositionForPathSegment(Path startingSubpath)
  {
    SourcePosition result = null;
    synchronized (this.paths)
    {
      for (Path p : this.paths.values()) {
        if ((p.startsWith(startingSubpath)) && (
          (result == null) || (p.getSourcePosition().compareTo(result) < 0))) {
          result = p.getSourcePosition();
        }
      }
    }
    return result;
  }
  
  public boolean isEmpty()
  {
    return this.paths.isEmpty();
  }
  
  public void removePathsWhichStartWith(Collection<Path> removalBasePaths)
  {
    Iterator<Map.Entry<Integer, Path>> thisPathsIter;
    Path candidateForRemoval;
    synchronized (this.paths)
    {
      for (thisPathsIter = this.paths.entrySet().iterator(); thisPathsIter.hasNext();)
      {
        Map.Entry<Integer, Path> thisEntry = (Map.Entry)thisPathsIter.next();
        candidateForRemoval = (Path)thisEntry.getValue();
        for (Path removalBasePath : removalBasePaths) {
          if (candidateForRemoval.startsWith(removalBasePath))
          {
            thisPathsIter.remove();
            break;
          }
        }
      }
    }
  }
  
  public boolean containsComponent(UUID uuid)
  {
    synchronized (this.paths)
    {
      for (Path path : values()) {
        if (path.contains(uuid)) {
          return true;
        }
      }
    }
    return false;
  }
  
  public Position getLowPositionForComponent(UUID uuid)
  {
    Position result = new Position();
    synchronized (this.paths)
    {
      for (Path p : this.paths.values())
      {
        Path pathToUuid = p.segmentEndingWithUuid(uuid);
        if ((pathToUuid != null) && (pathToUuid.getSourcePosition() != null)) {
          result.mergeLowerPath(pathToUuid);
        }
      }
    }
    return result;
  }
  
  public SourcePosition getLowSourcePositionForComponent(UUID uuid)
  {
    SourcePosition result = null;
    synchronized (this.paths)
    {
      for (Path p : this.paths.values()) {
        if (p.contains(uuid))
        {
          SourcePosition sp = p.getSourcePosition();
          if ((sp != null) && ((result == null) || (sp.compareTo(result) < 0))) {
            result = sp;
          }
        }
      }
    }
    return result;
  }
  
  public PartitionedSourcePosition getPartitionedSourcePositionForComponent(UUID uuid)
  {
    Map<String, SourcePosition> resultMap = new HashMap();
    synchronized (this.paths)
    {
      for (Path p : this.paths.values()) {
        if (p.contains(uuid))
        {
          String partID = p.getPathComponent(0).getDistributionID();
          SourcePosition sp = p.getSourcePosition();
          if (sp == null) {
            resultMap.put("*", sp);
          } else if ((!resultMap.containsKey(partID)) || (sp.compareTo((SourcePosition)resultMap.get(partID)) < 0)) {
            resultMap.put(partID, sp);
          }
        }
      }
    }
    PartitionedSourcePosition result = new PartitionedSourcePosition(resultMap);
    return result;
  }
  
  public static Position from(UUID sourceUUID, String distributionID, SourcePosition sourcePosition)
  {
    Set<Path> paths = new HashSet();
    paths.add(new Path(sourceUUID, distributionID, sourcePosition));
    Position result = new Position(paths);
    return result;
  }
  
  public boolean precedes(Position that)
  {
    for (Path thatPath : that.values())
    {
      SourcePosition thatSP = thatPath.getSourcePosition();
      SourcePosition thisSP = getLowPositionForPathSegment(thatPath);
      if ((thisSP != null) && 
        (thisSP.compareTo(thatSP) < 0)) {
        return true;
      }
    }
    return false;
  }
  
  public boolean exceeds(Position that)
  {
    if ((that == null) || (that.isEmpty())) {
      return true;
    }
    for (Path thatPath : that.values())
    {
      SourcePosition thatSP = thatPath.getSourcePosition();
      SourcePosition thisSP = getLowPositionForPathSegment(thatPath);
      if ((thisSP != null) && 
        (thisSP.compareTo(thatSP) <= 0)) {
        return false;
      }
    }
    return true;
  }
  
  public int removeEqualPaths(Position that)
  {
    Set<Integer> removeThese = new HashSet();
    for (Path thatPath : that.values())
    {
      Path thisPath = get(Integer.valueOf(thatPath.getPathHash()));
      if ((thisPath != null) && (thisPath.getSourcePosition().equals(thatPath.getSourcePosition()))) {
        removeThese.add(Integer.valueOf(thatPath.getPathHash()));
      }
    }
    for (Integer removeThis : removeThese) {
      this.paths.remove(removeThis);
    }
    return removeThese.size();
  }
  
  public void read(Kryo kryo, Input input)
  {
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
      if (this.paths == null) {
        this.paths = new HashMap();
      }
      this.paths.put(Integer.valueOf(path.getPathHash()), path);
    }
  }
  
  public void write(Kryo kryo, Output output)
  {
    output.writeInt(size());
    for (Path p : values())
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
  
  public String toString()
  {
    StringBuilder result = new StringBuilder();
    
    result.append("[");
    for (Path p : this.paths.values())
    {
      result.append(p.toString());
      result.append(";");
    }
    result.append("]");
    
    return result.toString();
  }
}
