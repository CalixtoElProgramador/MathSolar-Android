package com.listocalixto.android.mathsolar.presentation.main.home

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.app.Constants.FREE_NEWS_TOPIC
import com.listocalixto.android.mathsolar.core.Resource
import com.listocalixto.android.mathsolar.data.model.Article
import com.listocalixto.android.mathsolar.data.source.article.ArticleDataSource
import com.listocalixto.android.mathsolar.domain.article.ArticleRepo
import com.listocalixto.android.mathsolar.utils.ArticleFilterType
import com.listocalixto.android.mathsolar.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repo: ArticleRepo) : ViewModel() {

    private val _forceUpdate = MutableLiveData(false)

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _topic = MutableLiveData<String>()
    val topic: LiveData<String> = _topic

    private val _items: LiveData<List<Article>> = _forceUpdate.switchMap { forceUpdate ->
        if (forceUpdate) {
            _dataLoading.value = true
            viewModelScope.launch {
                repo.refreshArticles(FREE_NEWS_TOPIC)
                _dataLoading.value = false
            }
        }
        repo.observeArticles().switchMap { filterArticles(it) }
    }

    val items: LiveData<List<Article>> = _items

    private val _currentFilteringLabel = MutableLiveData<Int>()
    val currentFilteringLabel: LiveData<Int> = _currentFilteringLabel

    private val _noArticlesLabel = MutableLiveData<Int>()
    val noArticlesLabel: LiveData<Int> = _noArticlesLabel

    private val _noArticlesIconRes = MutableLiveData<Int>()
    val noArticlesIconRes: LiveData<Int> = _noArticlesIconRes

    private val _articlesAddViewVisible = MutableLiveData<Boolean>()
    val articlesAddViewVisible: LiveData<Boolean> = _articlesAddViewVisible

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private var currentFiltering = ArticleFilterType.ALL_ARTICLES

    private val isDataLoadingError = MutableLiveData<Boolean>()

    private val _openTaskEvent = MutableLiveData<Event<String>>()
    val openTaskEvent: LiveData<Event<String>> = _openTaskEvent

    private var resultMessageShown: Boolean = false

    // This LiveData depends on another so we can use a transformation.
    val empty: LiveData<Boolean> = Transformations.map(_items) {
        it.isEmpty()
    }

    init {
        setFiltering(ArticleFilterType.ALL_ARTICLES)
        loadArticles(true)
    }

    /**
     * Sets the current task filtering type.
     *
     * @param requestType Can be [ArticleFilterType.ALL_ARTICLES],
     * [ArticleFilterType.BOOKMARK], or
     * [ArticleFilterType.HISTORY]
     */
    fun setFiltering(requestType: ArticleFilterType) {
        currentFiltering = requestType

        // Depending on the filter type, set the filtering label, icon drawables, etc.
        when (requestType) {
            ArticleFilterType.ALL_ARTICLES -> {
                setFilter(
                    R.string.label_all, R.string.no_articles_all,
                    R.drawable.ic_error_placeholder, true
                )
            }
            ArticleFilterType.BOOKMARK -> {
                setFilter(
                    R.string.label_bookmark, R.string.no_articles_bookmark,
                    R.drawable.ic_error_placeholder, false
                )
            }
            ArticleFilterType.HISTORY -> {
                setFilter(
                    R.string.label_history, R.string.no_articles_viewed,
                    R.drawable.ic_error_placeholder, false
                )
            }
        }
        // Refresh list
        loadArticles(false)
    }

    private fun setFilter(
        @StringRes filteringLabelString: Int, @StringRes noTasksLabelString: Int,
        @DrawableRes noTaskIconDrawable: Int, tasksAddVisible: Boolean
    ) {
        _currentFilteringLabel.value = filteringLabelString
        _noArticlesLabel.value = noTasksLabelString
        _noArticlesIconRes.value = noTaskIconDrawable
        _articlesAddViewVisible.value = tasksAddVisible
    }

    fun bookmarkArticle(article: Article, bookmark: Boolean) = viewModelScope.launch {
        if (!bookmark) {
            repo.bookmarkArticle(article)
            Log.d(TAG, "bookmarkArticle: Article marked bookmark")
            showSnackbarMessage(R.string.article_marked_bookmark)
        } else {
            repo.deleteBookmarkArticle(article)
            Log.d(TAG, "bookmarkArticle: Article deleted bookmark")
            showSnackbarMessage(R.string.article_deleted_bookmark)
        }
    }

    private fun filterArticles(resource: Resource<List<Article>>): LiveData<List<Article>> {
        val result = MutableLiveData<List<Article>>()

        if (resource is Resource.Success) {
            isDataLoadingError.value = false
            viewModelScope.launch {
                result.value = filterItems(resource.data, currentFiltering)
            }
        } else {
            result.value = emptyList()
            showSnackbarMessage(R.string.err_loading_articles)
            isDataLoadingError.value = true
        }
        return result
    }

    private fun filterItems(
        articles: List<Article>,
        filteringType: ArticleFilterType
    ): List<Article> {
        val articlesToShow = ArrayList<Article>()
        articles.forEach { article ->
            when (filteringType) {
                ArticleFilterType.ALL_ARTICLES -> articlesToShow.add(article)
                ArticleFilterType.BOOKMARK -> if (article.bookmark) articlesToShow.add(article)
                ArticleFilterType.HISTORY -> if (article.viewed) articlesToShow.add(article)
            }
        }
        return articlesToShow
    }

    private fun showSnackbarMessage(message: Int) {
        _snackbarText.value = Event(message)
    }

    /**
     * @param forceUpdate Pass in true to refresh the data in the [ArticleDataSource]
     */
    fun loadArticles(forceUpdate: Boolean) {
        _forceUpdate.value = forceUpdate
    }

    companion object {
        const val TAG = "HomeViewModel"
    }

}