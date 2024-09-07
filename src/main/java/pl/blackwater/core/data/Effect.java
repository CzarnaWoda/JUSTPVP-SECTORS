package pl.blackwater.core.data;

import lombok.Data;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;

import java.util.List;

@Data
public class Effect {

    private String id;
    @NonNull private String name;
    @NonNull private List<String> lore;
    @NonNull private Material material;
    @NonNull private int cost;
    @NonNull private PotionEffect potionEffect;

}
