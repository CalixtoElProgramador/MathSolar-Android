package com.listocalixto.android.mathsolar.domain.article

import android.util.Log
import androidx.lifecycle.LiveData
import com.listocalixto.android.mathsolar.app.CoroutinesQualifiers.LocalDataSourceArticle
import com.listocalixto.android.mathsolar.app.CoroutinesQualifiers.RemoteDataSourceArticle
import com.listocalixto.android.mathsolar.app.CoroutinesQualifiers.IoDispatcher
import com.listocalixto.android.mathsolar.core.Resource
import com.listocalixto.android.mathsolar.data.model.Article
import com.listocalixto.android.mathsolar.data.source.article.ArticleDataSource
import com.listocalixto.android.mathsolar.utils.ArticleTopic
import com.listocalixto.android.mathsolar.utils.ErrorMessage
import com.listocalixto.android.mathsolar.utils.toDatabaseModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ArticleRepoImpl @Inject constructor(
    @RemoteDataSourceArticle private val remoteDataSource: ArticleDataSource,
    @LocalDataSourceArticle private val localDataSource: ArticleDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ArticleRepo {

    override fun observeArticles(): LiveData<Resource<List<Article>>> =
        localDataSource.observeArticles()

    override fun observeArticle(articleId: String): LiveData<Resource<Article>> =
        localDataSource.observeArticle(articleId)


    override suspend fun getArticles(
        topic: ArticleTopic,
        forceUpdate: Boolean
    ): Resource<List<Article>> {
        if (forceUpdate) {
            try {
                updateArticlesFromRemoteDataSource(topic)
            } catch (e: Exception) {
                Resource.Error(ErrorMessage(exception = e))
                Log.e(TAG, "getArticle: catch block, exception: $e")
            }
        }
        return localDataSource.getArticles(topic)
    }


    override suspend fun getArticle(articleId: String, forceUpdate: Boolean): Resource<Article> {
        if (forceUpdate) {
            try {
                updateArticleFromRemoteDataSource(articleId)
            } catch (e: Exception) {
                Resource.Error(ErrorMessage(exception = e))
            }
        }
        return localDataSource.getArticle(articleId)
    }

    override suspend fun refreshArticles(topic: ArticleTopic) {
        updateArticlesFromRemoteDataSource(topic)
    }

    override suspend fun refreshArticle(articleId: String) {
        updateArticleFromRemoteDataSource(articleId)
    }

    private suspend fun updateArticlesFromRemoteDataSource(topic: ArticleTopic) {
        val remoteArticles = remoteDataSource.getArticles(topic)
        if (remoteArticles is Resource.Success) {
            //localDataSource.deleteAllArticles()
            remoteArticles.data.forEach { remoteArticle ->
                localDataSource.saveArticle(remoteArticle.toDatabaseModel(topic))
            }
        }
    }

    private suspend fun updateArticleFromRemoteDataSource(articleId: String) {
        val remoteArticle = remoteDataSource.getArticle(articleId)
        if (remoteArticle is Resource.Success) {
            localDataSource.saveArticle(remoteArticle.data)
        }
    }

    override suspend fun saveArticle(article: Article) {
        coroutineScope {
            launch { remoteDataSource.saveArticle(article) }
            launch { localDataSource.saveArticle(article) }
        }
    }

    override suspend fun bookmarkArticle(article: Article) = withContext<Unit>(ioDispatcher) {
        coroutineScope {
            launch { remoteDataSource.bookmarkArticle(article) }
            launch { localDataSource.bookmarkArticle(article) }
        }
    }

    override suspend fun bookmarkArticle(articleId: String) {
        withContext(ioDispatcher) {
            (getArticleWithId(articleId) as? Resource.Success)?.let {
                bookmarkArticle(it.data)
            }
        }
    }

    override suspend fun deleteBookmarkArticle(article: Article) = withContext<Unit>(ioDispatcher) {
        coroutineScope {
            launch { remoteDataSource.deleteBookmarkArticle(article) }
            launch { localDataSource.deleteBookmarkArticle(article) }
        }
    }

    override suspend fun deleteBookmarkArticle(articleId: String) {
        withContext(ioDispatcher) {
            (getArticleWithId(articleId) as? Resource.Success)?.let {
                deleteBookmarkArticle(it.data)
            }
        }
    }

    override suspend fun addArticleToHistory(article: Article) = withContext<Unit>(ioDispatcher) {
        coroutineScope {
            launch { remoteDataSource.addArticleToHistory(article) }
            launch { localDataSource.addArticleToHistory(article) }
        }
    }

    override suspend fun addArticleToHistory(articleId: String) {
        withContext(ioDispatcher) {
            (getArticleWithId(articleId) as? Resource.Success)?.let {
                addArticleToHistory(it.data)
            }
        }
    }

    override suspend fun deleteArticleFromHistory(article: Article) =
        withContext<Unit>(ioDispatcher) {
            coroutineScope {
                launch { remoteDataSource.deleteArticleFromHistory(article) }
                launch { localDataSource.deleteArticleFromHistory(article) }
            }
        }

    override suspend fun deleteArticleFromHistory(articleId: String) {
        withContext(ioDispatcher) {
            (getArticleWithId(articleId) as? Resource.Success)?.let {
                deleteArticleFromHistory(it.data)
            }
        }
    }

    override suspend fun clearHistory() {
        withContext(ioDispatcher) {
            coroutineScope {
                launch { remoteDataSource.clearHistory() }
                launch { localDataSource.clearHistory() }
            }
        }
    }

    override suspend fun deleteAllArticles() {
        withContext(ioDispatcher) {
            coroutineScope {
                launch { remoteDataSource.deleteAllArticles() }
                launch { localDataSource.deleteAllArticles() }
            }
        }
    }

    private suspend fun getArticleWithId(id: String): Resource<Article> {
        return localDataSource.getArticle(id)

    }

    companion object {
        const val TAG = "ArticleRepoImpl"
    }

}