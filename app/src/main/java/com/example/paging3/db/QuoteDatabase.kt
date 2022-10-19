package com.example.paging3.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.paging3.model.QuoteRemoteKeys
import com.example.paging3.model.Result

@Database(entities = [Result::class, QuoteRemoteKeys::class], version = 1)
abstract class QuoteDatabase : RoomDatabase() {
    abstract fun quoteDao() : QuoteDao
    abstract fun remoteKeysDao() : RemoteKeysDao
}