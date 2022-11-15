package com.example.library_network.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.library_network.bean.CommunityAttentionBean

/**
 *description : <p>
 *room的dao
 *</p>
 *
 *@author : flyli
 *@since :2021/6/3 22
 */
@Dao
interface CADao{

    @Insert
    suspend fun insertCABean(communityAttentionBean: CommunityAttentionBean)

    @Query("SELECT * FROM ca_table")
    suspend fun queryCABean():CommunityAttentionBean

    @Query("DELETE FROM ca_table")
    suspend fun deleteAllCABean()
}