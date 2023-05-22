package com.example

import com.example.database.CategoriaRepository
import com.example.model.Building
import com.example.model.BuildingItem
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.testing.*
import kotlin.test.*
import io.ktor.http.*
import com.example.plugins.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ApplicationTest {

    @Test
    fun testRoot() = testApplication {
        val categoriasRepository = CategoriaRepository()
        categoriasRepository.init()

        application {
            categories(categoriasRepository)
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Bem vindo a CoC API", bodyAsText())

        }
    }
    @Test
    fun testBuildings() = testApplication {
        val categoriasRepository = CategoriaRepository()
        categoriasRepository.init()
        application {
            categories(categoriasRepository)
        }
        client.get("/Buildings").apply {
            val BUILDING_SIZE = 11
            assertEquals(HttpStatusCode.OK, status)
            val gson = Gson()
            val buildingListType = object : TypeToken<List<Building>>() {}.type
            val buildings: List<Building> = gson.fromJson(bodyAsText(), buildingListType)
            assertEquals(BUILDING_SIZE, buildings.size)
        }
    }
    @Test
    fun testBuildingItemAll() = testApplication {
        val categoriasRepository = CategoriaRepository()
        categoriasRepository.init()
        application {
            categories(categoriasRepository)
        }
        client.get("/Buildings_Items").apply {
            val BUILDING_SIZE = 105
            assertEquals(HttpStatusCode.OK, status)
            val gson = Gson()
            val buildingListType = object : TypeToken<List<BuildingItem>>() {}.type
            val buildings: List<Building> = gson.fromJson(bodyAsText(), buildingListType)
            assertEquals(BUILDING_SIZE, buildings.size)
        }
    }
}