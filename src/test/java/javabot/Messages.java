package javabot;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

@Singleton
public class Messages implements Iterable<String> {
    List<String> messages = new ArrayList<>();

    public boolean isEmpty() {
        return messages.isEmpty();
    }

    public void add(final String message) {
        messages.add(message);
    }

    public int size() {
        return messages.size();
    }

    public String remove(final int index) {
        return messages.remove(index);
    }

    public List<String> get() {
        List<String> list = new ArrayList<>(messages);
        messages.clear();
        return list;
    }

    public String get(final int index) {
        return messages.get(index);
    }

    @Override
    public Iterator<String> iterator() {
        return messages.iterator();
    }

    @Override
    public void forEach(final Consumer<? super String> action) {
        messages.forEach(action);
    }

    @Override
    public Spliterator<String> spliterator() {
        return messages.spliterator();
    }

    @Override
    public String toString() {
        return messages.toString();
    }
}
