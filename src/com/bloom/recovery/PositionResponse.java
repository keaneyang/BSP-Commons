package com.bloom.recovery;

import com.bloom.uuid.UUID;

import java.io.Serializable;

public class PositionResponse
  implements Serializable
{
  private static final long serialVersionUID = -6801388395840353959L;
  public final UUID fromID;
  public final UUID fromServer;
  public final UUID toUUID;
  public final String toDistID;
  public final Position position;
  
  private PositionResponse()
  {
    this.fromID = null;
    this.fromServer = null;
    this.toUUID = null;
    this.toDistID = null;
    this.position = null;
  }
  
  public PositionResponse(UUID fromID, UUID fromServer, UUID toID, Position position)
  {
    this.fromID = fromID;
    this.fromServer = fromServer;
    this.toUUID = toID;
    this.toDistID = null;
    this.position = position;
  }
  
  public PositionResponse(UUID fromID, UUID fromServer, UUID toID, String toDistID, Position position)
  {
    this.fromID = fromID;
    this.fromServer = fromServer;
    this.toUUID = toID;
    this.toDistID = toDistID;
    this.position = position;
  }
}
