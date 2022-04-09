package com.mjr.mihiserver

import org.http4k.core.HttpHandler
import org.eclipse.jetty.server.Handler
import javax.servlet.http.HttpServlet
import org.http4k.core.Method
import org.http4k.core.ContentType
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
import java.io.OutputStream
import com.opencsv.CSVWriter
import org.eclipse.jetty.server.HttpChannel
import org.eclipse.jetty.server.HttpOutput
import org.eclipse.jetty.server.HttpWriter
import org.http4k.routing.header
import org.http4k.routing.path
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.InputStream
import java.io.OutputStreamWriter
import java.io.Writer
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
            var org = it.headerValues("ORGANISATION")[0]
            if (org == null) {org = "UNKNOWN"}
            val dataLines = requestData.lines()
            for (line in dataLines) {
                if (line != "") {
                    SendToDatabase.writeData(org, line)
                }
            }
            Response(OK).body("Data successfully added to server")
        },
        "/getdata" bind GET to {
            val extract = SendToDatabase.getData()
            val outputList = mutableListOf<String>()
            while (extract.next()) {
                val org = extract.getString(2)
                val ts2 = extract.getTimestamp(3)
                val data = extract.getString(4)
                outputList += "$org,${ts2.toString()},$data"
            }
            val response = Response(OK).body(outputList.joinToString(System.lineSeparator())).header("content-type", ContentType.TEXT_CSV.toHeaderValue())


            response
        },
        "/pong" bind GET to {
            Response(OK).body("ping")
        },
        "" bind GET to {
            val index = FileInputStream("./src/main/resources/index.html")
            Response(OK).body(index)
        },
        "/images/{image}" bind GET to {
            val index = FileInputStream("./images/"+it.path("image"))
            Response(OK).body(index)
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