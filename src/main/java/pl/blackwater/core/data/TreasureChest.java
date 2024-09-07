package pl.blackwater.core.data;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.blackwater.core.Core;
import pl.blackwater.core.enums.ChestType;
import pl.blackwater.core.events.CustomEventManager;
import pl.blackwater.core.events.EventType;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.TreasureChestManager;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.CoreConfig;
import pl.blackwater.core.utils.LocationUtil;
import pl.blackwater.market.utils.ItemStackUtil;
import pl.blackwaterapi.store.Entry;
import pl.blackwaterapi.utils.Base64Util;
import pl.blackwaterapi.utils.GsonUtil;
import pl.blackwaterapi.utils.RandomUtil;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.impl.customevents.AddStatCustomEventPacket;
import pl.justsectors.redis.channels.RedisChannel;
import pl.justsectors.redis.client.RedisClient;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Data
public class TreasureChest implements Entry, Colors {

    private UUID uuid;
    private Location location;
    private ChestType type;
    private transient ItemStack[] inventory;
    private String inventoryString;
    private long time;

    public TreasureChest(UUID uuid, Location location, ChestType type, ItemStack[] inventory, long time){
        this.uuid = uuid;
        this.location = location;
        this.type = type;
        this.inventory = inventory;
        this.inventoryString = Base64Util.itemStackArrayToBase64(inventory);
        this.time = time;
    }

