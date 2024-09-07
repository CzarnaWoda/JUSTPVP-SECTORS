package pl.blackwater.storage.data;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import lombok.Data;

@Data
public class Creator {
	
	private Player creator;
	private Sign sign;
	private long rentTime;
	private int cost;
	private ProtectedRegion storageRegion;
	private boolean leftClick,rightClick,isRegion;
	private Block interactBlock;
	
	public Creator(Player creator, Sign sign, long rentTime, int cost, ProtectedRegion storageRegion, boolean leftClick, boolean rightClick, boolean isRegion, Block interactBlock) {
		this.creator = creator;
		this.sign = sign;
		this.rentTime = rentTime;
		this.cost =  cost;
		this.storageRegion = storageRegion;
		this.leftClick = leftClick;
		this.rightClick = rightClick;
		this.isRegion = isRegion;
		this.interactBlock = interactBlock;
	}

}
