package com.bloom.ser;

import com.bloom.event.SimpleEvent;
import com.bloom.jmqmessaging.StreamInfoResponse;
import com.bloom.jmqmessaging.ZMQReceiverInfo;
import com.bloom.messaging.Address;
import com.bloom.messaging.ReceiverInfo;
import com.bloom.recovery.Position;
import com.bloom.uuid.UUID;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.bloom.proc.events.PublishableEvent;
import com.bloom.runtime.DistLink;
import com.bloom.runtime.RecordKey;
import com.bloom.runtime.StreamEvent;
import com.bloom.runtime.containers.DefaultTaskEvent;
import com.bloom.runtime.containers.ITaskEvent;
import java.util.Queue;
import java.util.concurrent.ConcurrentMap;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.ISOChronology;

public class CommonObjectSpace
{
  public static final int COMMON_REGISTRATION_START_INDEX = 50;
  public static final int COMMON_REGISTRATION_MAX_SIZE = 100;
  
  public static void registerClasses(Kryo kryo)
  {
    int index = 50;
    kryo.register(byte[].class, index++);
    kryo.register(char[].class, index++);
    kryo.register(short[].class, index++);
    kryo.register(int[].class, index++);
    kryo.register(long[].class, index++);
    kryo.register(float[].class, index++);
    kryo.register(double[].class, index++);
    kryo.register(boolean[].class, index++);
    kryo.register(String[].class, index++);
    kryo.register(Object[].class, index++);
    kryo.register(ConcurrentMap.class, index++);
    kryo.register(Queue.class, index++);
    kryo.register(UUID.class, index++);
    kryo.register(DistLink.class, index++);
    kryo.register(ITaskEvent.class, index++);
    kryo.register(DefaultTaskEvent.class, index++);
    kryo.register(com.bloom.runtime.containers.WAEvent.class, index++);
    kryo.register(StreamInfoResponse.class, index++);
    kryo.register(ZMQReceiverInfo.class, index++);
    kryo.register(ReceiverInfo.class, index++);
    kryo.register(Address.class, index++);
    kryo.register(PublishableEvent.class, index++);
    kryo.register(StreamEvent.class, index++);
    kryo.register(com.bloom.proc.events.WAEvent.class, index++);
    kryo.register(SimpleEvent.class, index++);
    kryo.register(Position.class, index++);
    kryo.register(RecordKey.class, index++);
    
    kryo.register(ISOChronology.class, new Serializer()
    {
      DateTimeZone defaultZone = DateTimeZone.getDefault();
      
      public ISOChronology read(Kryo kr, Input input, Object type)
      {
        String zoneID = input.readString();
        if (zoneID == null) {
          return ISOChronology.getInstance();
        }
        DateTimeZone zone = DateTimeZone.forID(zoneID);
        return ISOChronology.getInstance(zone);
      }
      public void write(Kryo kr, Output output, ISOChronology object)
      {
        DateTimeZone zone = object.getZone();
        if (zone != this.defaultZone)
        {
          String zoneID = zone.getID();
          output.writeString(zoneID);
        }
        else
        {
          output.writeString(null);
        }
      }
	@Override
	public Object read(Kryo arg0, Input arg1, Class arg2) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void write(Kryo arg0, Output arg1, Object arg2) {
		// TODO Auto-generated method stub
		
	}
    });
    kryo.register(DateTime.class, new Serializer()
    {
      DateTimeZone defaultZone = DateTimeZone.getDefault();
      
      public DateTime read(Kryo kr, Input input, Class type)
      {
        long instant = input.readLong();
        Chronology cr = null;
        String zoneID = input.readString();
        if (zoneID != null)
        {
          DateTimeZone zone = DateTimeZone.forID(zoneID);
          cr = ISOChronology.getInstance(zone);
        }
        return new DateTime(instant, cr);
      }
      
      public void write(Kryo kr, Output output, DateTime object)
      {
        output.writeLong(object.getMillis());
        DateTimeZone zone = object.getZone();
        if (zone != this.defaultZone)
        {
          String zoneID = zone.getID();
          output.writeString(zoneID);
        }
        else
        {
          output.writeString(null);
        }
      }

	@Override
	public void write(Kryo arg0, Output arg1, Object arg2) {
		// TODO Auto-generated method stub
		
	}
    });
  }
}
