package pl.blackwater.storage.managers;

import java.util.HashMap;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import lombok.Getter;
import pl.blackwater.storage.data.Creator;

public class CreatorManager {
	
	@Getter private static HashMap<Player, Creator> creators = new HashMap<>();
	
	public static void addCreator(Player creator, Sign sign, long rentTime, int cost, ProtectedRegion storageRegion, boolean leftClick, boolean rightClick, boolean isRegion, Block interactBlock) {
		Creator c = new Creator(creator, sign, rentTime, cost, storageRegion, leftClick, rightClick, isRegion, interactBlock);
		creators.put(creator, c);
	}
	public static void removeCreator(Player p) {
		creators.remove(p);
	}
	public static Creator getCreator(Player creator) {
		return creators.get(creator);
	}

}
