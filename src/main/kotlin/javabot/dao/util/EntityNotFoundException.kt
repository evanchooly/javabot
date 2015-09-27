package javabot.dao.util

import org.bson.types.ObjectId

public class EntityNotFoundException<T>(clazz: Class<T>, id: ObjectId?) :
      RuntimeException("An object of type $clazz with ID $id does not exist.")