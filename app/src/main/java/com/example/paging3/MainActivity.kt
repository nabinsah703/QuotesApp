 package com.example.paging3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.paging3.paging.LoaderAdapter
import com.example.paging3.paging.QuotePagingAdapter
import dagger.hilt.android.AndroidEntryPoint

 @AndroidEntryPoint
class MainActivity : AppCompatActivity() {
     lateinit var recyclerView: RecyclerView
     lateinit var adapter: QuotePagingAdapter
     lateinit var quoteViewModel: QuoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        recyclerView = findViewById(R.id.recyclerview)
        adapter = QuotePagingAdapter()
        quoteViewModel = ViewModelProvider(this).get(QuoteViewModel::class.java)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
            header = LoaderAdapter(),
            footer = LoaderAdapter()
        )
        quoteViewModel.list.observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }
// lateinit var quoteViewModel: QuoteViewModel
//
//     override fun onCreate(savedInstanceState: Bundle?) {
//         super.onCreate(savedInstanceState)
//         setContentView(R.layout.activity_main)
//
//
//         quoteViewModel = ViewModelProvider(this)[quoteViewModel::class.java]
//         val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
//         val adapter =QuotePagingAdapter()
//            recyclerView.setHasFixedSize(true)
//         recyclerView.adapter = adapter
//
//         quoteViewModel.list.observe(this){adapter.submitData(lifecycle,it)}
}
