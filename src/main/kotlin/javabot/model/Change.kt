package javabot.model

import org.bson.types.ObjectId
import xyz.morphia.annotations.Entity
import xyz.morphia.annotations.Id
import xyz.morphia.annotations.Indexed

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
