package javabot

import javabot.database.UpgradeScript

import java.util.Comparator

class ScriptComparator : Comparator<UpgradeScript> {
    override fun compare(o1: UpgradeScript, o2: UpgradeScript): Int {
        if (o1 === o2) {
            return 0
        }
        if (o1.id() < o2.id()) {
            return -1
        }
        if (o1.id() > o2.id()) {
            return 1
        }
        throw RuntimeException("%s and %s have the same priority: %d".format(o1.javaClass.name, o2.javaClass.name, o1.id()))
    }
}
