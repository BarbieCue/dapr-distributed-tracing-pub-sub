package org.example

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

const val servicePort = 8084
const val sidecarPort = 3504
const val pubSubName = "pub-sub"
const val topic = "on_stock"
const val httpTraceHeader = "traceparent"

fun main() {
    embeddedServer(Netty, port = servicePort, host = "0.0.0.0", module = Application::module).start(wait = true)
}

fun Application.module() {
    routing {

        post("/paid") {
            val client = HttpClient(CIO){
                install(ContentNegotiation) {
                    json(Json {
                        prettyPrint = true
                    })
                }
            }

            val message = "{\"orderId\": \"123\"}"
            val response = client.post("http://localhost:${sidecarPort}/v1.0/publish/$pubSubName/$topic") {
                contentType(ContentType.Application.Json)
                header(httpTraceHeader, call.request.header(httpTraceHeader))
                setBody(message)
            }

            val info = "Stock Service: Published $message on topic $topic (${response.status})"
            println(info)

            call.respond(HttpStatusCode.OK)
        }
    }
}