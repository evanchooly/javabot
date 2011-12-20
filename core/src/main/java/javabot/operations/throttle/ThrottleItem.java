package javabot.operations.throttle;

public interface ThrottleItem<T extends ThrottleItem> {
    /** Check if this item matches the other item
     * @return true if t matches this item, false otherwise.
     */
    boolean matches (T t);
}
