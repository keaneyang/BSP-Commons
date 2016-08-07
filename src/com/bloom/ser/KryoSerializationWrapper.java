 package com.bloom.ser;
 
 import java.io.IOException;
 import java.io.ObjectInputStream;
 import java.io.ObjectOutputStream;
 import java.io.ObjectStreamException;
 import java.io.Serializable;
 
 public class KryoSerializationWrapper
   implements Serializable
 {
   private static final long serialVersionUID = -1585682752066344644L;
   private byte[] s;
   
   public KryoSerializationWrapper(Object target)
   {
     this.s = KryoSerializer.write(target);
   }
   
   public Object readResolve() throws ObjectStreamException {
     return KryoSerializer.read(this.s);
   }
   
   private void writeObject(ObjectOutputStream out) throws IOException {
     out.writeInt(this.s.length);
     out.write(this.s);
   }
   
   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException { int len = in.readInt();
     this.s = new byte[len];
     in.readFully(this.s);
   }
 }

