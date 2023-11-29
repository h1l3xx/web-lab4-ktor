package com.example.plugins

import com.example.dto.AuthDto
import com.example.dto.AuthResultDto
import com.example.dto.ErrorDto
import com.example.dto.UserDto
import com.example.security.JwtConfig
import com.example.security.hash
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
class UserService(private val database: Database) {
    object Users : Table() {
        val id = integer("id").autoIncrement()
        val name = varchar("name", length = 50)
        val birthday = varchar("birthday", length = 25)
        val email = varchar("email", length = 25)
        val login = varchar("login", length = 50)
        val password = varchar("password", length = 1000)

        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(database) {
            SchemaUtils.create(Users)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun create(user: UserDto): String = dbQuery {
        val user_id = Users.insert {
            it[name] = user.name
            it[birthday] = user.birthday
            it[email] = user.email
            it[login] = user.login
            it[password] = hash(user.password)
        }[Users.id]
        JwtConfig.instance.createAccessToken(user_id)
    }

    suspend fun auth(auth : AuthDto) : AuthResultDto{
        val user = dbQuery {
            Users.select (Users.login eq auth.login
         ).map {
            it[Users.id]
        }.singleOrNull()
        }
        if (user == null){
            return AuthResultDto(
                false,
                ErrorDto(
                    401,
                    "Unauth",
                    "Invalid login or password "
                ),
                null,
                null
            )
        }else{
            val password = dbQuery {
                Users.select(Users.id eq user)
                    .map {
                        it[Users.password]
                    }
                    .singleOrNull()
            }
            if (hash(auth.password) == password){
                val token = JwtConfig.instance.createAccessToken(user)
                return AuthResultDto(
                    true,
                    null,
                    token,
                    user
                )
            }else{
                return AuthResultDto(
                    false,
                    ErrorDto(
                        401,
                        "Unauth",
                        "Invalid login or password "
                    ),
                    null,
                    null
                )
            }
        }
    }

    suspend fun read(id: Int): UserDto? {
        return dbQuery {
            Users.select { Users.id eq id }
                .map { UserDto( it[Users.email], it[Users.name], it[Users.birthday], it[Users.login], it[Users.password]) }
                .singleOrNull()
        }
    }

    suspend fun update(id: Int, user: UserDto) {
        dbQuery {
            Users.update({ Users.id eq id }) {
                it[name] = user.name
                it[birthday] = user.birthday
                it[email] = user.email
                it[login] = user.login
                it[password] = user.password
            }
        }
    }

    suspend fun delete(id: Int) {
        dbQuery {
            Users.deleteWhere { Users.id.eq(id) }
        }
    }
}
