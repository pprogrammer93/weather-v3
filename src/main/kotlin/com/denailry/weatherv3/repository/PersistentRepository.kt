package com.denailry.weatherv3.repository

import com.denailry.weatherv3.mvp.WeatherModel
import com.denailry.mocked.database.Database
import com.denailry.mocked.database.Weather
import com.denailry.mocked.database.WeatherDay
import java.lang.Exception

class PersistentRepository(private val database: Database) : Repository {
    override fun create(model: WeatherModel) = update(model)

    override fun read(location: String): List<WeatherModel> {
        val weathers = database.getByLocation(location)

        val results = ArrayList<WeatherModel>()
        weathers.forEach { results.add(createModel(it)) }

        return results
    }

    override fun update(model: WeatherModel) {
        val weather = createWeatherFrom(model) ?: throw Exception("invalid model")
        database.save(weather)
    }

    override fun delete(model: WeatherModel) {
        for (day in WeatherDay.values()) {
            if (day.toString() == model.day.toString()) {
                database.delete(model.location, day)
            }
        }
    }

    private fun createWeatherFrom(model: WeatherModel) : Weather? {
        for (day in WeatherDay.values()) {
            if (model.day.toString() == day.toString()) {
                return Weather(model.location, day, model.temperature, model.type)
            }
        }
        return null
    }

    private fun createModel(weather: Weather) : WeatherModel {
        for (day in WeatherModel.Day.values()) {
            if (day.toString() == weather.day.toString()) {
                return WeatherModel(weather.location, day, weather.temperature, weather.type)
            }
        }

        throw Exception("unexpected type of day from database")
    }
}