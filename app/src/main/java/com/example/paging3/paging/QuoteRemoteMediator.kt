package com.example.paging3.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.paging3.db.QuoteDatabase
import com.example.paging3.model.QuoteRemoteKeys
import com.example.paging3.model.Result
import com.example.paging3.retrofit.QuateAPI
import java.lang.Exception

@OptIn(ExperimentalPagingApi::class)
class QuoteRemoteMediator(
    private val quateAPI: QuateAPI,
    private val quoteDatabase: QuoteDatabase
) : RemoteMediator<Int, Result>() {
    val quoteDao = quoteDatabase.quoteDao()
    val quoteRemoteKeysDao = quoteDatabase.remoteKeysDao()

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Result>): MediatorResult {
        return try {

            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosesttoCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1

                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    prevPage

                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    nextPage

                }
            }
            val response = quateAPI.getQuates(currentPage)
            val endOfPaginationReached = response.totalPages == currentPage

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            quoteDatabase.withTransaction {
                if (loadType == LoadType.REFRESH){
                    quoteDao.deleteQuotes()
                    quoteRemoteKeysDao.deleteAllRemoteKeys()
                }
                quoteDao.addQuotes(response.results)
                val keys = response.results.map { quote ->
                    QuoteRemoteKeys(
                        id = quote._id,
                        prevPage = prevPage,
                        nextPage = nextPage
                    )
                }
                quoteRemoteKeysDao.addAllRemoteKeys(keys)
            }
            MediatorResult.Success(endOfPaginationReached)


        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
        //Fetch Quotes from API
        //Save these Quotes + RemoteKeys Data into DB
        //Logic for States - REFRESH, PREPEND, APPEND
    }

    private suspend fun getRemoteKeyClosesttoCurrentPosition(state: PagingState<Int, Result>): QuoteRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?._id?.let { id ->
                quoteRemoteKeysDao.getRemoteKeys(id = id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Result>): QuoteRemoteKeys? {
        return state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { quote ->
            quoteRemoteKeysDao.getRemoteKeys(id = quote._id)

        }

    }


    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, Result>
    ): QuoteRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { quote ->
            quoteRemoteKeysDao.getRemoteKeys(id = quote._id)
        }

    }
}