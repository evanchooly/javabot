package javabot.javadoc

import org.jboss.forge.roaster.model.TypeVariable
import org.mongodb.morphia.annotations.Embedded

@Embedded
class JavadocType {
    lateinit var name: String
    var bounds = mutableListOf<String>()

    constructor() { }

    constructor(type: TypeVariable<*>): this() {
        name = type.getName()
        type.getBounds().forEach {
            bounds.add(it.getQualifiedNameWithGenerics())
        }
    }

    override fun toString(): String{
        return "JavadocType(name='$name', bounds=$bounds)"
    }
}