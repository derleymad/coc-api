package com.example.plugins

import com.example.database.CategoriaRepository
import com.example.model.Categoria
import com.example.model.Categorias
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jsoup.Jsoup
import java.io.IOException
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
            call.respond("Welcome to CoC API")
        }
        get("/teste"){
            val repoCatItens = categoriaRepository.obterCategoriasItem()
            call.respond(repoCatItens)
        }

        get("/categories/{item}"){
            val repoCatItens = categoriaRepository.obterCategoriasItem()
            val titlePath = call.parameters["item"]
            val categoria = repoCatItens.find{it.title==titlePath}
            println(repoCatItens.toString())
            println("o titulo éeeeeeee"+titlePath)
            println(categoria)
            if(categoria !=null){
                call.respond(categoria)
            }else{
                call.respond("Item não encontrada")
            }
        }
        get("/categories"){
            call.respond(categoriaRepository.obterCategorias())
        }
    }
}