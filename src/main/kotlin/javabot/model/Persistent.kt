package javabot.model

import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.Id

public interface Persistent {
    @Id
    public var id: ObjectId?
}
