package javabot.model

import org.bson.types.ObjectId
import dev.morphia.annotations.Entity
import dev.morphia.annotations.Id
import dev.morphia.annotations.Indexed

import java.io.Serializable
import java.time.LocalDateTime

@Entity(value = "changes", noClassnameStored = true) class Change : Serializable, Persistent {

    @Id
    var id: ObjectId = ObjectId()
    lateinit var message: String
    @Indexed(name = "changed")
    var changeDate: LocalDateTime = LocalDateTime.now()

    private constructor() {
    }

    constructor(message: String, date: LocalDateTime = LocalDateTime.now()) {
        this.message = message
        this.changeDate = date
    }
}
