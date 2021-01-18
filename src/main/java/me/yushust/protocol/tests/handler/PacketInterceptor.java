package me.yushust.protocol.tests.handler;

import org.bukkit.entity.Player;

/**
 * Represents a packet interceptor, intercepts
 * packets when they are written or read.
 * @param <T> The packet type
 */
public interface PacketInterceptor<T> {

  /**
   * Called when a packet is received, the received packet
   * is passed as parameter, and the returned packet is
   * given to the real channel.
   */
  default T in(Player player, T packet) {
    return packet;
  }

  /**
   * Called when a packet is sent to the player, the
   * original packet is given as parameter, and the
   * intercepted packet is returned by the method.
   */
  default T out(Player player, T packet) {
    return packet;
  }

}
