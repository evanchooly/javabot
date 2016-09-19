package javabot.javadoc;

import javabot.model.Persistent
import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.Field
import org.mongodb.morphia.annotations.Index
import org.mongodb.morphia.annotations.IndexOptions
import org.mongodb.morphia.annotations.Indexes
import java.util.Date

@Indexes(
    Index(fields = arrayOf(Field("created")), options = IndexOptions(expireAfterSeconds = 7200))
)
class JavadocSource(): Persistent {

    constructor(name: String, api: JavadocApi, text: String): this() {
        this.api = api.id
        this.text = text
        this.name = name
    }

    lateinit var name: String
    lateinit var api: ObjectId
    var created = Date()
    lateinit var text: String
    var processed = false
}
