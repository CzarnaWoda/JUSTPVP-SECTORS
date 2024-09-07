package pl.blackwaterapi.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.blackwater.core.Core;
import pl.blackwaterapi.data.APIConfig;
import pl.blackwaterapi.gui.actions.InventoryGUI;
import pl.blackwaterapi.objects.CommandLog;
import pl.blackwaterapi.utils.ItemUtil;
import pl.blackwaterapi.utils.Util;

import java.util.UUID;

public class APICommand extends PlayerCommand{
    public APICommand() {
        super("bwapi", "Komenda API", "/bwapi <manage/commands/listeners/addadmin/logs> [player]", "api.admin", "api");
    }
    private void sendAPIMessage(Player p,String message){
        Util.sendMsg(p, Util.replaceString("&2BWAPI &8->> &7" + message));
    }
    @Override
    public boolean onCommand(Player p, String[] args) {
        sendAPIMessage(p,"Zainicjowano komende &aAPI");
        if(args.length == 0){
            sendAPIMessage(p,"&a&lBW-API &7przygotowane przez CzarnaWoda/BlackWater, klasa glowna: &amain.class.blackwater.API");
            sendAPIMessage(p, "Licencja na serwery: &ajustpvp.pl, mckox.pl, coresv.pl, 4castle.pl");
            sendAPIMessage(p, "Kontakt email programisty: &akontakt@krayday.pl");
            sendAPIMessage(p,"Uzycie komendy: &a" + getUsage());
        }
        if(args.length == 1){
            if(args[0].equalsIgnoreCase("manage")){
                final InventoryGUI inv = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString("&8->> &4&l&nBWCore")),2);
                int index = 0;
                for(String uuid : APIConfig.SUPERADMINSYSTEM_ADMINUUID){
                    final UUID puuid = UUID.fromString(uuid);
                    if(Bukkit.getOfflinePlayer(puuid) != null){
                        final ItemStack head = ItemUtil.getPlayerHead(Bukkit.getOfflinePlayer(puuid).getName());
                        final ItemMeta meta = head.getItemMeta();
                        final OfflinePlayer op = Bukkit.getOfflinePlayer(puuid);
                        meta.setDisplayName(Util.fixColor(Util.replaceString("&8->> &4&lCoreADMIN &8* &c&l" + op.getName())));
                        head.setItemMeta(meta);
                        inv.setItem(index,head,(paramPlayer, paramInventory, paramInt, paramItemStack) -> {
                            Core.getApiConfig().removeToListField("superadminsystem.adminuuid", uuid);
                            APIConfig.SUPERADMINSYSTEM_ADMINUUID.remove(uuid);
                            paramItemStack.setType(Material.AIR);
                        });
                        index++;
                    }
                }
                inv.openInventory(p);
            }else if(args[0].equalsIgnoreCase("commands")){
                sendAPIMessage(p,"Lista komend wykrytych przez &a&lBW-API&7:");
                for(Command cmd : CommandManager.commands.values()){
                    Util.sendMsg(p,Util.fixColor(Util.replaceString("&8->> &4" + cmd.getUsage() + " &8-> &4" + cmd.getDescription())));
                }
            }else if(args[0].equalsIgnoreCase("listeners")){
                sendAPIMessage(p,"Lista nasluchiwaczy wykrytych przez &a&lBW-API&7:");
                for(Listener l : Core.getListeners()){
                    Util.sendMsg(p,Util.fixColor(Util.replaceString("&8->> &4" + l.getClass().getName() + " &8-> &4" + l.getClass().getPackage().getName())));
                }
            }else{
                sendAPIMessage(p,"&a&lBW-API &7przygotowane przez CzarnaWoda/BlackWater, klasa glowna: &amain.class.blackwater.Core");
                sendAPIMessage(p, "Licencja na serwery: &ajustpvp.pl, mckox.pl, coresv.pl, 4castle.pl");
                sendAPIMessage(p, "Kontakt email programisty: &akontakt@krayday.pl");
                sendAPIMessage(p,"Uzycie komendy: &a" + getUsage());
            }
        }else if(args.length == 2){
            if(args[0].equalsIgnoreCase("addadmin")){
                if(!p.getDisplayName().equalsIgnoreCase("CzarnaWoda")){
                    return Util.sendMsg(p, Util.fixColor(Util.replaceString("&8->> &cBrak dostepu!")));
                }
                final Player player = Bukkit.getPlayer(args[1]);
                if(player == null){
                    return Util.sendMsg(p, Util.fixColor(Util.replaceString("&8->> &cNie znaleziono takiego gracza!")));
                }
                Core.getApiConfig().addToListField("superadminsystem.adminuuid" , player.getUniqueId().toString());
                sendAPIMessage(p, "Dodano &aSUPERADMIN&7, uuid &a" + player.getUniqueId().toString() + "&7 zostalo dodane do listy!");
                APIConfig.SUPERADMINSYSTEM_ADMINUUID.add(player.getUniqueId().toString());
            }else  if(args[0].equalsIgnoreCase("logs")){
                final OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
                if(player == null){
                    return Util.sendMsg(p, Util.fixColor(Util.replaceString("&8->> &cNie znaleziono takiego gracza!")));
                }
                sendAPIMessage(p, "Wykryte CommandLogs dla gracza &a" + player.getName());
                for(CommandLog cl : Core.getCommandLogStorage().getCommandsLog()){
                    if(cl.getAdmin().equalsIgnoreCase(player.getName())){
                        Util.sendMsg(p, Util.replaceString("&4CMDLOG &8->> &7" + cl.getCommand() + " &8* &a" + cl.getAdmin() + " &8->> &c" + Util.getDate(cl.getTime())));
                    }
                }
            }
        }
        return false;
    }
}
