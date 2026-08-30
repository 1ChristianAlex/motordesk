package com.khrix.infrastructure.exposed.connections

import com.khrix.infrastructure.exposed.seeds.LoadSeeds
import org.jetbrains.exposed.v1.jdbc.Database

class MemoryConnection(
    loadSeeds: LoadSeeds
) : DatabaseConnection(loadSeeds) {
    override val database = Database.connect(
        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
        user = "root",
        driver = "org.h2.Driver",
        password = "",
    )
}