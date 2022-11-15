package com.example.library_network.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.library_network.bean.CommunityAttentionBean

/**
 *description : <p>
 *数据库
 *</p>
 *
 *@author : flyli
 *@since :2021/6/3 22
 */
@Database(entities = [CommunityAttentionBean::class],version = 1,exportSchema = false)
abstract class Database:RoomDatabase() {

    abstract fun caDao():CADao

    companion object{
        private var INSTANCE:com.example.library_network.room.Database?=null
        @Synchronized
        fun getInstance(context: Context):com.example.library_network.room.Database{
            if (INSTANCE==null){
                INSTANCE=Room.databaseBuilder(context,com.example.library_network.room.Database::class.java,"open_db").build()
            }
            return INSTANCE!!
        }
    }
}