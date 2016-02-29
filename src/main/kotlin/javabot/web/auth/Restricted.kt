package javabot.web.auth

import javabot.web.model.Authority
import javabot.web.model.Authority.ROLE_ADMIN

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Restricted(vararg val value: Authority = arrayOf(ROLE_ADMIN))
