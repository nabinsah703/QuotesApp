package com.example.paging3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.paging3.repository.QuoteReository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class QuoteViewModel @Inject constructor(val reository: QuoteReository):ViewModel() {
    val list = reository.getQuotees().cachedIn(viewModelScope)


}