package javabot.kotlin.web.views

import com.google.common.base.Charsets
import io.dropwizard.views.View

public class ErrorView(template: String, public val image: String) : View(template, Charsets.ISO_8859_1)
