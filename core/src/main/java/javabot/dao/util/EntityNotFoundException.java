package javabot.dao.util;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Class clazz, Long id) {
        super("An object of type " + clazz + " with ID " + id + " does not exist.");
    }

    public EntityNotFoundException() {
        super();
    }

    public EntityNotFoundException(String arg0) {
        super(arg0);
    }
}