package pl.blackwater.chestpvpdrop.drops;

import net.lightshard.itemcases.ItemCases;
import net.lightshard.itemcases.cases.ItemCase;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.blackwater.chestpvpdrop.data.Drop;
import pl.blackwater.chestpvpdrop.data.DropData;
import pl.blackwater.chestpvpdrop.data.DropType;
import pl.blackwater.chestpvpdrop.managers.DropFile;
import pl.blackwater.chestpvpdrop.settings.Config;
import pl.blackwater.chestpvpdrop.utils.DropUtil;
import pl.blackwater.core.data.ChatControlUser;
import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.ChatControlUserManager;
import pl.blackwater.core.managers.EventManager;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.CoreConfig;
import pl.blackwaterapi.utils.RandomUtil;
import pl.blackwaterapi.utils.Util;

import java.util.*;

public class RandomDropData implements DropData, Colors
{
    private static final List<Drop> drops;
    private static final Set<UUID> noCobble;
    private static final Set<UUID> noKomunikaty;
    
    public static void changeNoCobble(final UUID uuid) {
        if (RandomDropData.noCobble.contains(uuid)) {
            RandomDropData.noCobble.remove(uuid);
        }
        else {
            RandomDropData.noCobble.add(uuid);
        }
    }
    
    public static void changeNoKomunikaty(final UUID uuid) {
        if (RandomDropData.noKomunikaty.contains(uuid)) {
            RandomDropData.noKomunikaty.remove(uuid);
        }
        else {
            RandomDropData.noKomunikaty.add(uuid);
        }
    }
    
    public static boolean isNoCobble(final UUID uuid) {
        return RandomDropData.noCobble.contains(uuid);
    }
    
    public static boolean isNoKomunikaty(final UUID uuid) {
        return RandomDropData.noKomunikaty.contains(uuid);
    }
    
    public RandomDropData() {
        super();
        RandomDropData.drops.clear();
        for (final String s : DropFile.getConfig().getConfigurationSection("random-drops").getKeys(false)) {
            final Drop d = new Drop(s);
            RandomDropData.drops.add(d);
        }
    }
    
    
    public static Drop getDropByName(final String name) {
        for (final Drop d : RandomDropData.drops) {
            if (d.getName().equalsIgnoreCase(name)) {
                return d;
            }
        }
        return null;
    }
    
