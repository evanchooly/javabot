package javabot;

import javabot.database.UpgradeScript;

import java.util.Comparator;

public class ScriptComparator implements Comparator<UpgradeScript> {
    @Override
    public int compare(final UpgradeScript o1, final UpgradeScript o2) {
        if (o1 == o2) {
            return 0;
        }
        if (o1.id() < o2.id()) {
            return -1;
        }
        if (o1.id() > o2.id()) {
            return 1;
        }
        throw new RuntimeException(String.format("%s and %s have the same priority: %d",
                                                 o1.getClass().getName(), o2.getClass().getName(), o1.id()));
    }
}
