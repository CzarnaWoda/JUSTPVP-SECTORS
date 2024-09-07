package pl.blackwaterapi.timer;

import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import pl.blackwaterapi.utils.ItemBuilder;
@Getter
public class ItemBuilderTimer extends BukkitRunnable{
	private ItemBuilder itemBuilder;
	private Inventory inv;
	private int position;
	public ItemBuilderTimer(ItemBuilder itemBuilder,int position,Inventory inv) {
		super();
		this.itemBuilder = itemBuilder;
		this.position = position;
		this.inv = inv;
	}
	@Override
	public void run() {
		inv.setItem(position, itemBuilder.build());
	}

}
