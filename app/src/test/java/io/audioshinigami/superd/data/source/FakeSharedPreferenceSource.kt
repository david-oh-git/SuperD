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

package io.audioshinigami.superd.data.source

class FakeSharedPreferenceSource(
    var preferences: MutableMap<String, Any> = mutableMapOf()
):SharedPreferenceRepo {

    override suspend fun save(key: String, value: String) {
        preferences[key] = value
    }

    override suspend fun save(key: String, value: Boolean) {
        preferences[key] = value
    }

    override suspend fun save(key: String, value: Int) {
        preferences[key] = value
    }

    override suspend fun getString(key: String): String = preferences[key] as String

    override suspend fun getBoolean(key: String): Boolean = preferences[key] as Boolean

    override suspend fun getInt(key: String): Int = preferences[key] as Int

    override suspend fun remove(key: String) {
        preferences.remove(key)
    }
}