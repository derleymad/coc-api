package com.example

import com.example.database.CategoriaRepository
import com.example.plugins.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {

   CategoriaRepository().init()
    embeddedServer(Netty, port = 8080, host="0.0.0.0") {
        categories()
    }.start(wait = true)
}


