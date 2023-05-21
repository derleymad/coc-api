package com.example.plugins

import com.example.database.CategoriaRepository
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.text.DateFormat

fun Application.categories(categoriaRepository: CategoriaRepository) {
    install(ContentNegotiation) {
        gson {
            setDateFormat(DateFormat.LONG)
            setPrettyPrinting()
        }
    }

    routing {
        get("/") {
            call.respondText("<!doctype html>\n" +
                    "<html>\n" +
                    "  <head>\n" +
                    "    <title>CoC API</title>\n" +
                    "  </head>\n" +
                    "  <body>\n" +
                    "    <p>Welcome to<strong>CoC API</strong><br> routes, paths, and API documentation will be added soon on  <strong>Github Project Page</strong></p>\n" +
                    "  </body>\n" +
                    "</html>",contentType = ContentType.Text.Html
            )
        }

        get("/Buildings/{parent}"){
            val parent = call.parameters["parent"]?.replace("_"," ")

            if(parent!=null){
                val buildings = categoriaRepository.getBuildingsByParent(parent)
                if(buildings.isNotEmpty()){
                    call.respond(buildings)
                }else{
                    call.respond("Item not found")
                }
            }else{
                call.respond("No path was added")
            }
        }

        get("/Buildings"){
            call.respond(categoriaRepository.obterBuildings())
        }
    }
}