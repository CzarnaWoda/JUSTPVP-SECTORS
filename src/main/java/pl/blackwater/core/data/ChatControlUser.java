package pl.blackwater.core.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lombok.Data;
import lombok.Getter;
import pl.blackwater.core.managers.UserManager;
import pl.blackwaterapi.gui.actions.IAction;
import pl.blackwaterapi.gui.actions.InventoryGUI;
import pl.blackwaterapi.utils.ItemBuilder;
import pl.blackwaterapi.utils.Util;
@Data
public class ChatControlUser {
	public static String caseOpenAccess = "message.caseopen",deathMessageAccess = "message.death",killKeyAccess = "message.killkey",killDiamondAccess = "message.killdiamond",killTurboMoneyAccess = "message.turbomoney",killKillStreakAccess = "message.killstreak", killMoneyLevelAccess = "message.moneylevel",itemShopMessageAccess = "message.itemshop",killStreakGlobalAccess = "message.globalkillstreak",autoMessageGlobalAccess = "message.automessage",marketMessageGlobalAccess = "message.market",headhunterMessageGlobalAccess = "message.headhunter";
	@Getter public static String caseOpenTitle = Util.fixColor(Util.replaceString("&8>> &7Wiadomosci &8* &6Otworzone Skrzynki &8<<")),deathMessageTitle = Util.fixColor(Util.replaceString("&8>> &7Wiadomosci &8* &6O Smierci &8<<")),killKeyMessageTitle = Util.fixColor(Util.replaceString("&8>> &7Wiadomosci &8* &6Klucz Za Zabojstwo &8<<")),killDiamondMessageTitle = Util.fixColor(Util.replaceString("&8>> &7Wiadomosci &8* &6Diament Za Zabojstwo &8<<")),killTurboMoneyMessageTitle = Util.fixColor(Util.replaceString("&8>> &7Wiadomosci &8* &6TurboMoney Za Zabojstwo &8<<")),killKillStreakMessageTitle = Util.fixColor(Util.replaceString("&8>> &7Wiadomosci &8* &6KillStreak Za Zabojstwo &8<<")),killMoneyLevelMessageTitle = Util.fixColor(Util.replaceString("&8>> &7Wiadomosci &8* &6Kasa I Level Za Zabojstwo &8<<")),itemShopMessageTitle = Util.fixColor(Util.replaceString("&8>> &7Wiadomosci &8* &6ItemShop &8<<")),killStreakGlobalMessageTitle = Util.fixColor(Util.replaceString("&8>> &7Wiadomosci &8* &6Globalne Zdobycia KillStreak &8<<")),autoMessageGlobalTitle = Util.fixColor(Util.replaceString("&8>> &7Wiadomosci &8* &6Auto-Message &8<<")),marketMessageGlobalTitle = Util.fixColor(Util.replaceString("&8>> &7Wiadomosci &8* &6Market &8<<")),headhunterMessageGlobalTitle = Util.fixColor(Util.replaceString("&8>> &7Wiadomosci &8* &6Lowcy Glow &8<<"));
	private UUID uuid;
	private boolean caseOpenMessage,deathMessage,killKeyMessage,killDiamondMessage,killTurboMoneyMessage,killKillStreakMessage,killMoneyLevelMessage,itemShopMessage,killStreakGlobalMessage,autoMessage,marketGlobalMessage,headhunterGlobalMessage;
	@Getter private static ItemBuilder air = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)7);
	
	public ChatControlUser(final User u,final List<String> message){
		uuid = u.getUuid();
		caseOpenMessage = message.contains(caseOpenAccess);
		deathMessage = message.contains(deathMessageAccess);
		killKeyMessage = message.contains(killKeyAccess);
		killDiamondMessage =  message.contains(killDiamondAccess);
		killTurboMoneyMessage = message.contains(killTurboMoneyAccess);
		killKillStreakMessage = message.contains(killKillStreakAccess);
		killMoneyLevelMessage = message.contains(killMoneyLevelAccess);
		itemShopMessage = message.contains(itemShopMessageAccess);
		killStreakGlobalMessage = message.contains(killStreakGlobalAccess);
		autoMessage = message.contains(autoMessageGlobalAccess);
		marketGlobalMessage = message.contains(marketMessageGlobalAccess);
		headhunterGlobalMessage = message.contains(headhunterMessageGlobalAccess);
	}
	public void update(){
		User u = UserManager.getUser(uuid);
		u.setChatControl(getChatControl());
	}
	public boolean isCaseOpenMessage(){
		return !caseOpenMessage;
	}
	public boolean isDeathMessage(){
		return !deathMessage;
	}
	public boolean isKillKeyMessage(){
		return !killKeyMessage;
	}
	public boolean isKillDiamondMessage(){
		return !killDiamondMessage;
	}
	public boolean isKillTurboMoneyMessage(){
		return !killTurboMoneyMessage;
	}
	public boolean isKillKillStreakMessage(){
		return !killKillStreakMessage;
	}
	public boolean isKillMoneyLevelMessage(){
		return !killMoneyLevelMessage;
	}
	public boolean isItemShopMessage(){
		return !itemShopMessage;
	}
	public boolean isKillStreakGlobalMessage(){
		return !killStreakGlobalMessage;
	}
	public boolean isAutoMessage(){
		return !autoMessage;
	}
	public boolean isMarketGlobalMessage(){
		return !marketGlobalMessage;
	}
	public boolean isHeadHunterGlobalMessage(){
		return !headhunterGlobalMessage;
	}
	public void toggleCaseOpenMessage(ItemStack MainItem, ItemStack glass){
		caseOpenMessage = !caseOpenMessage;
		toggleItem(MainItem, glass, isCaseOpenMessage());
	}
	public void toggleDeathMessage(ItemStack MainItem, ItemStack glass){
		deathMessage = !deathMessage;
		toggleItem(MainItem, glass, isDeathMessage());

	}
	public void toggleKillKeyMessage(ItemStack MainItem, ItemStack glass){
		killKeyMessage = !killKeyMessage;
		toggleItem(MainItem, glass, isKillKeyMessage());
	}
	public void toggleKillDiamondMessage(ItemStack MainItem, ItemStack glass){
		killDiamondMessage = !killDiamondMessage;
		toggleItem(MainItem, glass, isKillDiamondMessage());
	}
	public void toggleKillTurboMoneyMessage(ItemStack MainItem, ItemStack glass){
		killTurboMoneyMessage = !killTurboMoneyMessage;
		toggleItem(MainItem, glass, isKillTurboMoneyMessage());
	}
	public void toggleKillKillStreakMessage(ItemStack MainItem, ItemStack glass){
		killKillStreakMessage = !killKillStreakMessage;
		toggleItem(MainItem, glass, isKillKillStreakMessage());
	}
	public void toggleKillMoneyLevelMessage(ItemStack MainItem, ItemStack glass){
		killMoneyLevelMessage = !killMoneyLevelMessage;
		toggleItem(MainItem, glass, isKillMoneyLevelMessage());
	}
	public void toggleItemShopMessage(ItemStack MainItem, ItemStack glass){
		itemShopMessage = !itemShopMessage;
		toggleItem(MainItem, glass, isItemShopMessage());
	}
	public void toggleKillStreakGlobalMessage(ItemStack MainItem, ItemStack glass){
		killStreakGlobalMessage = !killStreakGlobalMessage;
		toggleItem(MainItem, glass, isKillStreakGlobalMessage());
	}
	public void toggleAutoMessage(ItemStack MainItem, ItemStack glass){
		autoMessage = !autoMessage;
		toggleItem(MainItem, glass, isAutoMessage());
	}
	public void toggleMarketMessage(ItemStack MainItem, ItemStack glass){
		marketGlobalMessage = !marketGlobalMessage;
		toggleItem(MainItem, glass, isMarketGlobalMessage());
	}
	public void toggleHeadHunterMessage(ItemStack MainItem, ItemStack glass){
		headhunterGlobalMessage = !headhunterGlobalMessage;
		toggleItem(MainItem, glass, isHeadHunterGlobalMessage());
	}
	public void toggleItem(ItemStack MainItem,ItemStack glass,boolean b){
		MainItem.setDurability((short)(b ? 5 : 14));
		ItemMeta MainMeta = MainItem.getItemMeta();
		MainMeta.setLore(Arrays.asList(Util.fixColor(Util.replaceString("&8* &7Status: " + (b ? "&2Wlaczone" : "&cWylaczone")))));
		MainItem.setItemMeta(MainMeta);
		glass.setDurability((short)(b ? 5 : 14));
		ItemMeta GlassMeta = glass.getItemMeta();
		GlassMeta.setDisplayName(Util.fixColor(Util.replaceString((b ? "&2%V%" : "&c%X%"))));
		glass.setItemMeta(GlassMeta);
		update();
	}
	public List<String> getChatControl(){
		List<String> cc = new ArrayList<String>();
		if(caseOpenMessage){
			cc.add(caseOpenAccess);
		}
		if(deathMessage){
			cc.add(deathMessageAccess);
		}
		if(killKeyMessage){
			cc.add(killKeyAccess);
		}
		if(killDiamondMessage){
			cc.add(killDiamondAccess);
		}
		if(killTurboMoneyMessage){
			cc.add(killTurboMoneyAccess);
		}
		if(killKillStreakMessage){
			cc.add(killKillStreakAccess);
		}
		if(killMoneyLevelMessage){
			cc.add(killMoneyLevelAccess);
		}
		if(itemShopMessage){
			cc.add(itemShopMessageAccess);
		}
		if(killStreakGlobalMessage){
			cc.add(killStreakGlobalAccess);
		}
		if(autoMessage){
			cc.add(autoMessageGlobalAccess);
		}
		if(marketGlobalMessage){
			cc.add(marketMessageGlobalAccess);
		}
		if(headhunterGlobalMessage){
			cc.add(headhunterMessageGlobalAccess);
		}
		return cc;
	}
	public void getItem(boolean b,String title,InventoryGUI inv,int position,int type,IAction a){
		ItemBuilder air = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)(type == 1 ? 1 : 11)).setTitle(Util.fixColor(Util.replaceString("&8* " + (type == 1 ? "&6&oWiadomosc Globalna" : "&9&oWiadomosc Do Gracza"))));
		ItemBuilder itemBuilder = new ItemBuilder(Material.STAINED_CLAY,1,(short)(b ? 5 : 14));
		itemBuilder.setTitle(Util.fixColor(Util.replaceString(title)));
		itemBuilder.addLore(Util.fixColor(Util.replaceString("&8* &7Status: " + (b ? "&2Wlaczone" : "&cWylaczone"))));
		ItemBuilder glass = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)(b ? 5 : 14)).setTitle(Util.fixColor(Util.replaceString((b ? "&2%V%" : "&c%X%"))));
		inv.setItem(position - 9, air.build(),null);
		inv.setItem(position, itemBuilder.build(), a);
		inv.setItem(position + 9, glass.build(),null);
	}

}
