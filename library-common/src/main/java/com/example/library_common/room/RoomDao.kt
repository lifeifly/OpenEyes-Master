package com.example.library_common.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.library_network.bean.HomeDiscoveryBean

/**
 *description : <p>
 *应用描述
 *</p>
 *
 *@author : flyli
 *@since :2021/5/25 15
 */
@Dao
interface HomeDiscoveryDao {
    /**
     * 首页相关
     */
    @Insert
    fun saveHomeDiscovery(homeDiscoveryBean: HomeDiscoveryBean)

    @Query("SELECT * FROM homediscoverybean ")
    fun queryHomeDiscovery():HomeDiscoveryBean
}