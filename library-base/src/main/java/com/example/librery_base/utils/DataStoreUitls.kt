package com.example.librery_base.utils

import androidx.datastore.preferences.core.*
import com.example.librery_base.global.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.lang.IllegalArgumentException

/**
 *description : <p>
 *datastore工具类
 *</p>
 *
 *@author : flyli
 *@since :2021/5/23 19
 */
object DataStoreUitls {
    /**
     * 同步获取数据
     * @param key String
     * @param default U
     * @return U
     */
    fun <U> getSyncData(key: String, default: U): U {
        val res = when (default) {
            is Long -> readLongData(
                key,
                default
            )
            is String -> readStringData(
                key,
                default
            )
            is Int -> readIntData(
                key,
                default
            )
            is Boolean -> readBooleanData(
                key,
                default
            )
            is Float -> readFloatData(
                key,
                default
            )
            else -> throw IllegalArgumentException("This type can be saved into DataStore")
        }
        return res as U
    }

    /**
     * 异步获取数据
     * @param key String
     * @param default U
     * @return U
     */
    fun <U> getAsyncData(key: String, default: U): Flow<U> {
        val res = when (default) {
            is Long -> readLongDataFlow(
                key,
                default
            )
            is String -> readStringDataFlow(
                key,
                default
            )
            is Int -> readIntDataFlow(
                key,
                default
            )
            is Boolean -> readBooleanDataFlow(
                key,
                default
            )
            is Float -> readFloatDataFlow(
                key,
                default
            )
            else -> throw IllegalArgumentException("This type can be saved into DataStore")
        }
        return res as Flow<U>
    }

    /**
     * 异步放置数据
     */
    suspend fun <U> putDataAsync(key: String, value: U) {
        when (value) {
            is Long -> saveLongDataAsync(
                key,
                value
            )
            is String -> saveStringDataAsync(
                key,
                value
            )
            is Int -> saveIntDataAsync(
                key,
                value
            )
            is Boolean -> saveBooleanDataAsync(
                key,
                value
            )
            is Float -> saveFloatDataAsync(
                key,
                value
            )
            else -> throw IllegalArgumentException("This type can be saved into DataStore")
        }
    }

    /**
     * 同步放置数据
     * @param key String
     * @param value U
     */
    fun <U> putDataSync(key: String, value: U) {
        when (value) {
            is Long -> saveLongDataSync(
                key,
                value
            )
            is String -> saveStringDataSync(
                key,
                value
            )
            is Int -> saveIntDataSync(
                key,
                value
            )
            is Boolean -> saveBooleanDataSync(
                key,
                value
            )
            is Float -> saveFloatDataSync(
                key,
                value
            )
            else -> throw IllegalArgumentException("This type can be saved into DataStore")
        }
    }

    /**
     * 同步获取数据的方法
     */
    fun readBooleanData(key: String, default: Boolean = false): Boolean {
        var value = false
        runBlocking {
            dataStore.data.first {
                value = it[booleanPreferencesKey(key)] ?: default
                true
            }
        }
        return value
    }

    fun readLongData(key: String, default: Long = 0): Long {
        var value = 0L
        runBlocking {
            dataStore.data.first {
                value = it[longPreferencesKey(key)] ?: default
                true
            }
        }
        return value
    }

    fun readIntData(key: String, default: Int = 0): Int {
        var value = 0
        runBlocking {
            dataStore.data.first {
                value = it[intPreferencesKey(key)] ?: default
                true
            }
        }
        return value
    }

    fun readFloatData(key: String, default: Float = 0F): Float {
        var value = 0F
        runBlocking {
            dataStore.data.first {
                value = it[floatPreferencesKey(key)] ?: default
                true
            }
        }
        return value
    }

    fun readStringData(key: String, default: String = ""): String {
        var value = ""
        runBlocking {
            dataStore.data.first {
                value = it[stringPreferencesKey(key)] ?: default
                true
            }
        }
        return value
    }

    /**
     * 异步获取数据
     */
    fun readBooleanDataFlow(key: String, default: Boolean = false): Flow<Boolean> =
        dataStore.data.catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map {
            it[booleanPreferencesKey(key)] ?: default
        }

    fun readFloatDataFlow(key: String, default: Float = 0F): Flow<Float> =
        dataStore.data.catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map {
            it[floatPreferencesKey(key)] ?: default
        }

    fun readIntDataFlow(key: String, default: Int = 0): Flow<Int> =
        dataStore.data.catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map {
            it[intPreferencesKey(key)] ?: default
        }

    fun readStringDataFlow(key: String, default: String = " "): Flow<String> =
        dataStore.data.catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map {
            it[stringPreferencesKey(key)] ?: default
        }

    fun readLongDataFlow(key: String, default: Long = 0L): Flow<Long> =
        dataStore.data.catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map {
            it[longPreferencesKey(key)] ?: default
        }


    /**
     * 异步存储数据
     */
    suspend fun saveBooleanDataAsync(key: String, value: Boolean) {
        dataStore.edit {
            it[booleanPreferencesKey(key)] = value
        }
    }

    suspend fun saveLongDataAsync(key: String, value: Long) {
        dataStore.edit {
            it[longPreferencesKey(key)] = value
        }
    }

    suspend fun saveIntDataAsync(key: String, value: Int) {
        dataStore.edit {
            it[intPreferencesKey(key)] = value
        }
    }

    suspend fun saveFloatDataAsync(key: String, value: Float) {
        dataStore.edit {
            it[floatPreferencesKey(key)] = value
        }
    }

    suspend fun saveStringDataAsync(key: String, value: String) {
        dataStore.edit {
            it[stringPreferencesKey(key)] = value
        }
    }

    /**
     * 同步存储数据
     */
    fun saveBooleanDataSync(key: String, value: Boolean) = runBlocking {
        saveBooleanDataAsync(
            key,
            value
        )
    }

    fun saveLongDataSync(key: String, value: Long) = runBlocking {
        saveLongDataAsync(
            key,
            value
        )
    }

    fun saveIntDataSync(key: String, value: Int) = runBlocking {
        saveIntDataAsync(
            key,
            value
        )
    }

    fun saveFloatDataSync(key: String, value: Float) = runBlocking {
        saveFloatDataAsync(
            key,
            value
        )
    }

    fun saveStringDataSync(key: String, value: String) = runBlocking {
        saveStringDataAsync(
            key,
            value
        )
    }

    /**
     * 异步清除
     */
    suspend fun clear(){
        dataStore.edit {
            it.clear()
        }
    }
    /**
     * 同步清除
     */
    fun clearSync()= runBlocking {
        dataStore.edit {
            it.clear()
        }
    }
}