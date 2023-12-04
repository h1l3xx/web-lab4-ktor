package com.example.database

import org.jetbrains.exposed.sql.Database

fun configureConnect(): Database {
    return Database.connect(
        url = "jdbc:postgresql://postgres:5432/postgres",
        user = "postgres",
        driver = "org.postgresql.Driver",
        password = "my-secret-pw"
    )
}