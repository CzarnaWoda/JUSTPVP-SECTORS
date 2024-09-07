package pl.blackwater.core.managers;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;

import lombok.Getter;
import pl.blackwater.core.data.Combat;

public class CombatManager {
	
	@Getter private static final ConcurrentHashMap<Player, Combat> combats = new ConcurrentHashMap<>();
	
	public static Combat getCombat(final Player player) {
		return combats.get(player);
	}
	public static void CreateCombat(final Player player) {
		final Combat combat = new Combat(player);
		combats.put(player, combat);
	}
	public static void removeCombat(final Player player) {
		combats.remove(player);
	}

	public static void removeFight(final Combat c) {
		if(c != null) {
			c.setLastAttactkPlayer(null);
			c.setLastAttactTime(0L);
		}
	}
}
