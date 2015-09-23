package javabot.kotlin.web.auth

import javabot.kotlin.web.model.Authority

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Restricted(vararg val value: Authority = arrayOf(Authority.ROLE_ADMIN))
