package javabot.model

import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Id
import org.mongodb.morphia.annotations.Indexed

import java.io.Serializable
import java.time.LocalDateTime

@Entity(value = "changes", noClassnameStored = true)
public class Change(var message: String?) : Serializable, Persistent {

    @Id
    var id: ObjectId? = null
    @Indexed(name = "changed")
    var changeDate = LocalDateTime.now()

    public constructor() : this(null) {

    }
}
