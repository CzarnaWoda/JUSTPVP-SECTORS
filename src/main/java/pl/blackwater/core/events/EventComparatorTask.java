package pl.blackwater.core.events;

import org.bukkit.scheduler.BukkitRunnable;
import pl.justsectors.packets.impl.customevents.EndCustomEventPacket;
import pl.justsectors.redis.client.RedisClient;

public class EventComparatorTask extends BukkitRunnable {
    @Override
    public void run() {

        CustomEventManager.compareEventStats();


        if(CustomEventManager.getActiveEvent() != null && CustomEventManager.getActiveEvent().getEventTime() <= System.currentTimeMillis()){
            CustomEventManager.endEvent();
        }
    }
}
