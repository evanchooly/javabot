package javabot.commands

import java.lang.annotation.ElementType.FIELD
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy.RUNTIME

Retention(RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation public class Param(public val name: String = "", public val required: Boolean = true, public val primary: Boolean = false, public val defaultValue:

String = "")
