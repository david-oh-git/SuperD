package io.audioshinigami.superd.factory

import kotlin.random.Random

/*
* helps generate random values for url, resquest_id & progress value*/

object DataFactory {

    private val alphabet = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    fun randomUrl() = "https://${randomString(8)}.com/${randomString(11)}/${randomString(8)}.mp4"

    fun randomProgressValue() = Random.nextInt(0, 100)

    fun randomRequestId(length: Int = 12) = Random.nextInt(100000000, 999999999)

    private fun randomString( length: Int = 15) = alphabet.map { it }.shuffled().subList(0, length).joinToString("")

} /*END DataFactory*/