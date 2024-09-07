package pl.blackwater.core.events;

import lombok.Data;
import lombok.NonNull;
import pl.blackwater.core.data.User;

import java.util.HashMap;
import java.util.UUID;

@Data
public abstract class CustomEvent {


    @NonNull
    private String eventName;

    @NonNull
    private String eventDescription;

    @NonNull
    private EventType eventType;

    @NonNull
    private long eventTime;





    public void addStatForEvent(UUID uuid, int amount){
        CustomEventManager.getStatCount().merge(uuid, amount, Integer::sum);
        if(!CustomEventManager.getUuidEventList().contains(uuid)){
            CustomEventManager.getUuidEventList().add(uuid);
        }
    }
    public void removePlayerForEvent(UUID uuid) {
        if (CustomEventManager.getStatCount().get(uuid) != null){
            CustomEventManager.getStatCount().remove(uuid);
        }
    }




}
