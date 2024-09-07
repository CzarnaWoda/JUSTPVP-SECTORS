package pl.blackwater.guilds.commands.user;

import net.lightshard.itemcases.ItemCases;
import net.lightshard.itemcases.cases.ItemCase;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import pl.blackwater.core.Core;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.guilds.data.Guild;
import pl.blackwater.guilds.data.GuildShopItem;
import pl.blackwater.guilds.data.Member;
import pl.blackwater.guilds.managers.GuildManager;
import pl.blackwater.guilds.managers.MemberManager;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.gui.actions.InventoryGUI;
import pl.blackwaterapi.utils.ItemBuilder;
import pl.blackwaterapi.utils.ItemUtil;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.impl.guild.GuildEffectPacket;
import pl.justsectors.packets.impl.guild.GuildRemoveSoulsPacket;
import pl.justsectors.redis.client.RedisClient;

import java.util.Arrays;
import java.util.Collections;

public class ShopCommand extends PlayerCommand implements Colors {
    public ShopCommand() {
        super("sklep", "Sklep gildyjny", "/g sklep", "guild.gracz", "shop");
    }

    @Override
    public boolean onCommand(Player player, String[] strings) {


        final Guild g = GuildManager.getGuild(player);
        if(g == null){
            return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz gildii");
        }
        if(!g.isCanChangePvP(player.getUniqueId())){
            return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz uprawnien aby to zrobic");
        }
        openGlobalShop(player,g);
        return false;
    }

