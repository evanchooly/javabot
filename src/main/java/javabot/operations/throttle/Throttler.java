package javabot.operations.throttle;

import java.util.ArrayList;
import java.util.List;

public class Throttler<T extends ThrottleItem> {
    /**
     * Maximum throttle items to keep in memory
     */
    private final int maxThrottleItems; // 100;
    /**
     * Throttle time in millis
     */
    private final int throttleTime; //  = 20 * 1000; // 20 seconds.
    private final List<TimedThrottleItem> items;

    public Throttler(final int maxThrottleItems, final int throttleTime) {
        this.maxThrottleItems = maxThrottleItems;
        this.throttleTime = throttleTime;
        items = new ArrayList<TimedThrottleItem>(maxThrottleItems);
    }

    private class TimedThrottleItem {
        private final T ti;
        private long when;

        public TimedThrottleItem(final T ti) {
            this.ti = ti;
            when = System.currentTimeMillis();
        }

        public boolean matchT(final T t) {
            return ti.matches(t);
        }

        public long getWhen() {
            return when;
        }
    }

    /**
     * Check if an item is currently throttled or not.
     *
     * @return true if t is currently throttled and ought to be ignored, false otherwise.
     */
    public boolean isThrottled(final T t) {
        synchronized (items) {
            final long now = System.currentTimeMillis();
            for (final TimedThrottleItem ti : items) {
                if (now - ti.getWhen() < throttleTime && ti.matchT(t)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Add a new item to the throttle list.
     */
    public void addThrottleItem(final T t) {
        synchronized (items) {
            items.add(new TimedThrottleItem(t));
            while (items.size() > maxThrottleItems) {
                items.remove(0);
            }
        }
    }
}
