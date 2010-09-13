package javabot.database;

import java.util.Comparator;

import javabot.Javabot;

public interface UpgradeScript {
    int id();

    void execute(Javabot bot);

    class ScriptComparator implements Comparator<UpgradeScript> {
        @Override
        public int compare(final UpgradeScript o1, final UpgradeScript o2) {
            if(o1.id() < o2.id()) {
                return -1;
            } else if(o1.id() > o2.id()) {
                return 1;
            }
            throw new RuntimeException(String.format("%s and %s have the same priority: %d",
                o1.getClass().getName(), o2.getClass().getName(), o1.id()));
        }
    }
}
