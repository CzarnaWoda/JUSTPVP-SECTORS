package pl.blackwater.core.commands;

import lombok.Getter;
import lombok.Setter;
import net.lightshard.itemcases.ItemCases;
import net.lightshard.itemcases.cases.ItemCase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.blackwater.core.Core;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.settings.FanPageConfig;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.ItemUtil;
import pl.blackwaterapi.utils.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;


public class FanPageCommand extends PlayerCommand implements Colors {
    public FanPageCommand() {
        super("fanpage", "Daje nagrode za polubienie fanpage justpvp.pl", "/fanpage", "core.fanpage", "fb","facebook","fp","nagroda");
    }
    @Getter
    @Setter
    private static boolean fanpage;
    private static HashMap<UUID, Long> times;

    static {
        times = new HashMap<>();
    }
    @Override
    public boolean onCommand(Player player, String[] strings) {
        if(!fanpage){
            return Util.sendMsg(player, Util.fixColor(Util.replaceString(WarningColor + "Blad: " + WarningColor_2 + "Komenda zostala tymczasowo wylaczona!")));
        }
        if(FanPageConfig.uuidlist.contains(player.getUniqueId().toString())){
            return Util.sendMsg(player, WarningColor + "Blad: " + WarningColor_2 + "Odebrales juz swoja nagrode!");
        }
        final Long t = times.get(player.getUniqueId());
        if (t != null && System.currentTimeMillis() - t < 15000L) {
            return Util.sendMsg(player, WarningColor + "Blad" + SpecialSigns + ": " + WarningColor_2 + "Komendy mozesz uzyc raz na 15s!");
        }
        Bukkit.getScheduler().runTaskAsynchronously(Core.getPlugin(), ()-> {
            try {
                URL url = new URL("https://vps.hidey.eu/justpvp/check.php?name=" + player.getName());
                HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
                httpcon.addRequestProperty("User-Agent", "Mozilla/4.76");
                BufferedReader in = null;
                try {
                    in = new BufferedReader(new InputStreamReader(httpcon.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String inputLine;
                try {
                    while ((inputLine = in.readLine()) != null) {
                        if (inputLine.contains("true")) {
                            Bukkit.broadcastMessage(Util.fixColor(Util.replaceString("&8->> &4&lGratulacje! &7Gracz &6&l" + player.getDisplayName() + " &apolubił nasz fanpage &7i otrzymał nagrode w postaci &akluczy&7! &2&n/fanpage")));


                            ItemCase itemCase = ItemCases.getInstance().getCaseManager().fromName("ChestPvP");
                            ItemStack key = itemCase.getKey();
                            key.setAmount(2);
                            ItemUtil.giveItems(Collections.singletonList(key),player);
                            FanPageConfig fanPageConfig = new FanPageConfig();
                            fanPageConfig.addUUIDtoList(player.getUniqueId());
                            FanPageConfig.uuidlist.add(player.getUniqueId().toString());
                            return;
                        }

                        if (inputLine.contains("false")) {
                            Util.sendMsg(player, Util.replaceString("&8->> &7Aby otrzymac nagrode:"));
                            Util.sendMsg(player, Util.replaceString("&8->> &71. &aWejdz na fanpage fb.com/justpvppl"));
                            Util.sendMsg(player, Util.replaceString("&8->> &71. &6&lZalajkuj strone"));
                            Util.sendMsg(player, Util.replaceString("&8->> &72. &aNapisz wiadomosc &2kluczyk#" + player.getDisplayName()));
                            Util.sendMsg(player, Util.replaceString("&8->> &73. &aWpisz ponownie komende /fanpage"));
                            return;
                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return false;
    }
}
