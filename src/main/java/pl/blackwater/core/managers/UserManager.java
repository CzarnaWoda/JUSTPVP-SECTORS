package pl.blackwater.core.managers;


import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.User;
import pl.blackwater.core.settings.CoreConfig;
import pl.blackwater.guilds.ranking.RankingManager;
import pl.blackwater.guilds.scoreboard.ScoreBoardNameTag;
import pl.blackwaterapi.utils.GsonUtil;
import pl.blackwaterapi.utils.Logger;
import pl.justsectors.nbt.NBTManager;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.impl.user.JoinSectorPacket;
import pl.justsectors.packets.impl.user.LeftSectorPacket;
import pl.justsectors.packets.impl.user.UserChangeSectorPacket;
import pl.justsectors.packets.impl.user.UserRegisterPacket;
import pl.justsectors.redis.channels.RedisChannel;
import pl.justsectors.redis.client.RedisClient;
import pl.justsectors.sectors.SectorManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

public  class UserManager
{
    public static final HashMap<UUID, User> users = new HashMap<>();
    public static final HashMap<String, User> userByName = new HashMap<>();

    private static final ExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public static User getUser(final Player player) {
        return users.get(player.getUniqueId());
    }
    
    public static User getUser(final UUID uuid) {
        return users.get(uuid);
    }
    
    public static User getUser(final String name) {
        return userByName.get(name.toLowerCase());
    }

    public static User registerUser(final User u) {
        users.put(u.getUuid(), u);
        userByName.put(u.getLastName().toLowerCase(), u);
        return u;
    }

    public static User createUser(final Player p) {
        final User u = new User(p);
        users.put(p.getUniqueId(), u);
        userByName.put(u.getLastName().toLowerCase(), u);
        return u;
    }

    public static User createUser(final String userName, final UUID uuid, final String address, final GameMode gameMode, final boolean allowFlight,
                                   final Location lastLocation, final Location homeLocation) {
        final User u = new User(userName, uuid, address, gameMode, allowFlight, lastLocation, homeLocation);
        RankingManager.addRanking(u);
        users.put(u.getUuid(), u);
        userByName.put(u.getLastName().toLowerCase(), u);
        return u;
    }

    
    public static void joinToGame(final Player p)
    {
      User u = getUser(p);
      if (u == null)
      {
        u = createUser(p);
        RedisClient.sendSectorsPacket(new UserRegisterPacket(u.getLastName(), u.getUuid(), u.getFirstIP(), u.getGameMode(), u.isFly(), u.getLastLocation(), u.getHomeLocation()));
      }
      else
      {
        u.setLastName(p.getName());
        u.setLastJoin(System.currentTimeMillis());
        u.setLastIP(p.getAddress().getHostString());
        p.setGameMode(u.getGameMode());
        p.setAllowFlight(u.isFly());
        if(p.getGameMode().equals(GameMode.ADVENTURE)){
            p.setGameMode(GameMode.SURVIVAL);
        }
      }
        //REDIS NEW SYSTEM <- SEARCH
        //final JoinSectorPacket packet = new JoinSectorPacket(u.getLastName(), CoreConfig.CURRENT_SECTOR_NAME);
        //RedisClient.sendSectorsPacket(packet);
        final String userName = u.getLastName();
        SectorManager.getCurrentSector().ifPresent(sector -> {
            sector.getRedisOnlinePlayers().addAsync(userName);
        });
        Core.getRankManager().joinPlayer(p);
        Core.getRankManager().implementPermissions(u);
    }
    
    public static void leaveFromGame(final Player p, boolean sendToOtherSectors) {
         final User u = getUser(p);
         if (u == null) {
            Logger.severe("Dane uzytkownika '" + p.getName() + "' przepadly!");
            return;
        }
         u.setFly(p.getAllowFlight());
         u.setGameMode(p.getGameMode());
         u.setLastName(p.getName());
         u.setLastLocation(p.getLocation());
         u.setGod(p.hasPermission("core.god"));
        //REDIS NEW SYSTEM <- SEARCH
         //final LeftSectorPacket packet = new LeftSectorPacket(u.getLastName(), CoreConfig.CURRENT_SECTOR_NAME);
         //RedisClient.sendSectorsPacket(packet);
         final NBTTagCompound nbtTagCompound = NBTManager.createNBTBasedOnUser(u);
         if(nbtTagCompound == null)
         {
            p.kickPlayer("Blad podczas zapisu Twojego ekwipunku!");
            return;
         }
         u.setNbtString(nbtTagCompound.toString());
         u.setLastUpdate(System.currentTimeMillis());
         RedisChannel.INSTANCE.USERS.put(u.getUuid(), GsonUtil.toJson(u));
         if(sendToOtherSectors) {
             final RedisPacket userPacket = new UserChangeSectorPacket(u);
             RedisClient.sendUser(userPacket);
         }
         Core.getRankManager().unimplementPermissions(u);
    }
    
    public static void setup() {
        final AtomicInteger ai = new AtomicInteger(0);
        MinecraftServer.getServer().postToMainThread(() -> {
            RedisChannel.INSTANCE.USERS.forEach((uuid, s) -> {
                final User user = GsonUtil.fromJson(s, User.class);
                users.put(user.getUuid(), user);
                userByName.put(user.getLastName().toLowerCase(), user);
                RankingManager.addRanking(user);
                ai.getAndIncrement();
            });
        });
        Core.getPlugin().getLogger().log(Level.INFO, "Zaladowano " + users.size() + " userow z bazy!");
    }
    
    public static HashMap<UUID, User> getUsers() {
        return UserManager.users;
    }

    

    //public static int getPosition(User user) {
      //  for (RankList.Data<User> userData : TabThread.getInstance().getRankList().getTopPlayers())
     //       if (userData.getKey().equals(user))
     //           return TabThread.getInstance().getRankList().getTopPlayers().indexOf(userData)+1;
      //  return -1;
   // }
}
