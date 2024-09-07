package pl.blackwater.guilds.data;

import lombok.Data;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;

@Data
public class GuildEffect {

    @NonNull
    private String key;
    @NonNull
    private Material material;
    @NonNull
    private short data;
    @NonNull
    private PotionEffect potionEffect;

}
