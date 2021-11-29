package com.listocalixto.android.mathsolar.presentation.main.articles

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.app.CoroutinesQualifiers.MainDispatcher
import com.listocalixto.android.mathsolar.core.NetworkConnection
import com.listocalixto.android.mathsolar.core.Resource
import com.listocalixto.android.mathsolar.data.model.Article
import com.listocalixto.android.mathsolar.data.source.article.ArticleDataSource
import com.listocalixto.android.mathsolar.domain.article.ArticleRepo
import com.listocalixto.android.mathsolar.utils.ArticleFilterType
import com.listocalixto.android.mathsolar.utils.ArticleTopic
import com.listocalixto.android.mathsolar.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticlesViewModel @Inject constructor(
    private val repo: ArticleRepo,
    private val networkConnection: NetworkConnection,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _forceUpdate = MutableLiveData(false)

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _topic = MutableLiveData(ArticleTopic.SOLAR_POWER)
    val topic: LiveData<ArticleTopic> = _topic

    private val _items: LiveData<List<Article>> = _forceUpdate.switchMap { forceUpdate ->
        if (forceUpdate) {
            _dataLoading.value = true
            viewModelScope.launch {
                _topic.value?.let { repo.refreshArticles(it) }
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

    private var lifeCycleOwner: LifecycleOwner? = null

    private val isDataLoadingError = MutableLiveData<Boolean>()

    private val _openArticleEvent = MutableLiveData<Event<String>>()
    val openArticleEvent: LiveData<Event<String>> = _openArticleEvent

    private var resultMessageShown: Boolean = false

    // This LiveData depends on another so we can use a transformation.
    val empty: LiveData<Boolean> = Transformations.map(_items) {
        it?.isEmpty()
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
                    R.string.label_all_articles, R.string.no_articles_all,
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

    private fun filterArticles(resource: Resource<List<Article>>) =
        liveData(viewModelScope.coroutineContext + mainDispatcher) {
            if (resource is Resource.Success) {
                isDataLoadingError.value = false
                emit(filterItems(resource.data, currentFiltering))

            } else {
                showSnackbarMessage(R.string.err_loading_articles)
                isDataLoadingError.value = true
                emit(emptyList())
            }
        }

    private fun filterItems(
        articles: List<Article>,
        filteringType: ArticleFilterType
    ): List<Article> {
        val articlesToShow = ArrayList<Article>()
        articles.forEach { article ->
            when (filteringType) {
                ArticleFilterType.ALL_ARTICLES -> {
                    if (article.topic == _topic.value) articlesToShow.add(article)
                }
                ArticleFilterType.BOOKMARK -> {
                    if (article.bookmark) articlesToShow.add(article)
                }
                ArticleFilterType.HISTORY -> {
                    if (article.viewed) articlesToShow.add(article)
                }
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

    fun setLifecycleOwnerToObserveNetworkConnection(lifecycle: LifecycleOwner) {
        lifeCycleOwner = lifecycle
    }

    fun changeTopic(topic: ArticleTopic) {
        _topic.value = topic
        lifeCycleOwner?.let { lifecycle ->
            networkConnection.observe(lifecycle, Observer {
                loadArticles(it)
            })
        }
    }

    fun openArticle(articleId: String) {
        _openArticleEvent.value = Event(articleId)
        viewModelScope.launch {
            repo.addArticleToHistory(articleId)
        }
    }

    fun updateBookmark(article: Article) {
        viewModelScope.launch {
            if (article.bookmark) {
                repo.deleteBookmarkArticle(article)
            } else {
                repo.bookmarkArticle(article)
            }
        }
    }

    companion object {
        const val TAG = "ArticlesViewModel"
    }

}