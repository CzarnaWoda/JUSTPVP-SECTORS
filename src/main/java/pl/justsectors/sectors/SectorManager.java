package pl.justsectors.sectors;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.User;
import pl.blackwater.core.settings.CoreConfig;
import pl.justsectors.nbt.NBTManager;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.impl.user.UserChangeSectorPacket;
import pl.justsectors.redis.client.RedisClient;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

public class SectorManager {

    private final static Map<String, Sector> sectors = new LinkedHashMap<>();

    public static void registerSector(final Sector sector)
    {
        Core.getPlugin().getLogger().log(Level.INFO, "Registering sector " + sector.getSectorName() + " and type " + sector.getSectorType().name());
        sectors.put(sector.getSectorName(), sector);
    }


    public static void sendToSector(User user, Sector sector) {
        sendToSector(user, sector.getSectorName());
    }

    public static void sendToSector(User user, String sectorName) {
        final NBTTagCompound nbtTagCompound = NBTManager.createNBTBasedOnUser(user);
        if(nbtTagCompound == null)
        {
            user.asPlayer().kickPlayer("Blad podczas przenoszenia Cie na inny sektor. Zglos to administrajci!");
            return;
        }
        user.setNbtString(nbtTagCompound.toString());
        //TODO user.update ale async
        //user.setSectorTo(sectorName);
        //this.sectorPlugin.getSectorClient().getChannel().writeAndFlush(user.asBuffer());//TODO moze do jednego sektora tylko?
        final RedisPacket packet = new UserChangeSectorPacket(user);
        RedisClient.sendUser(packet, sectorName);
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final DataOutputStream dos = new DataOutputStream(bos);
        try {
            dos.writeUTF("Connect");
            dos.writeUTF(sectorName);
            user.asPlayer().sendPluginMessage(Core.getPlugin(), "BungeeCord", bos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Optional<Sector> getCurrentSector()
    {
        return Optional.of(sectors.get(CoreConfig.CURRENT_SECTOR_NAME));//todo do zmiennej przypisac
    }

    public static Sector getSector(final String sectorName)
    {
        return sectors.get(sectorName);
    }

    public static Map<String, Sector> getSectors() {
        return sectors;
    }

}
