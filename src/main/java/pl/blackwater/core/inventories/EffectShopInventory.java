package pl.blackwater.core.inventories;

import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.Effect;
import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.EffectConfig;
import pl.blackwater.core.utils.ColoredMaterialsUtil;
import pl.blackwaterapi.gui.actions.InventoryGUI;
import pl.blackwaterapi.utils.ItemBuilder;
import pl.blackwaterapi.utils.RandomUtil;
import pl.blackwaterapi.utils.Util;

public class EffectShopInventory implements Colors {

    /*@Getter private static InventoryGUI effectShopInventory = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Kupowanie " + ImportantColor + "efektow")), 3);
            @Getter final BukkitTask bukkitTask = new BukkitRunnable() {
                @Override
                public void run() {
            final ItemBuilder itemBuilder1 = ColoredMaterialsUtil.getStainedGlassPane((short) RandomUtil.getRandInt(0, 15));
            for (int i = 9; i < 18; i++) {
                effectShopInventory.setItem(i - 9, itemBuilder1.build(), null);
                effectShopInventory.setItem(i + 9, itemBuilder1.build(), null);
            }
        }
    }.runTaskTimerAsynchronously(Core.getPlugin(), 20L, 60L);
    public EffectShopInventory(){
        int position = 9;
        for(Effect effect : EffectConfig.getEffectHashMap().values()){
            final ItemBuilder itemBuilder = new ItemBuilder(effect.getMaterial(),1).setTitle(Util.fixColor(Util.replaceString(effect.getName())));
            for(String lore : effect.getLore()){
                itemBuilder.addLore(Util.fixColor(Util.replaceString(lore.replace("{EFFECT}", effect.getPotionEffect().getType().getName()).replace("{COST}", String.valueOf(effect.getCost())))));
            }
            final ItemBuilder itemBuilder1 = ColoredMaterialsUtil.getStainedGlassPane((short) RandomUtil.getRandInt(0,15));
            effectShopInventory.setItem(position - 9, itemBuilder1.build(), null);
            effectShopInventory.setItem(position, itemBuilder.build(), (player, inventory, i, itemStack) -> {
                final User user = UserManager.getUser(player);
                if(user.getMoney() >= effect.getCost()){
                    user.removeMoney(effect.getCost());
                    player.addPotionEffect(effect.getPotionEffect(),true);
                    Util.sendMsg(player, Util.replaceString(SpecialSigns + "->> " + MainColor + "Pomyslnie zakupiono" + ImportantColor + " efekt"));
                }else{
                    Util.sendMsg(player, WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz wystarczajacej ilosci pieniedzy " + SpecialSigns + "(" + WarningColor + effect.getCost() + SpecialSigns + ")");
                }
            });
            effectShopInventory.setItem(position + 9, itemBuilder1.build(), null);
            position++;
        }
    }*/
    public void DisableAnimation(){

    }
}
