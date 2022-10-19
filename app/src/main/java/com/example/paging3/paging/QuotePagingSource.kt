package com.example.paging3.paging

import android.content.ContentValues.TAG
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.paging3.model.Result
import com.example.paging3.retrofit.QuateAPI

class QuotePagingSource(val quateAPI: QuateAPI) : PagingSource<Int, Result>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        try {
            Log.d(TAG, "load: quoteapi data is fetched")
            val position = params.key ?: 1
            val response = quateAPI.getQuates(position)
            Log.d(TAG, "load $response")
            return LoadResult.Page(
                data = response.results,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (position == response.lastItemIndex) null else position + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }

//        if(state.anchorPosition != null){
//            val anchorPage = state.closestPageToPosition(state.anchorPosition!!)
//            if (anchorPage?.prevKey != null) {
//                return anchorPage.prevKey!!.plus(1)
//            }
//            else if(anchorPage?.nextKey != null){
//                return anchorPage.nextKey!!.minus(1)
//            }
//        } else{
//            return null
//        }
    }
}