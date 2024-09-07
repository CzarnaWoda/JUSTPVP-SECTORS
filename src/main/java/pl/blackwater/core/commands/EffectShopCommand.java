package pl.blackwater.core.commands;

import org.bukkit.entity.Player;
import pl.blackwater.core.inventories.EffectShopInventory;
import pl.blackwaterapi.commands.PlayerCommand;

public class EffectShopCommand extends PlayerCommand {

    public EffectShopCommand() {
        super("effect", "Sklep efektow", "/effect", "core.effect", "efekty","efektyshop","efekt","efect","sklepefektow");
    }


    @Override
    public boolean onCommand(Player player, String[] strings) {
        //EffectShopInventory.getEffectShopInventory().openInventory(player);
        return false;
    }
}
