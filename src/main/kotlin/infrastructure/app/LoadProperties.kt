package com.khrix.infrastructure.app

import java.util.*

fun loadProperties() = Properties().apply {
    object {}.javaClass.classLoader.getResourceAsStream("secrets.properties")?.use {
        load(it)
    }
}