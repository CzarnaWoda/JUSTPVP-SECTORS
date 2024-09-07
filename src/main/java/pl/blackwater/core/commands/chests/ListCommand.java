package pl.blackwater.core.commands.chests;

import org.bukkit.entity.Player;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.TreasureChest;
import pl.blackwater.core.enums.ChestType;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.Util;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ListCommand extends PlayerCommand implements Colors {
    public ListCommand() {
        super("list", "Sprawdza ilosc skrzynek z danego typu", "/chest list [(optional)type]", "core.chest");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        Collection<TreasureChest> chests = Core.getTreasureChestManager().getChests().values();
        AtomicInteger static_chest = new AtomicInteger();
        AtomicInteger unlimited_chest = new AtomicInteger();
        AtomicInteger normal_chest = new AtomicInteger();
        chests.forEach(treasureChest -> {
            if (treasureChest.getType().equals(ChestType.STATIC)) {
                static_chest.getAndIncrement();
            } else {
                if (treasureChest.getType().equals(ChestType.UNLIMITED)) {
                    unlimited_chest.getAndIncrement();
                } else {
                    normal_chest.getAndIncrement();
                }
            }
        });
        List<String> message = Arrays.asList(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + MainColor + "Lista skrzyn na" + ImportantColor + " JustPvP.PL" + SpecialSigns + " <<-")),
                Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "STATIC: " + ImportantColor + static_chest.get())),
                Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "UNLIMITED: " + ImportantColor + unlimited_chest.get())),
                Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "AUTO-GENERATE: " + ImportantColor + normal_chest.get())),
                Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + MainColor + "Lista skrzyn na" + ImportantColor + " JustPvP.PL" + SpecialSigns + " <<-")));
        message.forEach(m -> Util.sendMsg(player, m));
        return false;
    }
}
