package com.denailry.mocked.cache

interface Cache {
    fun save(key: String, weather: Weather?)
    fun get(key: String) : Weather?
}