package com.listocalixto.android.mathsolar.presentation.main.articles.article_details

import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.core.Resource
import com.listocalixto.android.mathsolar.data.model.Article
import com.listocalixto.android.mathsolar.domain.article.ArticleRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleDetailsViewModel @Inject constructor(
    private val repo: ArticleRepo
) : ViewModel() {

    private val _articleId = MutableLiveData<String>()

    private val _article = _articleId.switchMap { articleId ->
        repo.observeArticle(articleId).map { computeResult(it) }
    }

    val article: LiveData<Article?> = _article

    val isDataAvailable: LiveData<Boolean> = _article.map { it != null }

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    val bookmark: LiveData<Boolean> = _article.map { input: Article? ->
        input?.bookmark ?: false
    }

    fun start(articleId: String?) {
        // If we're already loading or already loaded, return (might be a config change)
        if (_dataLoading.value == true || articleId == _articleId.value) {
            return
        }
        // Trigger the load
        articleId?.let { _articleId.value = it }
    }

    fun setBookmark() = viewModelScope.launch {
        val article = _article.value ?: return@launch
        if (article.bookmark) {
            repo.deleteBookmarkArticle(article)
        } else {
            repo.bookmarkArticle(article)
        }
    }

    private fun computeResult(resource: Resource<Article>): Article? {
        return if (resource is Resource.Success) {
            resource.data
        } else {
            showSnackbarMessage(R.string.err_loading_articles)
            null
        }
    }

    private fun showSnackbarMessage(@StringRes message: Int) {
        TODO("Not yet implemented")
    }

}