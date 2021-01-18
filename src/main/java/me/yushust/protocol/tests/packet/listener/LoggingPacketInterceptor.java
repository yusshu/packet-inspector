package me.yushust.protocol.tests.packet.listener;

import me.yushust.protocol.tests.handler.PacketInterceptor;
import me.yushust.protocol.tests.inspect.PacketIntrospector;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;

public class LoggingPacketInterceptor<T>  implements PacketInterceptor<T> {

  private long cooldown;
  private long lastLog;

  private boolean cancelOutgoing;
  private boolean cancelIncoming;

  public LoggingPacketInterceptor(long cooldown) {
    this.cooldown = cooldown;
  }

  public boolean isCancellingOutgoing() {
    return cancelOutgoing;
  }

  public boolean isCancellingIncoming() {
    return cancelIncoming;
  }

  public void setCancelOutgoing(boolean cancelOutgoing) {
    this.cancelOutgoing = cancelOutgoing;
  }

  public void setCancelIncoming(boolean cancelIncoming) {
    this.cancelIncoming = cancelIncoming;
  }

  @Override
  public T in(Player player, T packet) {
    log(packet);
    return cancelIncoming ? null : packet;
  }

  @Override
  public T out(Player player, T packet) {
    log(packet);
    return cancelOutgoing ? null : packet;
  }

  private void log(T packet) {
    long now = System.currentTimeMillis();
    if (lastLog + cooldown < now) {
      PacketIntrospector.inspect((Packet<?>) packet);
      lastLog = now;
    }
  }
}
