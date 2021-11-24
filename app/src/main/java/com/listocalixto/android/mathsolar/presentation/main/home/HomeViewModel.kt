package com.listocalixto.android.mathsolar.presentation.main.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.listocalixto.android.mathsolar.app.Constants.FREE_NEWS_QUERY
import com.listocalixto.android.mathsolar.core.Resource
import com.listocalixto.android.mathsolar.domain.article.ArticleRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repo: ArticleRepo) : ViewModel() {

    private val _initViewModel = MutableLiveData(false)
    val initViewModel: LiveData<Boolean> = _initViewModel

    init {
        Log.d(TAG, "Init HomeViewModel")
        _initViewModel.value = true
        getArticles()
    }

    private fun getArticles() {
        viewModelScope.launch {
            when (val result = repo.getArticles(FREE_NEWS_QUERY)) {
                is Resource.Success -> {
                    val articles = result.data.articles
                    Log.d(TAG, "getArticles: $articles")
                }
                is Resource.Error -> {
                    Log.d(TAG, "getArticles: Error: ${result.errorMessage.exception}")
                }
            }
        }
    }

    companion object {
        const val TAG = "HomeViewModel"
    }

}