    public void setup()
    {
        try {
            this.inventory = Base64Util.itemStackArrayFromBase64(this.inventoryString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insert() {
        RedisChannel.INSTANCE.TREASURE_CHESTS.putAsync(this.uuid, GsonUtil.toJson(this));
    }

    @Override
    public void update(boolean b) {
        insert();
    }

    @Override
    public void delete() {
        RedisChannel.INSTANCE.TREASURE_CHESTS.removeAsync(this.uuid);
    }

    public void openNormalChest(Chest chest, User user) {
        if (user.getChesttimes().get(getUuid()) != null) {
            final long time = user.getChesttimes().get(getUuid());
            if (time > System.currentTimeMillis()) {
                Util.sendMsg(user.getPlayer(), MainColor + "Ta skrzynke bedziesz mogl otworzyc za " + ImportantColor + Util.secondsToString((int) ((time-System.currentTimeMillis())/1000L)));
                return;
            }
        }
        final Inventory inv = chest.getInventory();
        int index = 0;
        int set = 0;
        boolean debug = false;
        for (int i = 0; i < inv.getSize(); i++) {
            if (RandomUtil.getChance(((100.0 / (inv.getSize() + 1)) + Math.min(5,(double)user.getKills()/1000.0)) * (CoreConfig.TREASUREMANAGER_POWER/100.0D))) {
                Material material;
                ItemStack itemStack;
                if(RandomUtil.getChance(80.0)) {
                    final int random = RandomUtil.getRandInt(0, Core.getTreasureChestManager().getNormalMaterialList().size() - 1);
                    material = Core.getTreasureChestManager().getNormalMaterialList().get(random);
                    final int amount = RandomUtil.getRandInt(1, Math.min(14, Math.max((user.getKills() / 250), 3)));
                    //5120
                    itemStack = new ItemStack(material,amount);
                }else{
                    final int random = RandomUtil.getRandInt(0, Core.getTreasureChestManager().getNormalKitMaterialList().size() - 1);
                    material = Core.getTreasureChestManager().getNormalKitMaterialList().get(random);
                    if(material == Material.IRON_SWORD || material == Material.GOLD_SWORD || material == Material.STONE_SWORD || material == Material.WOOD_SWORD){
                        if(RandomUtil.getChance(50.0)){
                            int randomMaxEnchant = (Core.getTreasureChestManager().getMaxEnchants().get(material) == 1 ? 1 : RandomUtil.getRandInt(1, Core.getTreasureChestManager().getMaxEnchants().get(material)));
                            itemStack = new ItemStack(material,1);
                            itemStack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, randomMaxEnchant);
                            randomMaxEnchant = (Core.getTreasureChestManager().getMaxEnchants().get(material) == 1 ? 1 : RandomUtil.getRandInt(1, Core.getTreasureChestManager().getMaxEnchants().get(material)));
                            itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, randomMaxEnchant);
                        }else{
                            final int randomMaxEnchant = (Core.getTreasureChestManager().getMaxEnchants().get(material) == 1 ? 1 : RandomUtil.getRandInt(1, Core.getTreasureChestManager().getMaxEnchants().get(material)));
                            itemStack = new ItemStack(material,1);
                            itemStack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, randomMaxEnchant);
                        }
                    }else{
                        if(RandomUtil.getChance(50.0)){
                            int randomMaxEnchant = (Core.getTreasureChestManager().getMaxEnchants().get(material) == 1 ? 1 : RandomUtil.getRandInt(1, Core.getTreasureChestManager().getMaxEnchants().get(material)));
                            itemStack = new ItemStack(material,1);
                            itemStack.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, randomMaxEnchant);
                            randomMaxEnchant = (Core.getTreasureChestManager().getMaxEnchants().get(material) == 1 ? 1 : RandomUtil.getRandInt(1, Core.getTreasureChestManager().getMaxEnchants().get(material)));
                            itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, randomMaxEnchant);
                        }else{
                            final int randomMaxEnchant = (Core.getTreasureChestManager().getMaxEnchants().get(material) == 1 ? 1 : RandomUtil.getRandInt(1, Core.getTreasureChestManager().getMaxEnchants().get(material)));
                            itemStack = new ItemStack(material,1);
                            itemStack.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, randomMaxEnchant);
                        }
                    }
                }
                while (inv.getItem(set) != null && !debug){
                    if(set == inv.getSize()-1){
                        set = 0;
                        debug = true;
                    }
                    set++;
                }
                inv.setItem(set, itemStack);
                set++;
                index++;
             }
         }
        while (index == 0){
            if (RandomUtil.getChance(100.0 / (inv.getSize() + 1))) {
                final int random = RandomUtil.getRandInt(0, Core.getTreasureChestManager().getNormalMaterialList().size() - 1);
                final Material material = Core.getTreasureChestManager().getNormalMaterialList().get(random);
                final int amount = RandomUtil.getRandInt(1, Math.min(32, Math.max((user.getKills() / 20), 3)));
                ItemStack itemStack = new ItemStack(material, amount);
                while (inv.getItem(set) != null && !debug){
                    if(set == inv.getSize()-1){
                        set = 0;
                        debug = true;
                    }
                    set++;
                }
                inv.setItem(set, itemStack);
                index++;
            }
        }
        user.getChesttimes().put(getUuid(), System.currentTimeMillis() + this.time);
        update(false);
        if(CustomEventManager.getActiveEvent() != null && CustomEventManager.getActiveEvent().getEventType().equals(EventType.OPEN_CHEST)){
            final AddStatCustomEventPacket packet = new AddStatCustomEventPacket(user.getUuid(), 1);
            RedisClient.sendSectorsPacket(packet);
        }
        Util.sendMsg(user.getPlayer(), MainColor + "Otworzyles skrzynke z " + ImportantColor + "przedmiotami " + MainColor + "nastepny raz bedziesz ją mógł otworzyc za " + ImportantColor + Util.secondsToString((int) ((user.getChesttimes().get(getUuid()) - System.currentTimeMillis())/1000L)));
    }
    public void openUnlimitedChest(Player player){
        Inventory inv = Bukkit.createInventory(player, inventory.length, Util.fixColor(Util.replaceString(MainColor + "Skrzynia " + ImportantColor + "unlimited")));
        inv.setContents(getInventory());
        player.openInventory(inv);
        Util.sendMsg(player, MainColor + "Otworzyles skrzynke z " + ImportantColor + "przedmiotami" + MainColor + ", bierz ile " + ImportantColor + "chcesz");
    }
    public void openStaticChest(Player player){
        final User user = UserManager.getUser(player);
        if (user.getChesttimes().get(getUuid()) != null) {
            final long time = user.getChesttimes().get(getUuid());
            if (time > System.currentTimeMillis()) {
                Util.sendMsg(player, MainColor + "Ta skrzynke bedziesz mogl otworzyc za " + ImportantColor + Util.secondsToString((int) ((time-System.currentTimeMillis())/1000L)));
                return;
            }
        }
        user.getChesttimes().put(getUuid(), System.currentTimeMillis() + this.time);
        update(false);
        Inventory inv = Bukkit.createInventory(player, inventory.length, Util.fixColor(Util.replaceString(MainColor + "Skrzynia " + ImportantColor + "skarbów")));
        inv.setContents(getInventory());
        player.openInventory(inv);
        if(CustomEventManager.getActiveEvent() != null && CustomEventManager.getActiveEvent().getEventType().equals(EventType.OPEN_CHEST)){
            final AddStatCustomEventPacket packet = new AddStatCustomEventPacket(user.getUuid(), 1);
            RedisClient.sendSectorsPacket(packet);
        }
        Util.sendMsg(player, MainColor + "Otworzyles skrzynke z " + ImportantColor + "przedmiotami " + MainColor + "nastepny raz bedziesz ją mógł otworzyc za " + ImportantColor + Util.secondsToString((int) ((user.getChesttimes().get(getUuid()) - System.currentTimeMillis())/1000L)));
    }
}
