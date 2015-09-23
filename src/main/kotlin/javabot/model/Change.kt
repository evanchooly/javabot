package javabot.model

import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Id
import org.mongodb.morphia.annotations.Indexed

import java.io.Serializable
import java.time.LocalDateTime

Entity(value = "changes", noClassnameStored = true)
public class Change : Serializable, Persistent {

    Id
    private var id: ObjectId? = null
    public var message: String? = null
    Indexed(name = "changed")
    public var changeDate: LocalDateTime? = null

    public constructor() {
    }

    public constructor(message: String) {
        this.message = message
    }

    override fun getId(): ObjectId {
        return id
    }

    override fun setId(id: ObjectId) {
        this.id = id
    }
}
