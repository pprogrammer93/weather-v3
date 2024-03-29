package com.denailry.weatherv3

import com.denailry.mocked.cache.GoCache
import com.denailry.weatherv3.mvp.WeatherPresenter
import com.denailry.weatherv3.mvp.WeatherView
import com.denailry.weatherv3.repository.PersistentRepository
import com.denailry.mocked.database.GoSQL
import com.denailry.weatherv3.repository.SmartRepository

class Application {
    private var view: WeatherView? = null

    fun run() {
        view = WeatherView()
        val cache = GoCache()
        val database = GoSQL("src/main/resources/weathers.txt")

        val repository = SmartRepository(cache, database)
        val presenter = WeatherPresenter(view!!, repository)
        view!!.setPresenter(presenter)

        startReceivingInput()
    }

    private fun startReceivingInput() {
        val inputHandler = view ?: return
        var input: String? = null

        while (input != "Q") {
            if (input != null) {
                handleCommands(inputHandler, input.split(" "))
            }
            input = readLine()
        }
    }

    private fun handleCommands(handler: WeatherView, commands: List<String>) {
        if (commands.size != 2) {
            println("-- invalid input --")
        } else {
            handler.acceptLocationAndDay(commands[0], commands[1])
        }
    }
}

fun main() {
    val app = Application()
    app.run()
}