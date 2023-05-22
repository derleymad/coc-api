package com.example.model

import com.example.database.CategoriaRepository
import com.example.network.GetCategoriasItemRequest
import com.example.network.GetCategoriasRequest
import com.example.plugins.categories
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals

class ResquestsTest {
    @Test
    fun testGetCategoriasRequest() = testApplication{
        val categoriasRepository = CategoriaRepository()
        categoriasRepository.init()
        application {
            categories(categoriasRepository)
        }
        val repository = CategoriaRepository()
        val categorias = GetCategoriasRequest(repository).execute()
        println(categorias)
        assertEquals(3,categorias.size)
    }

    @Test
    fun testGetCategoriasItemRequest() = testApplication{
        val categoriasRepository = CategoriaRepository()
        categoriasRepository.init()
        application {
            categories(categoriasRepository)
        }
        val repository = CategoriaRepository()
        val categoriasItem = GetCategoriasItemRequest(repository).execute()
        println(categoriasItem)
        assertEquals(106,categoriasItem.size)
    }

}