package com.khrix.infrastructure.exposed.connections

import com.khrix.infrastructure.app.InfraConfig
import com.khrix.infrastructure.exposed.seeds.LoadSeeds
import org.jetbrains.exposed.v1.jdbc.Database

class PostgresConnection(
    loadSeeds: LoadSeeds,
    infraConfig: InfraConfig,
) : DatabaseConnection(loadSeeds) {
    override val database =
        infraConfig.exposedConfig.run {
            Database.connect(
                url = url,
                driver = driver,
                user = user,
                password = password,
            )
        }
}
