package com.example.network

import com.example.database.CategoriaRepository
import com.example.model.Building
import com.example.model.Categoria
import org.jsoup.Jsoup
import java.io.IOException

class GetCategoriasRequest(private val categoriaRepository: CategoriaRepository){
    val urlHome = "https://clashofclans.fandom.com/wiki/Clash_of_Clans_Wiki"
    val mergedList = mutableListOf<Categoria>()

    fun execute() : MutableList<Categoria>{
        try {
            val home = Jsoup.connect(urlHome).get()
            //FOR HOME PAGE OF WIKI
            //Categorie Title
            val listLaneBanner =home.body().getElementsByClass("LaneBanner")
            val categoriesTitles = listLaneBanner.eachText()
            categoriesTitles.removeAt(0)//The first one is about clash blog

            //For url of images
            var sizeOfAll = 0

            //3
           val listOftables =home.body().getElementsByClass("lcs-container")[0].getElementsByTag("table")
           val categorias = listOftables.subList(0,4)

            //for images
            val listOfImagesDataSet = mutableListOf<List<String>>()
            categorias.forEach{
                val imagesDataSet= mutableSetOf<String>()
                it.getElementsByClass("floatnone").forEach {
                    imagesDataSet.add(it.getElementsByTag("img")[0].getElementsByAttribute("data-src").attr("data-src"))
                }
                listOfImagesDataSet.add(imagesDataSet.toList())
            }
            listOfImagesDataSet.removeAt(listOfImagesDataSet.size-1)

            //for links
            val listOfLinksDataSet = mutableListOf<List<String>>()
            categorias.forEach{
                val linksDataSet= mutableSetOf<String>()
                it.getElementsByClass("TitleBlock-Link").forEach {
                    linksDataSet.add(it.getElementsByAttribute("href").attr("href"))
                }
                listOfLinksDataSet.add(linksDataSet.toList())
            }
            listOfLinksDataSet.removeAt(listOfLinksDataSet.size-1)

            //for titles
            val listOfTitleDataSet = mutableListOf<List<String>>()
            categorias.forEach {
                val titleDataSet = mutableListOf<String>()
                 it.select(".TitleBlock-Title").forEach {
                    titleDataSet.add(it.text())
                     sizeOfAll++
                }
                listOfTitleDataSet.add(titleDataSet)
            }
            listOfTitleDataSet.removeAt(listOfTitleDataSet.size-1)

//            println(categoriesTitles)
//            println(listOfLinksDataSet)
//            println(listOfImagesDataSet)
//            println(listOfTitleDataSet)

            // Criando a lista final de Categoria com as Building correspondentes

            val minSize = minOf(categoriesTitles.size, listOfLinksDataSet.size, listOfImagesDataSet.size, listOfTitleDataSet.size)

            for (i in 0 until minSize) {
                val categoryTitle = categoriesTitles[i]
                val buildingList = mutableListOf<Building>()

                val linkList = listOfLinksDataSet[i]
                val imageList = listOfImagesDataSet[i]
                val titleList = listOfTitleDataSet[i]

                val minBuildingSize = minOf(linkList.size, imageList.size, titleList.size)

                for (j in 0 until minBuildingSize) {
                    val parentCategory = categoryTitle
                    val buildingTitle = titleList[j]
                    val buildingImage = imageList[j]
                    val buildingLink = linkList[j]

                    val building = Building(parentCategory, buildingTitle, buildingImage, buildingLink)
                    buildingList.add(building)
                }

                val category = Categoria(categoryTitle, buildingList)
                mergedList.add(category)
            }

            //Adicionar no repositorio todos as buildings
            if(categoriaRepository.obterBuildings().isEmpty()){
                mergedList.forEach { cat->
                    cat.buildings.forEach { building->
                        categoriaRepository.adicionarBuilding(building)
                    }
                }
            }else{
            }
            GetCategoriasItemRequest(categoriaRepository).execute()
        } catch (e: IOException){
            val message = e.message ?: "Error desconhecido"
            println(message)
        }
        return mergedList
    }
}


