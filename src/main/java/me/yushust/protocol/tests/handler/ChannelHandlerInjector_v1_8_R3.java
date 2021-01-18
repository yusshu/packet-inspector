package me.yushust.protocol.tests.handler;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ChannelHandlerInjector_v1_8_R3
    implements ChannelHandlerInjector {

  @Override
  public void injectHandler(Player player) {
    ((CraftPlayer) player)
        .getHandle()
        .playerConnection
        .networkManager
        .channel
        .pipeline()
        .addBefore(
            "packet_handler",
            CHANNEL_NAME,
            new PacketChannelDuplexHandler(player)
        );
  }
}
