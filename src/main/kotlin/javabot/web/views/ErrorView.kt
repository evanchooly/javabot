package javabot.web.views

import com.google.common.base.Charsets
import io.dropwizard.views.common.View

class ErrorView(template: String, val image: String) :
    View(template, com.google.common.base.Charsets.ISO_8859_1)
