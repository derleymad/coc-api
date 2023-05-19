package com.example.network

import com.example.database.CategoriaRepository
import com.example.model.Categoria
import com.example.model.Categorias
import org.jsoup.Jsoup
import java.io.IOException


class GetCategoriasItemRequest(private val categoriaRepository: CategoriaRepository) {
    val imagesDataSet = mutableSetOf<String>()
    val linksDataSet = mutableSetOf<String>()
    val titles = mutableSetOf<String>()

    //categoriaItem
    val baseUrl = "https://clashofclans.fandom.com"


    fun execute() {
        val categories = categoriaRepository.obterCategorias()

        for (element in categories) {
            val home = Jsoup.connect(baseUrl + element.link).get()
            try {
                val linkHome =
                    home.getElementsByClass("flexbox-display bold-text hovernav")[0].getElementsByTag("div")
                val imagesHome =
                    home.getElementsByClass("flexbox-display bold-text hovernav")[0].getElementsByTag("img")

                imagesHome.forEachIndexed { index, element ->
                    if (index <= 1) {
                        imagesDataSet.add(element.getElementsByAttribute("src").attr("src").toString())
                    } else {
                        imagesDataSet.add(element.getElementsByAttribute("data-src").attr("data-src").toString())
                    }
                }
                linksDataSet.addAll(linkHome.select("div div div>a").eachAttr("href"))
                linksDataSet.removeAll(listOf(""))
                titles.addAll(linkHome.select("div div div>a").eachText())

//                    Log.i("categoriaTeste",linksDataSet.size.toString()+imagesDataSet.size.toString()+titles.size.toString())


                if(imagesDataSet.size==linksDataSet.size && linksDataSet.size ==titles.size){

                    val categoriasList = mutableListOf<Categoria>()
                    for(i in 0 until imagesDataSet.size){
                        categoriasList.add(Categoria(
                            image = imagesDataSet.elementAt(i),
                            link = linksDataSet.elementAt(i),
                            title = titles.elementAt(i)
                        ))
                    }
                    //adicionando categorias no repository
                    val categories = Categorias(categoriasList)
                    for(i in categories.categorias){
                        categoriaRepository.adicionarCategoriaItem(i.image,i.link,i.title)
                    }
                }



            } catch (e: IOException) {
                val message = e.message ?: "Error desconhecido"
            }
        }
    }
}


