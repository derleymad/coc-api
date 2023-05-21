package com.example

import com.example.database.CategoriaRepository
import com.example.network.GetCategoriasRequest
import com.example.plugins.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {

   val categoriasRepository = CategoriaRepository()
    categoriasRepository.init()

    val request = GetCategoriasRequest(categoriasRepository)
    request.execute()

    embeddedServer(Netty, port = 5555, host="0.0.0.0") {
        categories(categoriasRepository)
    }.start(wait = true)
}


