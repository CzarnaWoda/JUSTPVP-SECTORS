package pl.blackwater.core.events;

import lombok.NonNull;

public class BreakStoneCustomEvent extends CustomEvent{
    public BreakStoneCustomEvent(long time) {
        super("KOPACZOW", "Wykop jak najwiecej kamienia!", EventType.BREAK_STONE, time);
    }
}
