package me.yushust.protocol.tests;

import me.yushust.protocol.tests.handler.ChannelHandlerInjector;
import me.yushust.protocol.tests.handler.ChannelHandlerInjector_v1_8_R3;
import me.yushust.protocol.tests.handler.PacketChannelDuplexHandler;
import me.yushust.protocol.tests.packet.listener.LoggingPacketInterceptor;
import net.minecraft.server.v1_8_R3.PacketLoginInStart;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import net.minecraft.server.v1_8_R3.PacketPlayOutLogin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ProtocolTestsPlugin extends JavaPlugin implements Listener {

  private final ChannelHandlerInjector injector
      = new ChannelHandlerInjector_v1_8_R3();

  @Override
  public void onEnable() {
    Bukkit.getPluginManager().registerEvents(this, this);
  }

  @EventHandler
  public void onChat(AsyncPlayerChatEvent event) {
    Player player = event.getPlayer();
    String message = event.getMessage();
    if (message.startsWith("toggle-inspect ")) {
      String clazz = message.substring("toggle-inspect ".length());
      clazz = "net.minecraft.server.v1_8_R3." + clazz;
      Class<?> klass;

      try {
        klass = Class.forName(clazz);
      } catch (ClassNotFoundException e) {
        player.sendMessage(ChatColor.RED + "JUEPUTAPUTA no existe esa clase");
        event.setCancelled(true);
        return;
      }

      event.setCancelled(true);
      player.sendMessage(ChatColor.GREEN + "toggleado :Si: rejoinea joto");

      if (PacketChannelDuplexHandler.INTERCEPTORS.containsKey(klass)) {
        PacketChannelDuplexHandler.INTERCEPTORS.remove(klass);
      } else {
        PacketChannelDuplexHandler.INTERCEPTORS.put(klass, new LoggingPacketInterceptor<>(10));
      }
    }
  }

  @EventHandler
  public void onJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    injector.injectHandler(player);
  }

}
