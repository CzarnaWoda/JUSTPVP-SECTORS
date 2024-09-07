package pl.blackwater.core.data;

import org.bukkit.entity.Player;

import lombok.Data;
@Data
public class Combat {
    private Player player;
    private long lastAttactTime;
    private Player lastAttactkPlayer;
    
    public Combat(Player player) {
    	this.player = player;
    	lastAttactTime = 0L;
    	lastAttactkPlayer = null;
    }
    
    
    public boolean hasFight() {
        return this.getLastAttactTime() > System.currentTimeMillis();
    }
    
    public boolean wasFight() {
        return this.getLastAttactkPlayer() != null;
    }
}
