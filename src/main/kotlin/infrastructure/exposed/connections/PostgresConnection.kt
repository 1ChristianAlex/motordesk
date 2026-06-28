package com.khrix.infrastructure.exposed.connections

import com.khrix.infrastructure.app.InfraCredentials
import com.khrix.infrastructure.exposed.seeds.LoadSeeds
import org.jetbrains.exposed.v1.jdbc.Database

class PostgresConnection(
    loadSeeds: LoadSeeds,
    infraCredentials: InfraCredentials
) : DatabaseConnection(loadSeeds) {

    override val database = infraCredentials.exposedConfig.run {
        Database.connect(
            url = url,
            driver = driver,
            user = user,
            password = password
        )
    }
}