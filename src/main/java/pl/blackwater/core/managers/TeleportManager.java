package pl.blackwater.core.managers;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Teleportable;
import pl.blackwaterapi.teleport.LocationTeleport;
import pl.blackwaterapi.teleport.PlayerTeleport;
import pl.blackwaterapi.teleport.TeleportRequest;
import pl.blackwaterapi.timer.TeleportCallback;
import pl.blackwaterapi.timer.TimerCallback;
import pl.blackwaterapi.timer.TimerManager;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.impl.teleport.SpawnTeleport;
import pl.justsectors.sectors.Sector;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class TeleportManager implements Listener {

    private static final Map<UUID, TimerTask> tasks = new HashMap<>();
    private static final Cache<UUID, Teleportable> teleportableCache = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.SECONDS).build();;
    private static final Cache<UUID, UUID> tpaRequestCache = CacheBuilder.newBuilder().expireAfterWrite(15, TimeUnit.SECONDS).build();
    private static final Cache<UUID, Long> joinDelay = CacheBuilder.newBuilder().expireAfterWrite(2, TimeUnit.SECONDS).build();


    public static void teleportPlayer(User teleportUser, User targetUser) {
        final Player teleportPlayer = teleportUser.asPlayer();
        if (teleportPlayer == null || !teleportPlayer.isOnline()) {
            return;
        }
        final TeleportRequest teleportRequest = new PlayerTeleport(teleportUser, targetUser);
        if (teleportUser.getUserRank().getPermissions().contains("core.teleport.bypass")) {
            teleportRequest.reuqestAccepted();
            return;
        }
        Util.sendMsg(teleportPlayer, "&8» &eTeleportacja nastapi za 5 sekund!");
        MinecraftServer.getServer().postToMainThread(() -> {
            teleportPlayer.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 5 * 20, 0), true);
            teleportPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 5 * 20, 0), true);
            runNow(teleportUser, new TeleportCallback<Teleportable>() {
                @Override
                public void success() {
                    teleportPlayer.removePotionEffect(PotionEffectType.CONFUSION);
                    teleportPlayer.removePotionEffect(PotionEffectType.BLINDNESS);
                    teleportRequest.reuqestAccepted();
                    tpaRequestCache.invalidate(targetUser.getUuid());
                }

                @Override
                public void error() {
                    teleportPlayer.removePotionEffect(PotionEffectType.CONFUSION);
                    teleportPlayer.removePotionEffect(PotionEffectType.BLINDNESS);
                    Util.sendMsg(teleportPlayer, " &8» &7Teleportacja zostala anulowana. &8(&7Powod: &cRuszyles sie!&8)");
                }
            });
        });
    }


    public static void teleportToSpawn(final User user, final Player player, final Sector sector, final boolean instant) {
        final TeleportRequest teleportRequest = new SpawnTeleport(user, sector);
        if (instant || player.hasPermission("api.timer.bypass")) {
            teleportRequest.reuqestAccepted();
            return;
        }
        Util.sendMsg(player, "&8» &eTeleportacja nastapi za 5 sekund!");
        player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 5 * 20, 0), true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 5 * 20, 0), true);
        runNow(user, new TeleportCallback<Teleportable>() {
            @Override
            public void success() {
                player.removePotionEffect(PotionEffectType.CONFUSION);
                player.removePotionEffect(PotionEffectType.BLINDNESS);
                teleportRequest.reuqestAccepted();
            }

            @Override
            public void error() {
                player.removePotionEffect(PotionEffectType.CONFUSION);
                player.removePotionEffect(PotionEffectType.BLINDNESS);
                Util.sendMsg(player, " &8» &7Teleportacja zostala anulowana. &8(&7Powod: &cRuszyles sie!&8)");
            }
        });
    }



    public static void teleportPlayer(User teleportUser, Location location, Sector sector) {
        final Player teleportPlayer = teleportUser.asPlayer();
        if (teleportPlayer == null || !teleportPlayer.isOnline()) {
            return;
        }
        final TeleportRequest teleportRequest = new LocationTeleport(teleportUser, sector, location);
        if (teleportUser.getUserRank().getPermissions().contains("core.teleport.bypass")) {
            teleportRequest.reuqestAccepted();
            return;
        }
        Util.sendMsg(teleportPlayer, "&8» &eTeleportacja nastapi za 5 sekund!");
        teleportPlayer.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 5 * 20, 0), true);
        teleportPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 5 * 20, 0), true);
        runNow(teleportUser, new TeleportCallback<Teleportable>() {
            @Override
            public void success() {
                teleportPlayer.removePotionEffect(PotionEffectType.CONFUSION);
                teleportPlayer.removePotionEffect(PotionEffectType.BLINDNESS);
                teleportRequest.reuqestAccepted();
            }

            @Override
            public void error() {
                teleportPlayer.removePotionEffect(PotionEffectType.CONFUSION);
                teleportPlayer.removePotionEffect(PotionEffectType.BLINDNESS);
                Util.sendMsg(teleportPlayer, " &8» &7Teleportacja zostala anulowana. &8(&7Powod: &cRuszyles sie!&8)");
            }
        });
    }


    public static void deleteRequestIfExists(UUID uuid) {
        TimerTask t = tasks.get(uuid);
        if (t != null) {
            cancel(t, uuid);
        }
    }

    private static void cancel(TimerTask task, UUID uuid) {
        task.cancel();
        tasks.remove(uuid);
    }

    public static boolean hasTeleportRequest(UUID uuid) {
        return tasks.get(uuid) != null;
    }

    public static Teleportable getRequest(UUID uuid) {
        return teleportableCache.asMap().get(uuid);//TODO asMap
    }


    public static boolean hasJoinDelayRequest(UUID uuid) {
        return joinDelay.getIfPresent(uuid) != null;
    }


    public static void registerJoinDelay(UUID uuid) {
        joinDelay.put(uuid, System.currentTimeMillis());
    }
    public static void registerRequest(UUID requestedPlayer, Teleportable teleportable) {
        teleportableCache.put(requestedPlayer, teleportable);
    }


    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event) {
        if (event.getFrom().getBlockX() == event.getTo().getBlockX() && event.getFrom().getBlockY() == event.getTo().getBlockY() && event.getFrom().getBlockZ() == event.getTo().getBlockZ()) {
            return;
        }
        TimerTask t = tasks.get(event.getPlayer().getUniqueId());
        if (t != null) {
            cancel(t, event.getPlayer().getUniqueId());
            return;
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player)event.getEntity();
            TimerTask t = tasks.get(player.getUniqueId());
            if (t != null) {
                cancel(t, player.getUniqueId());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        TimerTask t = tasks.get(player.getUniqueId());
        if (t != null) {
            cancel(t, player.getUniqueId());
        }
    }

    private static void runNow(User user, TeleportCallback<Teleportable> call) {
        TimerTask timerTask = tasks.get(user.getUuid());
        if (timerTask != null) {
            timerTask.cancel();
            return;
        }
        timerTask = new TimerTask(user.getUuid(), call);
        tasks.put(user.getUuid(), timerTask);
        timerTask.runTaskLater(Core.getPlugin(), 100);
    }

    public static class TimerTask
            extends BukkitRunnable {
        private final UUID player;
        private final TeleportCallback<Teleportable> call;

        public void run() {
            this.call.success();
            tasks.remove(this.player);
        }

        public void cancel() {
            super.cancel();
            this.call.error();
        }

        public TimerTask(UUID player, TeleportCallback<Teleportable> call) {
            this.player = player;
            this.call = call;
        }

        public UUID getPlayer() {
            return this.player;
        }
    }
}
