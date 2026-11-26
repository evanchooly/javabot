package javabot.web.resources

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import javabot.Javabot
import javabot.JavabotConfig
import javabot.dao.AdminDao
import javabot.dao.ApiDao
import javabot.dao.ChannelDao
import javabot.dao.ConfigDao
import javabot.model.Admin
import javabot.model.ApiEvent
import javabot.model.Channel
import javabot.model.javadoc.JavadocApi
import javabot.web.model.User
import javabot.web.views.ViewFactory
import javax.inject.Inject
import org.bson.types.ObjectId

class AdminResource
@Inject
constructor(
    var viewFactory: ViewFactory,
    var adminDao: AdminDao,
    var apiDao: ApiDao,
    var configDao: ConfigDao,
    var channelDao: ChannelDao,
    var javabot: Javabot,
    var config: JavabotConfig,
) {

    fun configureRoutes(routing: Routing) {
        with(routing) {
            route("/admin") {
                get {
                    val user =
                        getAuthenticatedUser(call)
                            ?: run {
                                val view = PublicErrorResource.view403()
                                call.respond(FreeMarkerContent(view.template, view.toModel()))
                                return@get
                            }

                    val current = adminDao.getAdminByEmailAddress(user.email)
                    if (current == null) {
                        val view = PublicErrorResource.view403()
                        call.respond(FreeMarkerContent(view.template, view.toModel()))
                    } else {
                        val view =
                            viewFactory.createAdminIndexView(
                                KtorServletRequest(call),
                                current,
                                Admin(),
                            )
                        call.respond(FreeMarkerContent("main.ftl", view.toModel()))
                    }
                }

                get("/config") {
                    val user =
                        getAuthenticatedUser(call)
                            ?: run {
                                call.respond(HttpStatusCode.Forbidden)
                                return@get
                            }
                    adminDao.getAdminByEmailAddress(user.email)
                        ?: run {
                            call.respond(HttpStatusCode.Forbidden)
                            return@get
                        }
                    val view = viewFactory.createConfigurationView(KtorServletRequest(call))
                    call.respond(FreeMarkerContent("main.ftl", view.toModel()))
                }

                get("/javadoc") {
                    val user =
                        getAuthenticatedUser(call)
                            ?: run {
                                call.respond(HttpStatusCode.Forbidden)
                                return@get
                            }
                    adminDao.getAdminByEmailAddress(user.email)
                        ?: run {
                            call.respond(HttpStatusCode.Forbidden)
                            return@get
                        }
                    val view = viewFactory.createJavadocAdminView(KtorServletRequest(call))
                    call.respond(FreeMarkerContent("main.ftl", view.toModel()))
                }

                get("/newChannel") {
                    val user =
                        getAuthenticatedUser(call)
                            ?: run {
                                call.respond(HttpStatusCode.Forbidden)
                                return@get
                            }
                    adminDao.getAdminByEmailAddress(user.email)
                        ?: run {
                            call.respond(HttpStatusCode.Forbidden)
                            return@get
                        }
                    val view =
                        viewFactory.createChannelEditView(KtorServletRequest(call), Channel())
                    call.respond(FreeMarkerContent("main.ftl", view.toModel()))
                }

                get("/editChannel/{channel}") {
                    val user =
                        getAuthenticatedUser(call)
                            ?: run {
                                call.respond(HttpStatusCode.Forbidden)
                                return@get
                            }
                    val channelName =
                        call.parameters["channel"]
                            ?: run {
                                call.respond(HttpStatusCode.BadRequest)
                                return@get
                            }

                    adminDao.getAdminByEmailAddress(user.email)
                        ?: run {
                            call.respond(HttpStatusCode.Forbidden)
                            return@get
                        }
                    val channel =
                        channelDao.get(channelName)
                            ?: run {
                                call.respond(HttpStatusCode.NotFound)
                                return@get
                            }
                    val view = viewFactory.createChannelEditView(KtorServletRequest(call), channel)
                    call.respond(FreeMarkerContent("main.ftl", view.toModel()))
                }

                post("/saveChannel") {
                    val user =
                        getAuthenticatedUser(call)
                            ?: run {
                                call.respond(HttpStatusCode.Forbidden)
                                return@post
                            }
                    adminDao.getAdminByEmailAddress(user.email)
                        ?: run {
                            call.respond(HttpStatusCode.Forbidden)
                            return@post
                        }

                    val params = call.receiveParameters()
                    val id = params["id"]
                    val name = params["name"] ?: ""
                    val key = params["key"] ?: ""
                    val logged = params["logged"]?.toBoolean() ?: false

                    val channel =
                        if (id == null) Channel(name, key, logged)
                        else Channel(ObjectId(id), name, key, logged)
                    channelDao.save(channel)

                    val current = adminDao.getAdminByEmailAddress(user.email)!!
                    val view =
                        viewFactory.createAdminIndexView(KtorServletRequest(call), current, Admin())
                    call.respond(FreeMarkerContent("main.ftl", view.toModel()))
                }

                post("/saveConfig") {
                    val user =
                        getAuthenticatedUser(call)
                            ?: run {
                                call.respond(HttpStatusCode.Forbidden)
                                return@post
                            }
                    adminDao.getAdminByEmailAddress(user.email)
                        ?: run {
                            call.respond(HttpStatusCode.Forbidden)
                            return@post
                        }

                    val params = call.receiveParameters()
                    val config = configDao.get()
                    config.server = params["server"] ?: config.server
                    config.url = params["url"] ?: config.url
                    config.port = params["port"]?.toIntOrNull() ?: config.port
                    config.historyLength =
                        params["historyLength"]?.toIntOrNull() ?: config.historyLength
                    config.trigger = params["trigger"] ?: config.trigger
                    config.nick = params["nick"] ?: config.nick
                    config.password = params["password"] ?: config.password
                    config.throttleThreshold =
                        params["throttleThreshold"]?.toIntOrNull() ?: config.throttleThreshold
                    config.minimumNickServAge =
                        params["minimumNickServAge"]?.toIntOrNull() ?: config.minimumNickServAge
                    configDao.save(config)

                    val view = viewFactory.createConfigurationView(KtorServletRequest(call))
                    call.respond(FreeMarkerContent("main.ftl", view.toModel()))
                }

                get("/enableOperation/{name}") {
                    val user =
                        getAuthenticatedUser(call)
                            ?: run {
                                call.respond(HttpStatusCode.Forbidden)
                                return@get
                            }
                    val name =
                        call.parameters["name"]
                            ?: run {
                                call.respond(HttpStatusCode.BadRequest)
                                return@get
                            }

                    adminDao.getAdminByEmailAddress(user.email)
                        ?: run {
                            call.respond(HttpStatusCode.Forbidden)
                            return@get
                        }
                    javabot.enableOperation(name)
                    val view = viewFactory.createConfigurationView(KtorServletRequest(call))
                    call.respond(FreeMarkerContent("main.ftl", view.toModel()))
                }

                get("/disableOperation/{name}") {
                    val user =
                        getAuthenticatedUser(call)
                            ?: run {
                                call.respond(HttpStatusCode.Forbidden)
                                return@get
                            }
                    val name =
                        call.parameters["name"]
                            ?: run {
                                call.respond(HttpStatusCode.BadRequest)
                                return@get
                            }

                    adminDao.getAdminByEmailAddress(user.email)
                        ?: run {
                            call.respond(HttpStatusCode.Forbidden)
                            return@get
                        }
                    javabot.disableOperation(name)
                    val view = viewFactory.createConfigurationView(KtorServletRequest(call))
                    call.respond(FreeMarkerContent("main.ftl", view.toModel()))
                }

                get("/edit/{id}") {
                    val user =
                        getAuthenticatedUser(call)
                            ?: run {
                                call.respond(HttpStatusCode.Forbidden)
                                return@get
                            }
                    val id =
                        call.parameters["id"]
                            ?: run {
                                call.respond(HttpStatusCode.BadRequest)
                                return@get
                            }

                    val current =
                        adminDao.getAdminByEmailAddress(user.email)
                            ?: run {
                                call.respond(HttpStatusCode.Forbidden)
                                return@get
                            }

                    val view =
                        viewFactory.createAdminIndexView(
                            KtorServletRequest(call),
                            current,
                            adminDao.find(ObjectId(id)),
                        )
                    call.respond(FreeMarkerContent("main.ftl", view.toModel()))
                }

                get("/delete/{id}") {
                    val user =
                        getAuthenticatedUser(call)
                            ?: run {
                                call.respond(HttpStatusCode.Forbidden)
                                return@get
                            }
                    val id =
                        call.parameters["id"]
                            ?: run {
                                call.respond(HttpStatusCode.BadRequest)
                                return@get
                            }

                    adminDao.getAdminByEmailAddress(user.email)
                        ?: run {
                            call.respond(HttpStatusCode.Forbidden)
                            return@get
                        }
                    val admin = adminDao.find(ObjectId(id))
                    if (admin != null && (!admin.botOwner)) {
                        adminDao.delete(admin)
                    }

                    val current = adminDao.getAdminByEmailAddress(user.email)!!
                    val view =
                        viewFactory.createAdminIndexView(KtorServletRequest(call), current, Admin())
                    call.respond(FreeMarkerContent("main.ftl", view.toModel()))
                }

                post("/add") {
                    val user =
                        getAuthenticatedUser(call)
                            ?: run {
                                call.respond(HttpStatusCode.Forbidden)
                                return@post
                            }

                    val params = call.receiveParameters()
                    val ircName = params["ircName"] ?: ""
                    val hostName = params["hostName"] ?: ""
                    val emailAddress = params["emailAddress"] ?: ""

                    var admin: Admin? = adminDao.getAdminByEmailAddress(emailAddress)
                    if (admin == null) {
                        admin = Admin(ircName, emailAddress, hostName, true)
                    } else {
                        admin.ircName = ircName
                        admin.hostName = hostName
                        admin.emailAddress = emailAddress
                    }
                    adminDao.save(admin)

                    val current = adminDao.getAdminByEmailAddress(user.email)!!
                    val view =
                        viewFactory.createAdminIndexView(KtorServletRequest(call), current, Admin())
                    call.respond(FreeMarkerContent("main.ftl", view.toModel()))
                }

                post("/addApi") {
                    val user =
                        getAuthenticatedUser(call)
                            ?: run {
                                call.respond(HttpStatusCode.Forbidden)
                                return@post
                            }

                    adminDao.getAdminByEmailAddress(user.email)
                        ?: run {
                            call.respond(HttpStatusCode.Forbidden)
                            return@post
                        }

                    val params = call.receiveParameters()
                    val name = params["name"]
                    val groupId = params["groupId"]
                    val artifactId = params["artifactId"]
                    val version = params["version"]

                    version?.let {
                        val apiName =
                            name
                                ?: artifactId
                                ?: run {
                                    call.respond(HttpStatusCode.BadRequest)
                                    return@post
                                }
                        val api =
                            JavadocApi(config, apiName, groupId ?: "", artifactId ?: "", version)
                        apiDao.save(api)
                        apiDao.save(ApiEvent.add(user.email, api))
                    }

                    val view = viewFactory.createJavadocAdminView(KtorServletRequest(call))
                    call.respond(FreeMarkerContent("main.ftl", view.toModel()))
                }

                get("/deleteApi/{id}") {
                    val user =
                        getAuthenticatedUser(call)
                            ?: run {
                                call.respond(HttpStatusCode.Forbidden)
                                return@get
                            }
                    val id =
                        call.parameters["id"]
                            ?: run {
                                call.respond(HttpStatusCode.BadRequest)
                                return@get
                            }

                    adminDao.getAdminByEmailAddress(user.email)
                        ?: run {
                            call.respond(HttpStatusCode.Forbidden)
                            return@get
                        }
                    apiDao.delete(ObjectId(id))

                    val view = viewFactory.createJavadocAdminView(KtorServletRequest(call))
                    call.respond(FreeMarkerContent("main.ftl", view.toModel()))
                }

                get("/reloadApi/{id}") {
                    val user =
                        getAuthenticatedUser(call)
                            ?: run {
                                call.respond(HttpStatusCode.Forbidden)
                                return@get
                            }
                    val id =
                        call.parameters["id"]
                            ?: run {
                                call.respond(HttpStatusCode.BadRequest)
                                return@get
                            }

                    adminDao.getAdminByEmailAddress(user.email)
                        ?: run {
                            call.respond(HttpStatusCode.Forbidden)
                            return@get
                        }
                    apiDao.find(ObjectId(id))?.let { apiDao.save(ApiEvent.reload(user.email, it)) }

                    val view = viewFactory.createJavadocAdminView(KtorServletRequest(call))
                    call.respond(FreeMarkerContent("main.ftl", view.toModel()))
                }
            }
        }
    }

    private fun getAuthenticatedUser(call: ApplicationCall): User? {
        // TODO: Implement proper authentication
        return null
    }
}
