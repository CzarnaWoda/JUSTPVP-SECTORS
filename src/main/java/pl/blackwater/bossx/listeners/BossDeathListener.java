package pl.blackwater.bossx.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import pl.blackwater.bossx.settings.BossConfig;
import pl.blackwater.bossx.settings.RewardsConfig;
import pl.blackwater.bossx.settings.RewardsStorage;
import pl.blackwater.core.Core;
import pl.blackwaterapi.utils.ActionBarUtil;
import pl.blackwaterapi.utils.Util;

public class BossDeathListener implements Listener
{
    @EventHandler
    public void OnBossDead(EntityDeathEvent e) {
        if (e.getEntityType().equals(EntityType.GIANT)){
            Player p = e.getEntity().getKiller();
        	if (p != null){
        		if (e.getEntity().getCustomName().equals(Util.fixColor(BossConfig.bossname))){
	                Bukkit.broadcastMessage(Util.fixColor(BossConfig.bossdeathmessage.replace("{PLAYER}", p.getDisplayName())));
	                for (Player players : Bukkit.getOnlinePlayers()){
	                    players.playSound(players.getLocation(), Sound.GHAST_DEATH, 2.0f, 1.0f);
	                }
	                List<ItemStack> list = new ArrayList<ItemStack>();
	                for(RewardsStorage rewards : RewardsConfig.rewardsStroage.values()){
	                	list.add(rewards.getWithAll());
	                }
	                Random r = new Random();
	                int i = r.nextInt(list.size());
	                if (list.get(i) != null) {
	                    HashMap<Integer, ItemStack> notStored = (HashMap<Integer, ItemStack>)p.getInventory().addItem(new ItemStack[] { (ItemStack)list.get(i) });
                        for (ItemStack is : notStored.values()) {
                            p.getWorld().dropItemNaturally(p.getLocation(), is);
                        }
	                    Core.boss.remove(e.getEntity().getUniqueId().toString());
	                }
	    		    ActionBarUtil.sendActionBar(p, Util.fixColor(BossConfig.bosshealthactionbar
	    		    			.replace("{FULLHP}", String.valueOf(BossConfig.bosshealth))
	    		    			.replace("{HP}", "0")));
        		}
            }
        }
    }
}
