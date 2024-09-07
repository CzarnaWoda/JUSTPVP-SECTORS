package pl.justsectors.packets.impl.user;

import org.bukkit.GameMode;
import org.bukkit.Location;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

import java.util.UUID;

public class UserRegisterPacket extends RedisPacket {

    private String userName;
    private UUID uuid;
    private String address;
    private GameMode gameMode;
    private boolean allowFlight;


    public UserRegisterPacket() {
    }

    public UserRegisterPacket(String userName, UUID uuid, String address, GameMode gameMode, boolean allowFlight, Location lastLocation, Location homeLocation) {
        this.userName = userName;
        this.uuid = uuid;
        this.address = address;
        this.gameMode = gameMode;
        this.allowFlight = allowFlight;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getUserName() {
        return userName;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getAddress() {
        return address;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public boolean isAllowFlight() {
        return allowFlight;
    }


}
