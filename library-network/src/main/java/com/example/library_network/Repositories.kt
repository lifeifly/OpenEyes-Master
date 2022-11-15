package com.example.library_network

import android.content.Context
import androidx.room.withTransaction
import com.example.library_network.bean.CommunityAttentionBean
import com.example.library_network.room.Database

/**
 *description : <p>
 *应用描述
 *</p>
 *
 *@author : flyli
 *@since :2021/6/3 22
 */
class Repositories private constructor(context: Context) {
    val globalNetwork = GlobalNetwork.getInstance()
    val database = Database.getInstance(context)
    val caDao = database.caDao()

    suspend fun getCABeanFromRoom()= caDao.queryCABean()

    suspend fun getCABeanFromNetwork(url: String)= globalNetwork.fetchCommunityAttention(url)

    suspend fun getCABeanFirst():CommunityAttentionBean{
        val response=getCABeanFromRoom()
        return response
    }
    suspend fun getCABeanAfter(url: String):CommunityAttentionBean{
        val response=getCABeanFromNetwork(url)
        database.withTransaction {
            caDao.deleteAllCABean()
            caDao.insertCABean(response)
        }
        return response
    }




    companion object {

        private var repository: Repositories? = null

        @Synchronized
        fun getInstance(context: Context): Repositories {
            if (repository == null) {
                repository = Repositories(context)
            }
            return repository!!
        }
    }

}