package pl.blackwater.core.events;

import lombok.NonNull;

public class OpenChestCustomEvent extends CustomEvent{
    public OpenChestCustomEvent(long time) {
        super("OTWIERACZ", "Otwieraj skrzynie na mapie!", EventType.OPEN_CHEST, time);
    }
}