    @Override
    public void breakBlock(final Block block, final Player player, final ItemStack item) {
        final ArrayList<ItemStack> drop = new ArrayList<>();
        for (final Drop d : RandomDropData.drops) {
            if (!d.isDisabled(player.getUniqueId())) {
                final ItemStack itemDrop = d.getWhat().clone();
                int expDrop = d.getExp();
                final int y = block.getLocation().getBlockY();
                if (!d.getFrom().equals(block.getType())) {
                    continue;
                } else if (!d.getTools().contains(item.getType()) || !d.getBiomes().contains(block.getBiome())) {
                    continue;
                }
                final User user = UserManager.getUser(player);
                double dropd;
                double chance = d.getChance();
                if(!d.getWhat().getType().equals(Material.ENDER_PEARL)){
                    chance+=user.getBonusDrop();
                }
                if (player.hasPermission("drop.vip")) {
                		if ((EventManager.isOnTurboDrop()) || (user.isTurboDrop())){
                			if (EventManager.isOnEventDrop()){
                				dropd = ((chance + d.getChanceVIP() + d.getChanceTurboDrop())  * Config.EVENT_DROP);
                			}else{
                				dropd = (chance + d.getChanceVIP() + d.getChanceTurboDrop());
                			}
                		}else{
                			if (EventManager.isOnEventDrop()){
                				dropd = ((chance + d.getChanceVIP())* Config.EVENT_DROP);
                			}else{
                				dropd = (chance + d.getChanceVIP());
                			}
                		}
                }else{
                		if ((EventManager.isOnTurboDrop()) || (user.isTurboDrop())){
                			if (EventManager.isOnEventDrop()){
                				dropd = ((chance + d.getChanceTurboDrop())* Config.EVENT_DROP);
                			}else{
                				dropd = (chance + d.getChanceTurboDrop());
                			}
                		}else{
                			if (EventManager.isOnEventDrop()){
                				dropd = ((chance)* Config.EVENT_DROP);
                			}else{
                				dropd = (chance);
                			}
                		}
                }

            	if (y < d.getMinHeight() || y > d.getMaxHeight() || !RandomUtil.getChance(dropd)) {
                    continue;
                }
                if (item.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS) && d.isFortune()) {
                    final int a = DropUtil.addFortuneEnchant((d.getMinAmount() == d.getMaxAmount()) ? d.getMinAmount() : RandomUtil.getRandInt(d.getMinAmount(), d.getMaxAmount()), item);
                    itemDrop.setAmount(a);
                    expDrop *= a;
                }
                if (itemDrop.getType() == Material.CHEST){
                	if(Config.DROP_CHEST_TIME < System.currentTimeMillis()){
                	ItemCase chest = ItemCases.getInstance().getCaseManager().fromName(Config.DROP_CHEST_NAME);
                	if(chest != null){
                		ItemStack key = chest.getItem();
                		player.getInventory().addItem(key);
                	}
                	}
                }else if (itemDrop.getType() == Material.GOLD_NUGGET){
                	if (Config.COINS_STATUS){
                		int coins = getRandom(Config.COINS_MIN, Config.COINS_MAX);
                		user.addMoney(coins);
                    	if (!RandomDropData.noKomunikaty.contains(player.getUniqueId())) {
                    		Util.sendMsg(player, Util.replaceString(d.getMessage().replace("{AMOUNT}", Integer.toString(coins)).replace("{PKT}", Integer.toString(d.getPkt() * itemDrop.getAmount()))));
                    	}
                	}
                }else{
                    drop.add(itemDrop);
                	if (!RandomDropData.noKomunikaty.contains(player.getUniqueId())) {
                		Util.sendMsg(player, Util.replaceString(d.getMessage().replace("{AMOUNT}", Integer.toString(itemDrop.getAmount())).replace("{PKT}", Integer.toString(d.getPkt() * itemDrop.getAmount()))));
                	}
                }
                if (itemDrop.getType() == Material.DIAMOND){
                    user.addDropDiamonds(itemDrop.getAmount());
                }else if (itemDrop.getType() == Material.EMERALD){
                    user.addDropEmeralds(itemDrop.getAmount());
                }else if (itemDrop.getType() == Material.GOLD_INGOT){
                    user.addDropGolds(itemDrop.getAmount());
                }else if (itemDrop.getType() == Material.IRON_INGOT){
                    user.addDropIrons(itemDrop.getAmount());
                }else if (itemDrop.getType() == Material.COAL){
                    user.addDropCoals(itemDrop.getAmount());
                }else if (itemDrop.getType() == Material.REDSTONE){
                    user.addDropRedStones(itemDrop.getAmount());
                }else if (itemDrop.getType() == Material.SULPHUR){
                    user.addDropGunPowdres(itemDrop.getAmount());
                }else if (itemDrop.getType() == Material.APPLE){
                    user.addDropApples(itemDrop.getAmount());
                }else if (itemDrop.getType() == Material.SLIME_BALL){
                    user.addDropSlimeBalls(itemDrop.getAmount());
                }else if (itemDrop.getType() == Material.ENDER_PEARL){
                    user.addDropPearls(itemDrop.getAmount());
                }else if (itemDrop.getType() == Material.BOOK){
                    user.addDropBooks(itemDrop.getAmount());
                }else if (itemDrop.getType() == Material.CHEST){
                    user.addDropChests(itemDrop.getAmount());
                }
                user.addExp(d.getPkt() * itemDrop.getAmount());
                if(user.getExp() >= user.getExpToLevel()) {
                    final ChatControlUser cc = ChatControlUserManager.getUser(user);
                    user.removeExp(user.getExpToLevel());
                    user.addLevel(1);
                    ChatControlUserManager.sendMsg(player, Util.replaceString(SpecialSigns + ">> " + MainColor + "Awansowales na " + ImportantColor + user.getLevel() + MainColor + " level"), cc.isKillMoneyLevelMessage());
                    player.spigot().playEffect(player.getLocation().add(0.0, 2.0, 0.0), Effect.COLOURED_DUST, 8, 0, 0.8f, 0.8f, 0.8f, 0.04f, 125, 100);
                    player.playSound(player.getLocation(), Sound.ANVIL_USE, 40, 50);
                    final int money = (user.getLevel() > 15 ? 100 : 50);
                    user.addMoney(money);
                    if(CoreConfig.LEVELMANAGER_BROADCASTATLEVEL.contains(user.getLevel())) {
                        player.getWorld().spigot().playEffect(player.getLocation().add(0.0, 2.0, 0.0), Effect.MAGIC_CRIT, 5, 10, 0.8f, 0.4f, 0.7f, 0.05f, 120, 750);
                        Bukkit.broadcastMessage(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + WarningColor_2 +  BOLD + "Gratulacje! " + MainColor + "gracz " + ImportantColor + user.getLastName() + MainColor + " zdobyl " + ImportantColor + user.getLevel() + MainColor + " level")));
                    }
                }
                if(player.hasPermission("drop.vip")){
           		if ((EventManager.isOnTurboExp()) || (user.isTurboExp())){
        			if (EventManager.isOnEventExp()){
                    	player.giveExp((expDrop + Config.DROP_VIP_EXP + Config.DROP_TURBO_EXP) * Config.EVENT_EXP);
        			}else{
                    	player.giveExp(expDrop + Config.DROP_VIP_EXP + Config.DROP_TURBO_EXP);
        			}
        		}else
                if (EventManager.isOnEventExp()){
                	player.giveExp((expDrop + Config.DROP_VIP_EXP) * Config.EVENT_EXP);
                }else{
                	player.giveExp(expDrop + Config.DROP_VIP_EXP);
                }
                }else{
               		if ((EventManager.isOnTurboExp()) || (user.isTurboExp())){
            			if (EventManager.isOnEventExp()){
                        	player.giveExp((expDrop + Config.DROP_TURBO_EXP) * Config.EVENT_EXP);
            			}else{
                        	player.giveExp(expDrop + Config.DROP_TURBO_EXP);
            			}
            		}else
                    if (EventManager.isOnEventExp()){
                    	player.giveExp((expDrop) * Config.EVENT_EXP);
                    }else{
                    	player.giveExp(expDrop);
                    }
                }
            }
        }
        if (!RandomDropData.noCobble.contains(player.getUniqueId())) {
            drop.add(new ItemStack(item.containsEnchantment(Enchantment.SILK_TOUCH) ? Material.STONE : Material.COBBLESTONE, 1));
            User user = UserManager.getUser(player);
            user.addDropStones(1);
            if(!EventManager.isOnEventInfinityStone()){
            	User u = UserManager.getUser(player);
            }
        }else{
            User user = UserManager.getUser(player);
            user.addDropStones(1);
            if(!EventManager.isOnEventInfinityStone()){
            	User u = UserManager.getUser(player);
            }
        }
        DropUtil.addItemsToPlayer(player, drop, block);
        DropUtil.recalculateDurability(player, item);
        block.setType(Material.AIR);
    }
    
    
    @Override
    public DropType getDropType() {
        return DropType.RANDOM_DROP;
    }
    
    public static List<Drop> getDrops() {
        return RandomDropData.drops;
    }
    
    private int getRandom(int lower, int upper)
    {
      Random random = new Random();
      return random.nextInt(upper - lower + 1) + lower;
    }
    
    static {
        drops = new ArrayList<>();
        noCobble = new HashSet<>();
        noKomunikaty = new HashSet<>();
    }
}
