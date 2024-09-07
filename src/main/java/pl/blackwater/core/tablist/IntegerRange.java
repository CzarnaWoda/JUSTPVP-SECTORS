package pl.blackwater.core.tablist;

import java.util.*;

public class IntegerRange
{
    private int minRange;
    private int maxRange;

    public IntegerRange(final int minRange, final int maxRange) {
        this.minRange = minRange;
        this.maxRange = maxRange;
    }

    public int getMinRange() {
        return this.minRange;
    }

    public int getMaxRange() {
        return this.maxRange;
    }

    public static <V> V inRange(final int value, final Map<IntegerRange, V> rangeMap) {
        for (final Map.Entry<IntegerRange, V> entry : rangeMap.entrySet()) {
            final IntegerRange range = entry.getKey();
            if (value >= range.getMinRange() && value <= range.getMaxRange()) {
                return entry.getValue();
            }
        }
        return null;
    }
}
