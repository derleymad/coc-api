package com.example.database
import com.example.model.Categoria
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

//data class CategoriaFromDb(val id: Int, val image: String, val link: String, val title: String)

object Categorias : Table() {
    val id = integer("id").autoIncrement()
    val image = varchar("image", 200)
    val link = varchar("link", 100)
    val title = varchar("title", 100)
    override val primaryKey = PrimaryKey(id)
}

class CategoriaRepository {
    fun init() {
        Database.connect("jdbc:sqlite:categorias.db", "org.sqlite.JDBC")
        transaction {
            SchemaUtils.create(Categorias)
        }
    }

    fun adicionarCategoria(image: String, link: String, title: String) {
        transaction {
            Categorias.insert {
                it[Categorias.image] = image
                it[Categorias.link] = link
                it[Categorias.title] = title
            }
        }
    }

    fun obterCategorias(): List<Categoria> {
        return transaction {
            Categorias.selectAll().map {
                Categoria(
                    image = it[Categorias.image],
                    link = it[Categorias.link],
                    title = it[Categorias.title]
                )
            }
        }
    }
}
