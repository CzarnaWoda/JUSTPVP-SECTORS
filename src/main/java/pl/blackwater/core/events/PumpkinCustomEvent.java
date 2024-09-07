package pl.blackwater.core.events;

import lombok.NonNull;

public class PumpkinCustomEvent extends CustomEvent{
    public PumpkinCustomEvent(long time) {
        super("ZBIERANIE DUSZ", "Zbieraj dynie na mapie!", EventType.PUMPKIN, time);
    }
}
