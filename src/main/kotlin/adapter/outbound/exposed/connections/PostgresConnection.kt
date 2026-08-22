package com.khrix.adapter.outbound.exposed.connections

import com.khrix.adapter.app.InfraConfig
import com.khrix.adapter.outbound.exposed.seeds.LoadSeeds
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
