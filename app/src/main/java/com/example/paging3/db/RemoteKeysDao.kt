package com.example.paging3.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.paging3.model.QuoteRemoteKeys

@Dao
interface RemoteKeysDao {
    @Query("SELECT * FROM QuoteRemoteKeys WHERE id =:id")
    suspend fun getRemoteKeys(id:String): QuoteRemoteKeys
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllRemoteKeys(remoteKeys: List<QuoteRemoteKeys>)
    @Query("DELETE FROM quoteremotekeys")
    suspend fun deleteAllRemoteKeys()
}