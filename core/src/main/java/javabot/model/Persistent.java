package javabot.model;

import org.bson.types.ObjectId;

public interface Persistent {
    ObjectId getId();

    void setId(ObjectId id);
}
