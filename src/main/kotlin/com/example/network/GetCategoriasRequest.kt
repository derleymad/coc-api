package com.example.network

import com.example.database.CategoriaRepository
import com.example.model.Categoria
import com.example.model.Categorias
import org.jsoup.Jsoup
import java.io.IOException


class GetCategoriasRequest(private val categoriaRepository: CategoriaRepository){
    val urlHome = "https://clashofclans.fandom.com/wiki/Clash_of_Clans_Wiki"
    val imagesDataSet= mutableSetOf<String>()
    val linksDataSet= mutableSetOf<String>()


    val titles= mutableSetOf<String>()

    val home = Jsoup.connect(urlHome).get()


    //categoriaItem
    val baseUrl = "https://clashofclans.fandom.com"


    fun execute(){
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
            for(i in 0 until 3){
                categoriasList.add(Categoria(
                    image = imagesDataSet.elementAt(i),
                    link = linksDataSet.elementAt(i),
                    title = titleDataSet.elementAt(i)
                ))
            }

            //adicionando categorias no repository
            val categories = Categorias(categoriasList)
            for(i in 0 until 3){
                val j = categories.categorias[i]
                categoriaRepository.adicionarCategoria(j.image,j.link,j.title)
            }

            GetCategoriasItemRequest(categoriaRepository).execute()

        } catch (e: IOException){
            val message = e.message ?: "Error desconhecido"
        }
    }
}


