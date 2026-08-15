package com.khrix.infrastructure.app

import com.khrix.BuildKonfig
import java.util.Properties

fun loadProperties() =
    Properties().apply {
        object {}.javaClass.classLoader.getResourceAsStream(BuildKonfig.PROPERTIES_FILE)?.use {
            load(it)
        }
    }
