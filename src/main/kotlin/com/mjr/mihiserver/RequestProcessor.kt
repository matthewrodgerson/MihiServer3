package com.mjr.mihiserver

import org.http4k.core.HttpHandler
import org.eclipse.jetty.server.Handler
import javax.servlet.http.HttpServlet
import org.http4k.core.Method
import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.then
import org.http4k.filter.DebuggingFilters.PrintRequest
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.SunHttp
import org.http4k.server.asServer
import org.http4k.servlet.asServlet
import java.io.BufferedInputStream
import java.text.SimpleDateFormat
import java.util.*

fun getHandler(): HttpServlet {
    val app: HttpHandler = routes(
        "/ping" bind GET to {
            SendToDatabase.writeData("Matthew", "some more test data 070222")
            Response(OK).body("pong")
        },
        "/addsessiondata" bind Method.POST to {
            val requestData = it.bodyString()
            val dataLines = requestData.lines()
            for (line in dataLines) {
                if (line != "") {
                    SendToDatabase.writeData("Matthew", line)
                }
            }
            Response(OK).body("Data successfully added to server")
        },
        "/pong" bind GET to {
            Response(OK).body("ping")
        },
        "" bind GET to {
            Response(OK).body("Hello!")
        }

    )
    return app.asServlet()
}

fun getConnection() {
//    val printingApp: HttpHandler = PrintRequest().then(app)
//    val server = printingApp.asServer(SunHttp(9000)).start()

    SendToDatabase.getConnection()

//    val sdf = SimpleDateFormat("yyyyMdd HH:mm:ss")
//    val timestamp = sdf.format(Date())
//    println("Server started on port " + server.port() + " " + timestamp)
//    println()
}