package com.khrix.infrastructure.app

import java.util.Properties

fun loadProperties() =
    Properties().apply {
        object {}.javaClass.classLoader.getResourceAsStream("dev.secrets.properties")?.use {
            load(it)
        }
    }
