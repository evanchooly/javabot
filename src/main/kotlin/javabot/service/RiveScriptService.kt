package javabot.service

import com.google.inject.Singleton
import com.rivescript.Config
import com.rivescript.RiveScript

@Singleton
class RiveScriptService:RiveScript(Config.utf8()) {
}