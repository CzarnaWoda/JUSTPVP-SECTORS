package pl.blackwater.core.events;

import java.util.Comparator;
import java.util.UUID;

public class EventStatComparator implements Comparator<UUID> {
    @Override
    public int compare(UUID o1, UUID o2) {
        final Integer i = CustomEventManager.getStatCount().get(o1);
        final Integer i1 = CustomEventManager.getStatCount().get(o2);
        return i1.compareTo(i);
    }
}
