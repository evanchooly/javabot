package javabot.dao.util;

import org.bson.types.ObjectId;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Class clazz, ObjectId id) {
        super("An object of type " + clazz + " with ID " + id + " does not exist.");
    }

    public EntityNotFoundException() {
        super();
    }

    public EntityNotFoundException(String arg0) {
        super(arg0);
    }
}