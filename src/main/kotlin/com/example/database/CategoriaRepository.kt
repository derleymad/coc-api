package com.example.database
import com.example.database.BuildingEntity.autoIncrement
import com.example.database.BuildingItemEntity.autoIncrement
import com.example.model.Building
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

//data class CategoriaFromDb(val id: Int, val image: String, val link: String, val title: String)

object BuildingEntity: Table() {
    val id = integer("id").autoIncrement()
    val parent = varchar("parent",100)
    val link = varchar("link", 100)
    val image = varchar("image", 300)
    val title = varchar("title", 100)
    override val primaryKey = PrimaryKey(id)
}

object BuildingItemEntity : Table(){
    val id = integer("id").autoIncrement()
    val parent = varchar("parentBuildingItem",100)
    val linkBuildingItem = varchar("linkBuildingItem", 100)
    val image = varchar("imageBuildingItem", 300)
    val title = varchar("titleBuildingItem", 100)
    override val primaryKey = PrimaryKey(id)
}
object ItemEntity : Table(){
    val id = integer("id").autoIncrement()
    val parent = varchar("parentItem",100)
    val linkItem = varchar("linkItem", 100)
    val image = varchar("imageItem", 300)
    val title = varchar("titleItem", 100)
    override val primaryKey = PrimaryKey(id)
}

class CategoriaRepository {
    fun init() {
        Database.connect("jdbc:sqlite:coc.db", "org.sqlite.JDBC")
        transaction {
            SchemaUtils.create(BuildingEntity)
            SchemaUtils.create(BuildingItemEntity)
            SchemaUtils.create(ItemEntity)
        }
    }

    fun adicionarBuilding(building: Building) {
        transaction {
            BuildingEntity.insert {
                it[parent] = building.parent
                it[image] = building.image
                it[link] = building.link
                it[title] = building.title
            }
        }
    }
    fun obterBuildings(): List<Building> {
        return transaction {
            BuildingEntity.selectAll().map {
                Building(
                    parent = it[BuildingEntity.parent],
                    image = it[BuildingEntity.image],
                    link = it[BuildingEntity.link],
                    title = it[BuildingEntity.title]
                )
            }
        }
    }

    fun getBuildingsByParent(parent: String): List<Building> {
        // Inicie uma transação
        return transaction {
            // Realize a consulta
            BuildingEntity.select { BuildingEntity.parent eq parent }.map {
                Building(
                    parent = it[BuildingEntity.parent],
                    title = it[BuildingEntity.title],
                    image = it[BuildingEntity.image],
                    link = it[BuildingEntity.link]
                )
            }
        }
    }
}
