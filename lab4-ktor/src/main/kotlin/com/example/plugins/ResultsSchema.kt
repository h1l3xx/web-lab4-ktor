package com.example.plugins

import com.example.dto.ResultDto
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
class ResultsService(private val database: Database) {
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

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun create(result: ResultDto): Int = dbQuery {
        Results.insert {
            it[x] = result.x
            it[y] = result.y
            it[r] = result.r
            it[Result] = result.result
            it[owner] = result.ownerId
        }[Results.id]
    }

    suspend fun read(id: Int): ResultDto? {
        return dbQuery {
            Results.select { Results.id eq id }
                .map { ResultDto(
                    it[Results.x],
                    it[Results.y],
                    it[Results.r],
                    it[Results.owner],
                    it[Results.Result],
                    null,
                    null
                    ) }
                .singleOrNull()
        }
    }

    suspend fun update(id: Int, result: ResultDto) {
        dbQuery {
            Results.update({ Results.id eq id }) {
                it[x] = result.x
                it[y] = result.y
                it[r] = result.r
                it[Result] = result.result
                it[owner] = result.ownerId
            }
        }
    }

    suspend fun getByUserId(id: Int): List<ResultDto> {
        return dbQuery {
            Results.select { Results.id eq id }
                .map { ResultDto(
                    it[Results.x],
                    it[Results.y],
                    it[Results.r],
                    it[Results.owner],
                    it[Results.Result],
                    null,
                    null
                ) }
        }
    }

    suspend fun delete(id: Int) {
        dbQuery {
            Results.deleteWhere { Results.id.eq(id) }
        }
    }
}