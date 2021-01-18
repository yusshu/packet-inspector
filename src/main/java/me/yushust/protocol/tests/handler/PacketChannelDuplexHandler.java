package me.yushust.protocol.tests.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

/**
 * Channel duplex handler that calls another handlers called
 * interceptors depending on the packet type.
 *
 * <p>The channel handler also behaves like a interceptor
 * registry. Containing a relation of packetType->interceptor
 * to call the required interceptor.</p>
 */
public final class PacketChannelDuplexHandler extends ChannelDuplexHandler {

  public static final Map<Class<?>, PacketInterceptor<?>> INTERCEPTORS =
      new HashMap<>();

  private final Player player;

  /** Constructs a new channel handler for the specified player */
  public PacketChannelDuplexHandler(Player player) {
    this.player = Objects.requireNonNull(player, "player");
  }

  /**
   * {@inheritDoc} The functionality is delegated to the generic method
   * {@link PacketChannelDuplexHandler#handleRead} it's generic to avoid
   * some unnecessary raw-types
   */
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object packet) throws Exception {
    handleRead(ctx, packet);
  }

  /**
   * Using the packet type, calls the required interceptor
   * present in the {@code interceptors} map. Calls
   * {@link PacketInterceptor#in}
   */
  private <T> void handleRead(ChannelHandlerContext ctx, T packet) throws Exception {
    @SuppressWarnings("unchecked")
    PacketInterceptor<T> interceptor =
        (PacketInterceptor<T>) INTERCEPTORS.get(packet.getClass());
    if (interceptor == null) {
      super.channelRead(ctx, packet);
    } else {
      try {
        packet = interceptor.in(player, packet);
      } catch (Throwable error) {
        Bukkit.getLogger().log(Level.SEVERE, "[MAKitPvP] [Packet Interceptor] Error while" +
            " intercepting an entrant packet.", error);
        return;
      }
      if (packet != null) {
        super.channelRead(ctx, packet);
      }
    }
  }

  /**
   * {@inheritDoc} The functionality is delegated to the generic method
   * {@link PacketChannelDuplexHandler#handleWrite} it's generic to avoid
   * some unnecessary raw-types
   */
  @Override
  public void write(ChannelHandlerContext ctx, Object packet, ChannelPromise promise) throws Exception {
    handleWrite(ctx, packet, promise);
  }

  /**
   * Using the packet type, calls the required interceptor
   * present in the {@code interceptors} map. Calls
   * {@link PacketInterceptor#out}
   */
  private <T> void handleWrite(ChannelHandlerContext ctx, T packet, ChannelPromise promise) throws Exception {
    @SuppressWarnings("unchecked")
    PacketInterceptor<T> interceptor =
        (PacketInterceptor<T>) INTERCEPTORS.get(packet.getClass());
    if (interceptor == null) {
      super.write(ctx, packet, promise);
    } else {
      try {
        packet = interceptor.out(player, packet);
      } catch (Throwable error) {
        Bukkit.getLogger().log(Level.SEVERE, "[MAKitPvP] [Packet Interceptor] Error while" +
            " intercepting a -> packet.", error);
        return;
      }
      if (packet != null) {
        super.write(ctx, packet, promise);
      }
    }
  }

}
