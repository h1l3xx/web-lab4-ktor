package com.example.plugins

import com.example.dto.ResultDto
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
class ResultsService(database: Database) {
    object Results : Table() {
        val id = integer("id").autoIncrement()
        val owner = reference("owner_id", UserService.Users.id)
        val x = double("x")
        val y = double("y")
        val r = double("r")
        val Result = bool("result")

        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(database) {
            SchemaUtils.create(Results)
        }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun create(result: ResultDto, cache : Boolean): Int = dbQuery {
        val id = dbQuery {
            Results.insert {
                it[x] = result.x
                it[y] = result.y
                it[r] = result.r
                it[Result] = result.result
                it[owner] = result.ownerId
            }
        }[Results.id]
        if (!cache){
            dbQuery {
                CacheService.Cache.insert {
                    it[x] = result.x
                    it[y] = result.y
                    it[r] = result.r
                    it[Result] = result.result
                    it[createAt] = System.currentTimeMillis()
                }
            }
        }
        return@dbQuery id
    }

    suspend fun getByUserId(id: Int): List<ResultDto> {
        return dbQuery {
            Results.select { Results.owner eq id }
                .map { ResultDto(
                    it[Results.x],
                    it[Results.y],
                    it[Results.r],
                    it[Results.owner],
                    it[Results.Result],
                    null
                ) }
        }
    }
    suspend fun deleteByUserId(userId : Int){
        dbQuery {
            Results.deleteWhere { owner.eq(userId) }
        }
    }
}