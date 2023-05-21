package com.example.network

import com.example.database.CategoriaRepository
import com.example.model.BuildingItem
import org.jsoup.Jsoup
import java.io.IOException


class GetCategoriasItemRequest(private val categoriaRepository: CategoriaRepository) {

    //categoriaItem
    val baseUrl = "https://clashofclans.fandom.com"
    fun execute() {
        val categories = categoriaRepository.obterBuildings()

        for (element in categories) {

            val titlesDataSet = mutableSetOf<String>()

            val home = Jsoup.connect(baseUrl + element.link).get()

            try {
                val main = home.getElementsByClass("mw-parser-output")[0]


                //for images
                val imagesDataSet = mutableSetOf<String>()
                main.getElementsByTag("img").forEachIndexed { index, element ->
                    //imagem random está no index 00
                    //a imagem da building esta no index 01
                    if(index>=2){
                        //primeiras duas imagens sem js tem links diferentes
                        if(index<=3){
                            val image = element.getElementsByAttribute("src").attr("src")
                            imagesDataSet.add(image)
                        }else{
                            val image = element.getElementsByAttribute("data-src").attr("data-src")
                            imagesDataSet.add(image)
                        }
                    }
                }

                //for links
                val linksDataSet = mutableSetOf<String>()
//                main.getElementsByTag("a").forEachIndexed { index, element ->
//                   val link = element.getElementsByAttribute("href").attr("href")
//                   linksDataSet.add(link)
//                }
                val hrElements = main.getElementsByTag("hr")
                println(hrElements)
                val startHr = hrElements[0]
                val endHr = hrElements[1]

                val anchorElements = home.select("a")
                val startIdx = anchorElements.indexOf(startHr.nextElementSibling())
                val endIdx = anchorElements.indexOf(endHr)

                val list  = mutableListOf<String>()
                anchorElements.forEach {
                    if(it.getElementsByAttribute("id").attr("id").isNullOrEmpty() && it.getElementsByAttribute("class").attr("class").isNullOrEmpty()){
                        list.add(it.toString())
                    }
                }

                list.forEach {
                    if(it.contains("Cannon")){
                        println(it)
                    }
                }

                for (i in startIdx until endIdx) {
                    val anchor = anchorElements[i]
                    // Aplicar a manipulação desejada aos elementos <a>
                    linksDataSet.add(anchor.text())
//                    anchor.addClass("selected")
                }

//                println(doc.html())

//                val links = home.getElementsByClass("mw-parser-output")[0].getElementsByTag("a")[2].getElementsByAttribute("href").attr("href")

                println(linksDataSet.size)
                println(linksDataSet)
//                println(links)
//                home.getElementsByClass("mw-parser-output")[0].getElementsByTag("a")[1].getElementsByAttribute("href").attr("src")
//                val linkHome =
//                    home.getElementsByClass("flexbox-display bold-text hovernav")[0].getElementsByTag("div")
//                val imagesHome =
//                    home.getElementsByClass("flexbox-display bold-text hovernav")[0].getElementsByTag("img")
//
//                imagesHome.forEachIndexed { index, element ->
//                    if (index <= 1) {
//                        imagesDataSet.add(element.getElementsByAttribute("src").attr("src").toString())
//                    } else {
//                        imagesDataSet.add(element.getElementsByAttribute("data-src").attr("data-src").toString())
//                    }
//                }
//                linksDataSet.addAll(linkHome.select("div div div>a").eachAttr("href"))
//                linksDataSet.removeAll(listOf(""))
//                titlesDataSet.addAll(linkHome.select("div div div>a").eachText())
//
//                println(linksDataSet.size.toString()+imagesDataSet.size.toString()+titlesDataSet.size.toString())
//                println(linksDataSet)
//                println(imagesDataSet)
//                println(titlesDataSet)
//

//                if(imagesDataSet.size==linksDataSet.size && linksDataSet.size ==titlesDataSet.size) {
//
//                    val categoriasList = mutableListOf<BuildingItem>()
//                    for (i in 0 until imagesDataSet.size) {
//                        categoriasList.add(
//                            BuildingItem(
//                                parent = element.parent,
//                                image = imagesDataSet.toList().get(i),
//                                link = linksDataSet.toList().get(i),
//                                title = titlesDataSet.toList().get(i)
//                            )
//                        )
//                    }
//                    println(categoriasList)
//                    //adicionando categorias no repository
////                    val categories = Categorias(categoriasList)
////                    for(i in categories.categorias){
////                        categoriaRepository.adicionarCategoriaItem(i.image,i.link,i.title)
////                    }
////                }
//                }



            } catch (e: IOException) {
                val message = e.message ?: "Error desconhecido"
            }
        }
    }
}


