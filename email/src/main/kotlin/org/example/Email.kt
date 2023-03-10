package org.example

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

const val servicePort = 8081

fun main() {
    embeddedServer(Netty, port = servicePort, host = "0.0.0.0", module = Application::module).start(wait = true)
}

fun Application.module() {
    routing {

        post("/paid") {
            println("Received message:\n${call.receiveText()}")
            println("Sending E-Mail: Dear customer, you have paid. Thanks")
            call.respond(HttpStatusCode.OK)
        }

        post("/shipped") {
            println("Received message:\n${call.receiveText()}")
            println("Sending E-Mail: Dear customer, we have shipped your order. Thanks")
            call.respond(HttpStatusCode.OK)
        }
    }
}