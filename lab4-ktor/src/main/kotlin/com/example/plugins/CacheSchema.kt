package com.example.plugins

import com.example.Const
import com.example.dto.CacheResultDto
import com.example.dto.CacheSelectResultDto
import com.example.dto.DotDto
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*

class CacheService(database: Database) {
    object Cache : Table() {
        val id = integer("id").autoIncrement()
        val x = double("x")
        val y = double("y")
        val r = double("r")
        val Result = bool("result")
        val createAt = long("create at")
        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(database) {
            SchemaUtils.create(Cache)
        }
    }

    suspend fun refresh(){
        val cache = newSuspendedTransaction {  dbQuery {
                Cache.selectAll()
            }.map {
                CacheResultDto(
                    it[Cache.x],
                    it[Cache.y],
                    it[Cache.r],
                    it[Cache.Result],
                    it[Cache.id],
                    it[Cache.createAt]
                )
            }
        }
        for (ch in cache){
            if (ch.time!! < System.currentTimeMillis() - Const.DELAY){
                delete(ch.id!!)
            }
        }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }


    suspend fun readByDot(dot : DotDto) : CacheSelectResultDto{
        val result = dbQuery {
            Cache.slice(Cache.x, Cache.y, Cache.r, Cache.Result, Cache.id).selectAll().map {
                CacheResultDto(
                    it[Cache.x],
                    it[Cache.y],
                    it[Cache.r],
                    it[Cache.Result],
                    it[Cache.id],
                    null
                )
            }
        }
        for (one in result){
            if (one.x == dot.x && one.y == dot.y && one.r == dot.r){
                return CacheSelectResultDto(
                    true,
                    one.result,
                    one.id
                )
            }
        }
        return CacheSelectResultDto(
                false,
                null,
            null
        )
    }

    suspend fun update(id: Int, time: Long) {
        dbQuery {
            Cache.update({ Cache.id eq id }) {
                it[createAt] = time
            }
        }
    }

    private suspend fun delete(id: Int) {
        dbQuery {
            Cache.deleteWhere { Cache.id.eq(id) }
        }
    }
}