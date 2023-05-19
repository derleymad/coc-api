package com.example.database
import com.example.model.Categoria
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

//data class CategoriaFromDb(val id: Int, val image: String, val link: String, val title: String)

object Categorias : Table() {
    val id = integer("id").autoIncrement()
    val image = varchar("image", 300)
    val link = varchar("link", 100)
    val title = varchar("title", 100)
    override val primaryKey = PrimaryKey(id)
}

object CategoriasItem : Table() {
    val id = integer("id").autoIncrement()
    val image = varchar("imageItem", 300)
    val link = varchar("linkItem", 100)
    val title = varchar("titleItem", 100)
    override val primaryKey = PrimaryKey(id)
}

class CategoriaRepository {
    fun init() {
        Database.connect("jdbc:sqlite:coc.db", "org.sqlite.JDBC")
        transaction {
            SchemaUtils.create(Categorias)
            SchemaUtils.create(CategoriasItem)
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
    fun adicionarCategoriaItem(image: String, link: String, title: String) {
        transaction {
            CategoriasItem.insert {
                it[CategoriasItem.image] = image
                it[CategoriasItem.link] = link
                it[CategoriasItem.title] = title
            }
        }
    }

    fun obterCategoriasItem(): List<Categoria> {
        return transaction {
            CategoriasItem.selectAll().map {
                Categoria(
                    image = it[CategoriasItem.image],
                    link = it[CategoriasItem.link],
                    title = it[CategoriasItem.title]
                )
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
