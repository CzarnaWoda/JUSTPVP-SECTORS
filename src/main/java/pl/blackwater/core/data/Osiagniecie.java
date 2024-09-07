package pl.blackwater.core.data;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lombok.Data;
import net.lightshard.itemcases.ItemCases;
import net.lightshard.itemcases.cases.ItemCase;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.interfaces.SpecialCharacters;
import pl.blackwater.core.managers.EnchantManager;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.OsiagnieciaConfig;
import pl.blackwaterapi.utils.ItemUtil;
import pl.blackwaterapi.utils.Logger;
import pl.blackwaterapi.utils.TitleUtil;
import pl.blackwaterapi.utils.Util;

@Data
public class Osiagniecie implements Colors,SpecialCharacters{
	private String id,name,gui_itemname,type;
	private ItemStack gui_item;
	private int gui_itemdata,amount_toachivment,reward_money;
	private List<String> rewardsenchantment,rewardsenchantmentlevel,reward_keys,gui_seconditemlore,gui_itemlore;
	private List<Integer> rewardsdata,rewardsamount,reward_keysamount;
	private List<ItemStack> rewardslist;
	public static List<Integer> BREAKSTONE_LIST;
	public Osiagniecie(final String id,final String name,final ItemStack gui_item,final int gui_itemdata,final String gui_itemname,final List<String> gui_itemlore,final List<String> gui_seconditemlore,final String type,final int amount_toachivment,final List<ItemStack> rewardslist,final List<Integer> rewardsdata,final List<Integer> rewardsamount,final List<String> rewardsenchantments,final List<String> rewardsenchantmentlevel,final int reward_money,List<String> reward_keys,final List<Integer> reward_keysamount){
		this.id = id;
		this.name = name;
		this.gui_item = gui_item;
		this.gui_itemdata = gui_itemdata;
		this.gui_itemname = gui_itemname;
		this.gui_itemlore = gui_itemlore;
		this.gui_seconditemlore = gui_seconditemlore;
		this.type = type;
		this.amount_toachivment = amount_toachivment;
		this.rewardslist = rewardslist;
		this.rewardsdata = rewardsdata;
		this.rewardsamount  = rewardsamount;
		this.rewardsenchantment = rewardsenchantments;
		this.rewardsenchantmentlevel = rewardsenchantmentlevel;
		this.reward_money = reward_money;
		this.reward_keys = reward_keys;
		this.reward_keysamount = reward_keysamount;
	}
	public final List<ItemStack> getRewards(){
		final List<ItemStack> rewards = new ArrayList<>();
		for(int i = 0; i<rewardslist.size();i++){
			final Material mat = rewardslist.get(i).getType();
			final ItemStack item = new ItemStack(mat,(rewardsamount.size() > i ? rewardsamount.get(i) : 1),(short)(rewardsdata.size() > i ? rewardsdata.get(i) : 0));
			final ItemMeta meta = item.getItemMeta();
			final List<Enchantment> rewardsenchantments = EnchantManager.fromStringToEnchantList(rewardsenchantment.get(i));
			final List<Integer> rewardsenchantmentlevels = EnchantManager.fromStringToIntegerList(rewardsenchantmentlevel.get(i));
			for(int ii = 0; ii < rewardsenchantments.size(); ii ++){
			if(!rewardsenchantments.isEmpty() && !rewardsenchantments.get(0).equals("")){
				meta.addEnchant(rewardsenchantments.get(ii), (rewardsenchantmentlevels.size() > ii && !rewardsenchantmentlevel.equals("") ? rewardsenchantmentlevels.get(ii) : rewardsenchantments.get(ii).getMaxLevel()), true);
			}
			}
			item.setItemMeta(meta);
			rewards.add(item);
		}
		return rewards;
	}
	public final ItemStack getGuiItem(final double Procent,final int progress){
		final ItemStack item = new ItemStack(gui_item.getType(),1,(short)gui_itemdata);
		final ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Util.fixColor(Util.replaceString(gui_itemname)));
		final List<String> lore = new ArrayList<>();
		final List<String> itemlore = (progress >= getAmount_toachivment() ? getGui_seconditemlore() : getGui_itemlore());
		for(String s : itemlore){
			lore.add(Util.fixColor(Util.replaceString(s
						.replace("{REACHAMOUNT}", (type.equalsIgnoreCase("SPEND_TIME") ? Util.secondsToString(getAmount_toachivment()) : String.valueOf(getAmount_toachivment())))
						.replace("{PROCENT}", String.valueOf(Procent))
						.replace("{AMOUNT}", (type.equalsIgnoreCase("SPEND_TIME") ? Util.secondsToString(progress) : String.valueOf(progress))))));
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	public final List<ItemStack> getRewardKeys(){
		final List<ItemStack> keys = new ArrayList<>();
		for(int i = 0;i<getReward_keys().size();i++){
			final ItemCase itemcase = ItemCases.getInstance().getCaseManager().fromName(getReward_keys().get(i));
			if(itemcase == null){
				Logger.fixColorSend("&cError &4ItemCase not found (null)");
				return keys;
			}
			final ItemStack key = itemcase.getKey();
			key.setAmount(getReward_keysamount().get(i));
			keys.add(key);
		}
		return keys;
	}
	@SuppressWarnings("deprecation")
	public static void Complete(final Player p, final Osiagniecie osiagniecie) {
		  final User u = UserManager.getUser(p);
		  for(String s : OsiagnieciaConfig.MESSAGE_OSIAGNIECIE){
			  if(!s.equals("{COMPOSITION}")){
				  Bukkit.broadcastMessage(Util.fixColor(Util.replaceString(s)
						  .replace("{OSIAGNIECIE}", osiagniecie.getName())
						  .replace("{PLAYER}", p.getName())));
				}else
					Util.SendRun_CommandTextComponentToALL(OsiagnieciaConfig.MESSAGE_COMPOSITION_TEXT, OsiagnieciaConfig.MESSAGE_COMPOSITION_COMMAND, OsiagnieciaConfig.MESSAGE_COMPOSITION_HIDETEXT);
			}
		  final int money = osiagniecie.getReward_money();
	      if(money > 0){
	    	  u.addMoneyViaPacket(money);
	      }
	      final List<ItemStack> keys = osiagniecie.getRewardKeys();
	      if(keys.size() > 0){
	    	  ItemUtil.giveItems(keys, p);
	      }
	      final List<ItemStack> rewards = osiagniecie.getRewards();
	      if(rewards.size() > 0){
	    	  ItemUtil.giveItems(rewards, p);
	      }
	      TitleUtil.sendFullTitle(p, 10, 100, 5, Util.replaceString(SpecialSigns + ">> " + WarningColor + "Osiagniecie" + SpecialSigns + " <<") , Util.replaceString(SpecialSigns + "* " + MainColor + "Zdobyto " + ImportantColor + BOLD + "osiagniecie " + WarningColor + smile + SpecialSigns + " *"));
		  p.spigot().playEffect(p.getLocation().add(0.0, 2.0, 0.0), Effect.CLOUD, 145, 0, 1.2f, 1.2f, 1.2f, 0.04f, 50, 100);
		  p.spigot().playEffect(p.getLocation().add(0.0, 2.0, 0.0), Effect.HEART, 145, 0, 0.8f, 0.8f, 0.8f, 0.04f, 20, 100);
		  p.spigot().playEffect(p.getLocation().add(0.0, 2.0, 0.0), Effect.COLOURED_DUST, 8, 0, 0.8f, 0.8f, 0.8f, 0.04f, 125, 100);
		  p.playSound(p.getLocation(), Sound.CAT_MEOW, 40, 50);
		  u.update(true);
	}
}
