package pl.blackwater.core.events;

import pl.blackwaterapi.utils.Util;

public class KillCustomEvent extends CustomEvent{

    public KillCustomEvent(long time) {
        super("SMIERTELNE ZNIWA", "Event polega na zabijaniu graczy, kto zabije wiecej tej wygrywa! Bierz miecz w swoje rece i walcz!", EventType.KILLS, time);
    }


}
