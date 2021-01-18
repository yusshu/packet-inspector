package me.yushust.protocol.tests.inspect;

import io.netty.buffer.ByteBuf;
import net.minecraft.server.v1_8_R3.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class PacketIntrospector {

  public static void inspect(Packet<?> packet, String... orderedFields) {

    StringBuilder log = new StringBuilder("Introspection of packet '");
    log.append(packet.getClass().getName());
    log.append("'");

    try {
      packet.b(new PacketDataSerializer(null) {

        private int cursor = 0;

        @Override
        public void a(byte[] bytes) {
          appendNextTag();
          log.append("wrote ");
          log.append(bytes.length);
          log.append("bytes. As string: '");
          log.append(new String(bytes, StandardCharsets.UTF_8));
          log.append("'");
        }

        @Override
        public void a(BlockPosition blockposition) {
          appendNextTag();
          log.append("position. ");
          log.append("x: ");
          log.append(blockposition.getX());
          log.append(" y: ");
          log.append(blockposition.getY());
          log.append(" z: ");
          log.append(blockposition.getZ());
        }

        @Override
        public void a(IChatBaseComponent component) {
          appendNextTag();
          log.append("component. ");
          log.append(component.getText());
        }

        @Override
        public void a(Enum<?> oenum) {
          appendNextTag();
          log.append("enum. ");
          log.append(oenum.name());
        }

        @Override
        public void a(UUID uuid) {
          appendNextTag();
          log.append("uuid. ");
          log.append(uuid);
        }

        @Override
        public void b(int i) {
          appendNextTag();
          log.append("int. ");
          log.append(i);
        }

        @Override
        public void b(long i) {
          appendNextTag();
          log.append("long. ");
          log.append(i);
        }

        @Override
        public void a(NBTTagCompound tag) {
          appendNextTag();
          log.append("nbt tag. ");
          log.append(tag);
        }

        @Override
        public void a(ItemStack item) {
          appendNextTag();
          log.append("item. ");
          log.append(item);
        }

        @Override
        public PacketDataSerializer a(String s) {
          appendNextTag();
          log.append("string. ");
          log.append(s);
          return this;
        }

        @Override
        public ByteBuf writeDouble(double d0) {
          appendNextTag();
          log.append("double. ");
          log.append(d0);
          return null;
        }

        @Override
        public ByteBuf writeBoolean(boolean flag) {
          appendNextTag();
          log.append("bool. ");
          log.append(flag);
          return null;
        }

        @Override
        public ByteBuf writeInt(int i) {
          appendNextTag();
          log.append("int. ");
          log.append(i);
          return null;
        }

        @Override
        public ByteBuf writeByte(int i) {
          appendNextTag();
          log.append("byte. ");
          log.append(i);
          return null;
        }

        @Override
        public ByteBuf writeChar(int i) {
          appendNextTag();
          log.append("char. ");
          log.append((char) i);
          return null;
        }

        @Override
        public ByteBuf writeFloat(float f) {
          appendNextTag();
          log.append("float. ");
          log.append(f);
          return null;
        }

        @Override
        public ByteBuf writeLong(long i) {
          appendNextTag();
          log.append("long. ");
          log.append(i);
          return null;
        }

        @Override
        public ByteBuf writeMedium(int i) {
          appendNextTag();
          log.append("medium. ");
          log.append(i);
          return null;
        }

        @Override
        public ByteBuf writeBytes(byte[] abyte) {
          a(abyte);
          return null;
        }

        private void appendNextTag() {
          log.append('\n');
          log.append(nextTag());
          log.append(": ");
        }

        private String nextTag() {
          if (orderedFields.length <= cursor) {
            return "unknown N" + (++cursor);
          } else {
            return orderedFields[cursor++];
          }
        }

      });
    } catch (IOException e) {
      System.out.println("fail!!!!!!!");
      System.out.println(log.toString());
      e.printStackTrace();
      return;
    }

    System.out.println(log.toString());
  }

}
