package pl.blackwater.bossx.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Giant;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import pl.blackwater.bossx.base.BossManager;
import pl.blackwater.bossx.settings.BossConfig;
import pl.blackwater.core.Core;
import pl.blackwaterapi.utils.ActionBarUtil;
import pl.blackwaterapi.utils.Util;

public class BossDamageListener implements Listener{
    public List<Entity> getNearbyEntites(Location l, int size) {
        List<Entity> entities = new ArrayList<Entity>();
        for (Entity ent : l.getWorld().getEntities()) {
            if (l.distance(ent.getLocation()) <= size) {
                entities.add(ent);
            }
        }
        return entities;
    }
    
    @EventHandler
    public void OnDamageBoss(EntityDamageEvent e) {
        if (e.getEntity().getType().equals((Object)EntityType.GIANT)) {
            Giant giant = (Giant)e.getEntity();
            Location loc = e.getEntity().getLocation();
            if (Core.boss.containsKey(e.getEntity().getUniqueId().toString())) {
                int health = Core.boss.get(e.getEntity().getUniqueId().toString());
                Random rand = new Random();
                int randomh = rand.nextInt(4);
                int damage = Integer.valueOf((int) e.getDamage());
                int hp0 = Core.boss.get(e.getEntity().getUniqueId().toString()) - damage;
                int hp = hp0 - randomh;
                Core.boss.put(e.getEntity().getUniqueId().toString(), hp);
                e.setDamage(0);
                if (health >= 0) {
                    for (Entity ent : this.getNearbyEntites(e.getEntity().getLocation(), 20)) {
                        if (ent instanceof Player) {
                            Player p = ((Player)ent).getPlayer();
                            if (health >= 1895 && health <= 1900) {
                                Util.sendMsg(p, BossConfig.messageprefix + " &6Rico Kabuuuuuum ");
                                p.getLocation().getWorld().createExplosion(loc, 2.0f);
                            }
                            else if (health >= 1795 && health <= 1800) {
                                Util.sendMsg(p, BossConfig.messageprefix + "&6Zeus, jeb*** piorunem! ");
                                p.getLocation().getWorld().strikeLightning(loc);
                            }
                            else if (health >= 1692 && health <= 1700) {
                                p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 30, 25));
                                Util.sendMsg(p, BossConfig.messageprefix + "&6Choroba morska?");
                            }
                            else if (health >= 1592 && health <= 1600) {
                                Util.sendMsg(p, BossConfig.messageprefix + "&6Rico, Kabuuuuuuuum ?");
                                p.getLocation().getWorld().createExplosion(loc, 2.0f);
                            }
                            else if (health >= 1395 && health <= 1400) {
                                giant.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 30, 20));
                                Util.sendMsg(p, BossConfig.messageprefix + "&6A gdzie ja teraz jestem ;>?");
                            }
                            else if (health >= 1192 && health <= 1200) {
                                p.setVelocity(p.getLocation().getDirection().multiply(-5));
                                Util.sendMsg(p, BossConfig.messageprefix + "&6Jebuuuuuuuuuuuuuud!");
                                p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 10, 30));
                                Util.sendMsg(p, BossConfig.messageprefix + "&6Truuuuteeeczkaaa ?");
                            }
                            else if (health >= 992 && health <= 1000) {
                                p.setVelocity(p.getLocation().getDirection().multiply(-4));
                                Util.sendMsg(p, BossConfig.messageprefix + "&6Jebuuuuuuuuuuuuuud!");
                                p.setFoodLevel(0);
                                Util.sendMsg(p, BossConfig.messageprefix + "&6Glodno wszedzie !");
                            }
                            else if (health >= 895 && health <= 900) {
                                p.setVelocity(p.getLocation().getDirection().multiply(5));
                                Util.sendMsg(p, BossConfig.messageprefix + "&6Jebuuuuuuuuuuuuuud!");
                            }
                            else if (health >= 695 && health <= 700) {
                                p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20, 5));
                                Util.sendMsg(p, BossConfig.messageprefix + "&6Co taki oslabiony jestes??");
                            }
                            else if (health >= 495 && health <= 500) {
                                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30, 20));
                                Util.sendMsg(p, BossConfig.messageprefix + "&6Widzisz cos ;> ?");
                            }
                            else if (health >= 395 && health <= 400) {
                                p.setVelocity(p.getLocation().getDirection().multiply(6));
                                Util.sendMsg(p, BossConfig.messageprefix + "&6Jebuuuuuuuuuuuuuud!");
                            }
                            else if (health >= 295 && health <= 300) {
                                Util.sendMsg(p, BossConfig.messageprefix + "&6Rico, Kabuuuuuuuum !");
                            }
                            else if (health >= 90 && health <= 100) {
                                p.setVelocity(p.getLocation().getDirection().multiply(6));
                                Util.sendMsg(p, BossConfig.messageprefix + "&6Jebuuuuuuuuuuuuuud!");
                                loc.getWorld().strikeLightning(p.getLocation());
                                BossManager.SpawnBabyZombie(p.getWorld(), loc, "&c&lSluga", 200, 5);
                                Util.sendMsg(p, BossConfig.messageprefix + "&6Umieeeeeeeraaaaaaaaaaaaaam!");
                            }
            	    		    ActionBarUtil.sendActionBar(p, Util.fixColor(BossConfig.bosshealthactionbar
            	    		    			.replace("{FULLHP}", String.valueOf(BossConfig.bosshealth))
            	    		    			.replace("{HP}", String.valueOf(Core.boss.get(e.getEntity().getUniqueId().toString())))));
                    }
                    }
                }
                else {
                    giant.setHealth(0);
                }
            }
        }
    }
}
