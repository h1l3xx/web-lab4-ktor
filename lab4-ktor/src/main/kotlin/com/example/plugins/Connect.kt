package com.example.plugins

import org.jetbrains.exposed.sql.Database

fun configureConnect(): Database {
    return Database.connect(
        url = "jdbc:postgresql://localhost:5432/postgres",
        user = "postgres",
        driver = "org.postgresql.Driver",
        password = ""
    )
}