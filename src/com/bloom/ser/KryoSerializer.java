 package com.bloom.ser;
 
 import com.esotericsoftware.kryo.Kryo;
 import com.esotericsoftware.kryo.io.Input;
 import com.esotericsoftware.kryo.io.Output;
 import org.apache.log4j.Logger;
 
 public final class KryoSerializer
 {
   private static Logger logger = Logger.getLogger(KryoSerializer.class);
   
   public static ClassLoader loader = null;
   
   public static void setClassLoader(ClassLoader aLoader) {
     loader = aLoader;
   }
   
   private static final transient ThreadLocal<Kryo> threadSerializers = new ThreadLocal()
   {
     protected Kryo initialValue() {
       Kryo kryo = new Kryo();
       
       if (KryoSerializer.loader != null)
         kryo.setClassLoader(KryoSerializer.loader);
       return kryo;
     }
   };
   
   public static Kryo getSerializer()
   {
     return (Kryo)threadSerializers.get();
   }
   
   static {
     getSerializer().setRegistrationRequired(false);
   }
   
   public static void register(Class<?>... classes) {
     for (Class<?> clazz : classes) {
       getSerializer().register(clazz);
     }
   }
   
   public static byte[] write(Object obj) {
     try {
       Output out = new Output(8192, 1048576);
       getSerializer().writeClassAndObject(out, obj);
       return out.toBytes();
     } catch (Throwable t) {
       logger.error("Thread: " + Thread.currentThread().getName() + " Exception: " + t);
       throw t;
     }
   }
   
   public static Object read(byte[] bytes) {
     try {
       Input in = new Input(bytes);
       
       return getSerializer().readClassAndObject(in);
     } catch (Throwable t) {
       logger.error("Thread: " + Thread.currentThread().getName() + " Exception: " + t);
       logger.error(t);
       throw t;
     }
   }
   
   public static Object read(byte[] bytes, int offset, int len) {
     try { byte[] b = new byte[len];
       System.arraycopy(bytes, offset, b, 0, len);
       Input in = new Input(b);
       
       return getSerializer().readClassAndObject(in);
     } catch (Throwable t) {
       logger.error("Thread: " + Thread.currentThread().getName() + " Exception: " + t);
       logger.error(t);
       throw t;
     }
   }
 }

