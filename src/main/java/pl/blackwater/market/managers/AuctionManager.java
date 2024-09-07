package pl.blackwater.market.managers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import pl.blackwater.core.Core;
import pl.blackwater.market.data.Auction;
import pl.blackwaterapi.utils.GsonUtil;
import pl.blackwaterapi.utils.Logger;
import pl.justsectors.redis.channels.RedisChannel;

public class AuctionManager {
	@Getter public static Map<Integer, Auction> auctions = new HashMap<>();

	public static Auction createAuction(Player p, int cost,ItemStack item){
		int index = auctions.size() + 1;
		return new Auction(index, p.getUniqueId(), cost, item);
	}

	public static void registerAuction(final Auction auction)
	{
		auctions.put(auction.getIndex(), auction);
	}

	public static Auction getAuction(int index){
		return auctions.get(index);
	}
	public static Map<Integer,Auction> getNonSoldAuctions(){
		Map<Integer,Auction> auctionslist = new HashMap<Integer,Auction>();
		int i = 1;
		for(Auction a : auctions.values()){
			if(!a.isSold()){
			auctionslist.put(i, a);
			i++;
			}
		}
		return auctionslist;
	}
	public static Set<Auction> getFromToAuction(int from, int to){
		Set<Auction> set = new HashSet<>();
		Map<Integer, Auction> map = getNonSoldAuctions();
		for(int i = from; i < to; i ++){
			if(map.get(i) != null){
				set.add(map.get(i));
			}else{
				return set;
			}
		}
		return set;
	}
	public static void loadAuctions(){
		RedisChannel.INSTANCE.AUCTIONS.forEach(((integer, s) -> {
			Auction auction = GsonUtil.fromJson(s, Auction.class);
			auctions.put(auction.getIndex(), auction);
			auction.setup();
		}));
		Logger.info("Loaded " + auctions.size() + " auctions");
	}
}
