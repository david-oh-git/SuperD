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