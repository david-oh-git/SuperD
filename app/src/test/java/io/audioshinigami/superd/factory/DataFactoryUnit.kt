/*
 * MIT License
 *
 * Copyright (c) 2020  David Osemwota
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.audioshinigami.superd.factory

import kotlin.random.Random

/*
* helps generate random values for url, resquest_id & progress value*/

object DataFactoryUnit {

    private val alphabet = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    fun randomUrl() = "https://${randomString(8)}.com/${randomString(11)}/${randomString(8)}.mp4"

    fun randomProgressValue() = Random.nextInt(0, 100)

    fun randomRequestId(length: Int = 12) = Random.nextInt(100000000, 999999999)

    private fun randomString( length: Int = 15) = alphabet.map { it }.shuffled().subList(0, length).joinToString("")

} /*END DataFactory*/