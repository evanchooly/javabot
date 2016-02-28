package javabot.dao.util

import org.bson.types.ObjectId

class EntityNotFoundException(clazz: Class<*>, id: ObjectId?) :
      RuntimeException("An object of type $clazz with ID $id does not exist.")