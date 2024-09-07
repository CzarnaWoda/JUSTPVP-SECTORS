package pl.blackwater.core.managers;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import lombok.Getter;

public class AntyMacroManager {
	
	@Getter public static HashMap<UUID, Integer> clickCount = new HashMap<>();
	@Getter public static HashMap<Player, Integer> notifyCount = new HashMap<>();
}