    private static boolean checkSouls(Guild g,int souls){
        if(g.getGuildSoul() >= souls){
            final RedisPacket packet = new GuildRemoveSoulsPacket(g.getTag(), souls);
            RedisClient.sendSectorsPacket(packet);
            g.update(true);
            return true;
        }
        return false;
    }
    private static void openGlobalShop(Player player, Guild guild){
        final InventoryGUI inv = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(SpecialSigns + "->> [" + " &6&nGLOBAL SHOP&8 " + SpecialSigns + "] <<-")),1);

        final ItemBuilder effects = new ItemBuilder(Material.POTION,1,(short)16473).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + ImportantColor  + UnderLined + "SKLEP Z EFEKTAMI " + SpecialSigns + "<<-|")))
                .addLore(Util.fixColor(Util.replaceString("  &8->> &7To tutaj zakupisz efekty dla &6CALEJ GILDII")));
        inv.setItem(1,effects.build(),(paramPlayer, paramInventory, paramInt, paramItemStack) -> {
            player.closeInventory();
            openEffectShop(player,guild);
        });


        final ItemBuilder items = new ItemBuilder(Material.DIAMOND,1).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + ImportantColor  + UnderLined + "SKLEP Z PRZEDMIOTAMI " + SpecialSigns + "<<-|")))
                .addLore(Util.fixColor(Util.replaceString("  &8->> &7W tym sklepie kupisz &6wszystkie przedmioty&7 dla &6swojej gildii&7!")));
        inv.setItem(7,items.build(),(paramPlayer, paramInventory, paramInt, paramItemStack) -> {
            player.closeInventory();
            openItemShop(player,guild);
        });

        final ItemBuilder chest = new ItemBuilder(Material.CHEST,1).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + ImportantColor  + UnderLined + "MAGICZNA SKRZYNIA GILDII " + SpecialSigns + "<<-|")))
                .addLore(Util.fixColor(Util.replaceString("&8  ->> &7W tej skrzyni kryja sie &cPOTEZNE PRZEDMIOTY")))
                .addLore(Util.fixColor(Util.replaceString("&8  ->> &7Koszt: &c65 DUSZ")));

        inv.setItem(4,chest.build(),(paramPlayer, paramInventory, paramInt, paramItemStack) -> {
            if(checkSouls(guild,65)) {
                paramPlayer.closeInventory();
                ItemCase itemCase = ItemCases.getInstance().getCaseManager().fromName("guildcreate");
                itemCase.open(paramPlayer);
            }else{
                Util.sendMsg(player, Util.replaceString(SpecialSigns + "->> " + MainColor  + "Nie posiadasz tyle dusz w swojej gidlii!"));
            }
        });
        inv.openInventory(player);


    }

    private static void openItemShop(Player player , Guild guild){

        final InventoryGUI inv = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(SpecialSigns + "->> [" + " &6&nITEMS SHOP&8 " + SpecialSigns + "] <<-")),1);

        final ItemBuilder guildinfo = new ItemBuilder(Material.DRAGON_EGG).setTitle(Util.fixColor(Util.replaceString("&8->> " + MainColor + "GILDIA " + ImportantColor + guild.getTag() + SpecialSigns + " <<-")));

        int index  = 0;
        for(GuildShopItem item : Core.getGshopitems()){
            final ItemStack a = item.getItemStack();

            final ItemMeta meta = a.getItemMeta();
            meta.setLore(Collections.singletonList(Util.fixColor(Util.replaceString("  &8->> &7Koszt: &6" + item.getCost() + " &a" + "DUSZ"))));
            a.setItemMeta(meta);
            inv.setItem(index , a,(paramPlayer, paramInventory, paramInt, paramItemStack) -> {
                if(checkSouls(guild,item.getCost())){
                    ItemUtil.giveItems(Collections.singletonList(item.getItemStack()),player);
                }else{
                    Util.sendMsg(player, Util.replaceString(SpecialSigns + "->> " + MainColor  + "Nie posiadasz tyle dusz w swojej gidlii!"));
                }

            });
            index++;
        }

        inv.openInventory(player);
    }
    private static void openEffectShop(Player player, Guild g){
        final InventoryGUI inv = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(SpecialSigns + "->> [" + " &6&nEFFECT SHOP&8 " + SpecialSigns + "] <<-")),1);
        final ItemBuilder speed1 = new ItemBuilder(Material.FEATHER).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "* ->> " + MainColor + "EFEKT &bSPEED I " +  SpecialSigns + "<<- *")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Czas: " + ImportantColor + "8m")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Cena: " + ImportantColor + "800 dusz")));
        final ItemBuilder fr1 = new ItemBuilder(Material.MAGMA_CREAM).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "* ->> " + MainColor + "EFEKT &6FIRE RESISTANCE I " +  SpecialSigns + "<<- *")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Czas: " + ImportantColor + "8m")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Cena: " + ImportantColor + "650 dusz")));
        final ItemBuilder haste2 = new ItemBuilder(Material.DIAMOND_PICKAXE).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "* ->> " + MainColor + "EFEKT &e&lHASTE II " +  SpecialSigns + "<<- *")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Czas: " + ImportantColor + "2m")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Cena: " + ImportantColor + "1500 dusz")));
        inv.setItem(0, speed1.build(), (player1, inventory, i, itemStack) -> {
            if(checkSouls(g,800)){
                final GuildEffectPacket packet = new GuildEffectPacket(PotionEffectType.SPEED.getName(),8*60*20,0,g.getTag());
                RedisClient.sendSectorsPacket(packet);
            }else{
                Util.sendMsg(player1, Util.replaceString(SpecialSigns + "->> " + MainColor  + "Nie posiadasz tyle dusz w swojej gidlii!"));
            }
        });
        inv.setItem(1, fr1.build(), (player1, inventory, i, itemStack) -> {
            if(checkSouls(g,650)){
                final GuildEffectPacket packet = new GuildEffectPacket(PotionEffectType.FIRE_RESISTANCE.getName(),8*60*20,0,g.getTag());
                RedisClient.sendSectorsPacket(packet);
            }else{
                Util.sendMsg(player1, Util.replaceString(SpecialSigns + "->> " + MainColor  + "Nie posiadasz tyle dusz w swojej gidlii!"));
            }
        });
        inv.setItem(2, haste2.build(), (player1, inventory, i, itemStack) -> {
            if(checkSouls(g,1500)){
                final GuildEffectPacket packet = new GuildEffectPacket(PotionEffectType.FAST_DIGGING.getName(),2*60*20,1,g.getTag());
                RedisClient.sendSectorsPacket(packet);
            }else{
                Util.sendMsg(player1, Util.replaceString(SpecialSigns + "->> " + MainColor  + "Nie posiadasz tyle dusz w swojej gidlii!"));
            }
        });
        inv.openInventory(player);
    }
}
