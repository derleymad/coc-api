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

fun Application.categories() {
    install(ContentNegotiation) {
        gson {
            setDateFormat(DateFormat.LONG)
            setPrettyPrinting()
        }
    }

    val urlHome = "https://clashofclans.fandom.com/wiki/Clash_of_Clans_Wiki"
    val imagesDataSet= mutableSetOf<String>()
    val linksDataSet= mutableSetOf<String>()

    routing {
        get("/") {
            call.respond("Welcome to CoC API")
        }
        get("/categories/{item}"){
            if(call.parameters["item"]=="admin"){
                call.respond("é admn")
            }
        }

        get("/teste"){
            call.respond(CategoriaRepository().obterCategorias())
        }

        get("/categories") {

            val home = Jsoup.connect(urlHome).get()
            try {
                val urlHome = home.body().getElementsByClass("floatnone")
                val urlToLinks = home.body().getElementsByClass("TitleBlock-Link")
                val titleDataSet = home.body().getElementsByClass("TitleBlock-Title").eachText()

                for (i in urlToLinks) {
                    val link = i.getElementsByAttribute("href").attr("href")
                    linksDataSet.add(link.toString())
                }

                for (i in urlHome) {
                    val imageLink = i.getElementsByTag("img")[0].getElementsByAttribute("data-src").attr("data-src")
                    imagesDataSet.add(imageLink.toString())
                }
                imagesDataSet.remove("")

                val categoriasList = mutableListOf<Categoria>()
                for(i in 0 until titleDataSet.size){
                    categoriasList.add(Categoria(
                        image = imagesDataSet.elementAt(i),
                        link = linksDataSet.elementAt(i),
                        title = titleDataSet.elementAt(i)
                    ))
                }

                val categorias = Categorias(categoriasList)
               // println(categorias.toString())
                for(i in categorias.categorias){
                    CategoriaRepository().adicionarCategoria(i.image,i.link,i.title)
                }

                call.respond(categorias)

            }catch (e: IOException){
                val message = e.message ?: "Error desconhecido"
                call.respond(message)
            }
        }
    }
}