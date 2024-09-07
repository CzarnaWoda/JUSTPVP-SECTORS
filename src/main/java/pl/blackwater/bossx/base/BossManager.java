package pl.blackwater.bossx.base;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Giant;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import pl.blackwater.bossx.settings.BossConfig;
import pl.blackwater.core.Core;
import pl.blackwaterapi.utils.Util;


public class BossManager {
	public static void SpawnBoss(World w,Location loc,String name,int health){
		Giant boss = (Giant)w.spawnEntity(loc,EntityType.GIANT);
		boss.setCustomName(Util.fixColor(name));
		boss.setMaxHealth(health);
		boss.setHealth(health);
        boss.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 4));
        boss.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 999999, 4));
        boss.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999, 4));
        boss.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 15));
		Core.boss.put(boss.getUniqueId().toString(), health);
		System.out.println(BossConfig.messageconsole);
		if(BossConfig.broadcastenable){
			Bukkit.broadcastMessage(Util.fixColor(BossConfig.broadcastmessage)
						.replace("{X}", String.valueOf(loc.getBlockX()))
						.replace("{Y}", String.valueOf(loc.getBlockY()))
						.replace("{Z}", String.valueOf(loc.getBlockZ())));
		}
		if(BossConfig.soundenable){
			for(Player online : Bukkit.getOnlinePlayers()){
				online.playSound(online.getLocation(), Sound.IRONGOLEM_HIT, 2.0f, 1.0f);
			}
		}
	}
	public static void SpawnBabyZombie(World w, Location loc,String name,int health){
		Zombie zombie = (Zombie)w.spawnEntity(loc, EntityType.ZOMBIE);
		zombie.setCustomName(Util.fixColor(name));
		zombie.setBaby(true);
		zombie.setMaxHealth(health);
		zombie.setHealth(health);
        zombie.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
        zombie.getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        zombie.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
        zombie.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
        zombie.getEquipment().setItemInHand(new ItemStack(Material.IRON_SWORD));
        zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 4));
        zombie.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 1));
	}
	public static void SpawnBabyZombie(World w, Location loc,String name,int health,int amount){
		for(int i = 0;i<amount;i++){
		Zombie zombie = (Zombie)w.spawnEntity(loc, EntityType.ZOMBIE);
		zombie.setCustomName(Util.fixColor(name));
		zombie.setBaby(true);
		zombie.setMaxHealth(health);
		zombie.setHealth(health);
        zombie.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
        zombie.getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        zombie.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
        zombie.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
        zombie.getEquipment().setItemInHand(new ItemStack(Material.IRON_SWORD));
        zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 4));
        zombie.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 1));
		}
	}
}
