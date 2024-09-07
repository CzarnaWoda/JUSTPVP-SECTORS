package pl.blackwater.market.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import lombok.Setter;
import pl.blackwater.core.Core;
import pl.blackwater.market.utils.ItemStackUtil;
import pl.blackwaterapi.store.Entry;
import pl.blackwaterapi.utils.GsonUtil;
import pl.justsectors.redis.channels.RedisChannel;

@Getter
@Setter
public class Auction implements Entry{

	private int index;
	private UUID owner;
	private long date;
	private int cost;
	private transient ItemStack item;
	private String itemStackString;
	private boolean sold;

	public Auction(int index,UUID owner,int cost, ItemStack item){
		super();
		this.index = index;
		this.owner = owner;
		this.date = System.currentTimeMillis();
		this.cost = cost;
		this.item = item;
		this.itemStackString = ItemStackUtil.ConvertItemStackToString(item);
		this.sold = false;
	}


	public void setup()
	{
		this.item = ItemStackUtil.ConvertStringToItemStack(this.itemStackString);
	}

	public void insert() {
		RedisChannel.INSTANCE.AUCTIONS.putAsync(this.index, GsonUtil.toJson(this));
	}

	public void update(boolean now) {
		insert();
	}

	public void delete() {
		System.out.println("Tego obiektu nie mozna usunac z bazy danych !");
	}

}
