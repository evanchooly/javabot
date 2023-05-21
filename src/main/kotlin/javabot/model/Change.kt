package javabot.model

import dev.morphia.annotations.Entity
import dev.morphia.annotations.Id
import dev.morphia.annotations.IndexOptions
import dev.morphia.annotations.Indexed
import java.io.Serializable
import java.time.LocalDateTime
import org.bson.types.ObjectId

@Entity(value = "changes", useDiscriminator = false)
class Change : Serializable, Persistent {
    @Id var id: ObjectId = ObjectId()
    lateinit var message: String

    @Indexed(options = IndexOptions(name = "changed"))
    var changeDate: LocalDateTime = LocalDateTime.now()

    constructor() {}

    constructor(message: String, date: LocalDateTime = LocalDateTime.now()) {
        this.message = message
        this.changeDate = date
    }
}
