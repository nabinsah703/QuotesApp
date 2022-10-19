package com.example.paging3.paging

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.paging3.R
import com.example.paging3.model.Result

class QuotePagingAdapter: PagingDataAdapter<Result, QuotePagingAdapter.QuoteViewHolder>(COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuotePagingAdapter.QuoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.quote_item,parent,false)
        return QuoteViewHolder(view)
    }
    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null ){
            holder.quote.text = item.content
        }
        Log.d(TAG, "onBindViewHolder: $item")
    }

    class QuoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val quote = itemView.findViewById<TextView>(R.id.quote)

    }

    companion object{
        private val COMPARATOR = object : DiffUtil.ItemCallback<Result>(){
            override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
                return oldItem._id == newItem._id
            }

            override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
                return oldItem == newItem
            }
        }
    }


    }

