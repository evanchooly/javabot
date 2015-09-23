package javabot.dao.util

import org.bson.types.ObjectId

public class EntityNotFoundException : RuntimeException {
    public constructor(clazz: Class<Any>, id: ObjectId) : super("An object of type $clazz with ID $id does not exist.") {
    }

    public constructor() : super() {
    }

    public constructor(arg0: String) : super(arg0) {
    }
}