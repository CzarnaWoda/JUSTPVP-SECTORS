package pl.blackwaterapi.utils;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.potion.PotionEffectType;

@EqualsAndHashCode(callSuper = true)
@Data
public class ActionBarMessageEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	@NonNull private final Player player;
	@NonNull private String message;
	private boolean cancelled = false;

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

}