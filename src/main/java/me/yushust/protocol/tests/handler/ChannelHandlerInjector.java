package me.yushust.protocol.tests.handler;

import org.bukkit.entity.Player;

/**
 * Injects the channel handler to the
 * player connection. The channel handler
 * name should be always {@link ChannelHandlerInjector#CHANNEL_NAME}
 */
public interface ChannelHandlerInjector {

  String CHANNEL_NAME = "ptpi";

  void injectHandler(Player player);

}
