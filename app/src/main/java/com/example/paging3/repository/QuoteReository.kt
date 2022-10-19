package com.example.paging3.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.paging3.db.QuoteDatabase
import com.example.paging3.paging.QuotePagingSource
import com.example.paging3.paging.QuoteRemoteMediator
import com.example.paging3.retrofit.QuateAPI
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class QuoteReository @Inject constructor(
    private val quateAPI: QuateAPI,
    private val quoteDatabase: QuoteDatabase
    ){
    fun getQuotees() = Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = 100
        ),
        remoteMediator = QuoteRemoteMediator(quateAPI,quoteDatabase),
        pagingSourceFactory = { quoteDatabase.quoteDao().getQuotes()}
    ).liveData

}