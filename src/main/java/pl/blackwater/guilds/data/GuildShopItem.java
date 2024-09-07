package pl.blackwater.guilds.data;

import lombok.Data;
import lombok.NonNull;
import org.bukkit.inventory.ItemStack;

@Data
public class GuildShopItem {

    @NonNull
    private ItemStack itemStack;
    @NonNull
    private int cost;
}
