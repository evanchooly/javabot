package javabot.model

import org.bson.types.ObjectId

public interface Persistent {
    public fun getId(): ObjectId

    public fun setId(id: ObjectId)
}
