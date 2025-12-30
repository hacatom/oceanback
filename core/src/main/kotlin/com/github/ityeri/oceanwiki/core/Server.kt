package com.github.ityeri.oceanwiki.core

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respondText
import io.ktor.http.HttpStatusCode
import io.ktor.server.routing.Routing
import io.ktor.server.routing.routing
import org.slf4j.LoggerFactory

class Server {
    private val logger = LoggerFactory.getLogger(Server::class.java)
    private val routeConfigurations = mutableListOf<Routing.() -> Unit>()

    fun addRoutes(block: Routing.() -> Unit): Server {
        routeConfigurations.add(block)
        return this
    }

    fun run() {
        val server = embeddedServer(CIO, port = 8000) {
            install(ContentNegotiation) {
                json()
            }
            install(StatusPages) {
                exception<Throwable> { call, cause ->
                    logger.error("Unhandled exception: ${cause.message}", cause)
                    call.respondText(
                        text = "Internal Server Error: ${cause.message}", // For production, use a more generic message
                        status = HttpStatusCode.InternalServerError
                    )
                }
            }
            routing {
                routeConfigurations.forEach { it() }
            }
        }
        server.start(wait = true)
    }
}
