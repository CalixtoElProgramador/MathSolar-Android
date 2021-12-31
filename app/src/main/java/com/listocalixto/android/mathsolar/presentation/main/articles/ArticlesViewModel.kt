package com.listocalixto.android.mathsolar.presentation.main.articles

import android.util.Log
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.IntegerRes
import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.app.CoroutinesQualifiers.MainDispatcher
import com.listocalixto.android.mathsolar.core.NetworkConnection
import com.listocalixto.android.mathsolar.core.Resource
import com.listocalixto.android.mathsolar.data.model.Article
import com.listocalixto.android.mathsolar.data.source.article.ArticleDataSource
import com.listocalixto.android.mathsolar.domain.article.ArticleRepo
import com.listocalixto.android.mathsolar.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.ref.Reference
import javax.inject.Inject
import kotlin.jvm.internal.Ref

@HiltViewModel
class ArticlesViewModel @Inject constructor(
    private val repo: ArticleRepo,
    networkConnection: NetworkConnection,
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
            viewModelScope.launch (viewModelScope.coroutineContext + mainDispatcher) {
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

    private val _snackbarText = MutableLiveData<Event<SnackbarMessage>>()
    val snackbarText: LiveData<Event<SnackbarMessage>> = _snackbarText

    private var currentFiltering = ArticleFilterType.ALL_ARTICLES

    private val isDataLoadingError = MutableLiveData<Boolean>()

    private val _openArticleEvent = MutableLiveData<Event<OnCardViewClicked>>()
    val openArticleEvent: LiveData<Event<OnCardViewClicked>> = _openArticleEvent

    private val _anArticleWasOpen = MutableLiveData(false)
    val anArticleWasOpen: LiveData<Boolean> = _anArticleWasOpen

    private val _expandedAppBarState = MutableLiveData(true)
    val expandedAppBarState: LiveData<Boolean> = _expandedAppBarState

    private var resultMessageShown: Boolean = false

    private var referenceBoolean = false

    // This LiveData depends on another so we can use a transformation.
    val empty: LiveData<Boolean> = Transformations.map(_items) {
        it?.isEmpty()
    }

    val isBookmarkOrHistory: LiveData<Boolean> = Transformations.map(_currentFilteringLabel) {
        it == R.string.label_bookmark || it == R.string.label_history
    }

    val userHasInternet: LiveData<Boolean> = Transformations.map(networkConnection) {
        it
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

    fun bookmarkArticle(article: Article, bookmark: Boolean) =
        viewModelScope.launch(viewModelScope.coroutineContext + mainDispatcher) {
            if (!bookmark) {
                repo.bookmarkArticle(article)
            } else {
                repo.deleteBookmarkArticle(article)
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

    fun showSnackbarMessage(message: Int) {
        _snackbarText.value = Event(SnackbarMessage(message))
    }

    /**
     * @param forceUpdate Pass in true to refresh the data in the [ArticleDataSource]
     */
    fun loadArticles(forceUpdate: Boolean) {
        _forceUpdate.value = forceUpdate
    }

    fun changeTopic(topic: ArticleTopic) {
        _topic.value = topic
        if (userHasInternet.value == true && _anArticleWasOpen.value == false) {
            Log.d(TAG, "changeTopic: ForceUpdate equals true")
            loadArticles(true)
            return
        }
        loadArticles(false)
    }

    fun openArticle(cardView: View, articleId: String) {
        _openArticleEvent.value = Event(OnCardViewClicked(cardView, articleId))
        _anArticleWasOpen.value = true
        viewModelScope.launch(viewModelScope.coroutineContext + mainDispatcher) {
            repo.addArticleToHistory(articleId)
        }
    }

    fun onChipChecked() {
        _anArticleWasOpen.value = false
    }

    fun setExpandedAppBarState(boolean: Boolean) {
        _expandedAppBarState.value = boolean
    }

    fun updateBookmark(article: Article) {
        viewModelScope.launch(viewModelScope.coroutineContext + mainDispatcher) {
            if (article.bookmark) {
                repo.deleteBookmarkArticle(article)
            } else {
                repo.bookmarkArticle(article)
            }
        }
    }

    fun updateReferenceInternet(boolean: Boolean) {
        referenceBoolean = boolean
    }

    fun getReferenceInternet(): Boolean = referenceBoolean

    companion object {
        const val TAG = "ArticlesViewModel"
    }

}