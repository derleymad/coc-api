package com.example.network

import com.example.database.CategoriaRepository
import com.example.model.BuildingItem
import com.example.model.Item
import org.jsoup.Jsoup
import java.io.IOException
class GetItemRequest(private val categoriaRepository: CategoriaRepository) {
    //categoriaItem
    val items = mutableListOf<Item>()
    val baseUrl = "https://clashofclans.fandom.com"

    fun execute() : MutableList<Item>{
        val buildingItems = categoriaRepository.obterBuildingsItems()

        for (element in buildingItems) {

            val home = Jsoup.connect(baseUrl + element.link).get()

            try {
//                val main = home.title()
                val dps= home.body().getElementsByClass("ModifierStat DPS").first()?.text()
                //
////                val dps = main.get(0)
//                val dps = main.getElementsByClass("ModifierStat DPS").text()

                items.add(
                    Item(
                        parent = "",
                        title = "",
                        image = "",
                        status = dps.toString(),
                    )
                )

            } catch (e: IOException) {
                val message = e.message ?: "Error desconhecido"
                println(message)
            }
        }

        return items
    }
}


