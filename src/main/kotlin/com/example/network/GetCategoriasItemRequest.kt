package com.example.network

import com.example.database.CategoriaRepository
import com.example.model.BuildingItem
import org.jsoup.Jsoup
import java.io.IOException


class GetCategoriasItemRequest(private val categoriaRepository: CategoriaRepository) {
    //categoriaItem
    val buildingItems = mutableListOf<BuildingItem>()
    val baseUrl = "https://clashofclans.fandom.com"
    fun execute() : MutableList<BuildingItem>{
        val categories = categoriaRepository.obterBuildings()

        for (element in categories) {


            val home = Jsoup.connect(baseUrl + element.link).get()

            try {
                val main = home.getElementsByClass("mw-parser-output")[0]

                //for images ,links and titles
                val imagesDataSet = mutableSetOf<String>()
                val linksDataSet = mutableSetOf<String>()
                val titlesDataSet = mutableSetOf<String>()

                main.getElementsByTag("img").forEachIndexed { index, element ->
                    //imagem random está no index 00
                    //a imagem da building esta no index 01

                    if(index>=2){
                        //primeiras duas imagens sem js tem links diferentes
                        if(index<=3){
                            //titles
                            val parentTitle = element.parent()?.attr("title")
                            if(!parentTitle.isNullOrEmpty()){
                                titlesDataSet.add(parentTitle)
                            }
                            //links
                            val parentLink = element.parent()?.attr("href")
                            if (!parentLink.isNullOrEmpty()) {
                                linksDataSet.add(parentLink)
                            }
                            //images
                            val image = element.getElementsByAttribute("src").attr("src")
                            imagesDataSet.add(image)
                        }else{
                            //titles
                            val parentTitle = element.parent()?.attr("title")
                            if(!parentTitle.isNullOrEmpty()){
                                titlesDataSet.add(parentTitle)
                            }

                            //links
                            val parentLink = element.parent()?.attr("href")
                            if (!parentLink.isNullOrEmpty()) {
                                linksDataSet.add(parentLink)
                            }
                            //images
                            val image = element.getElementsByAttribute("data-src").attr("data-src")
                            imagesDataSet.add(image)
                        }
                    }
                }

                // Verifica se todas as listas têm o mesmo tamanho
                if (linksDataSet.size == imagesDataSet.size && imagesDataSet.size == titlesDataSet.size) {
                    for (i in linksDataSet.indices) {
                        val parent = element.title
                        val link = linksDataSet.toList()[i]
                        val image = imagesDataSet.toList()[i]
                        val title = titlesDataSet.toList()[i]

                        val buildingItem = BuildingItem(parent, title, image, link)
                        buildingItems.add(buildingItem)
                    }
                } else {
                    println("As listas não têm o mesmo tamanho.")
                }
            } catch (e: IOException) {
                val message = e.message ?: "Error desconhecido"
                println(message)
            }
        }

        //Adicionar no repositorio todos as buildings
        if(categoriaRepository.obterBuildingsItems().isEmpty()){
            buildingItems.forEach { i->
                categoriaRepository.adicionarBuildingItem(i)
            }
        }else{
        }

        return buildingItems
    }
}


