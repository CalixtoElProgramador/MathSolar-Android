package com.listocalixto.android.mathsolar.domain.article

import androidx.lifecycle.LiveData
import com.listocalixto.android.mathsolar.app.Constants.FREE_NEWS_TOPIC
import com.listocalixto.android.mathsolar.app.CoroutinesQualifiers.LocalDataSourceArticle
import com.listocalixto.android.mathsolar.app.CoroutinesQualifiers.RemoteDataSourceArticle
import com.listocalixto.android.mathsolar.app.CoroutinesQualifiers.IoDispatcher
import com.listocalixto.android.mathsolar.core.Resource
import com.listocalixto.android.mathsolar.data.model.Article
import com.listocalixto.android.mathsolar.data.source.article.ArticleDataSource
import com.listocalixto.android.mathsolar.utils.ErrorMessage
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


    override suspend fun getArticles(topic: String, forceUpdate: Boolean): Resource<List<Article>> {
        if (forceUpdate) {
            try {
                updateArticlesFromRemoteDataSource(topic)
            } catch (e: Exception) {
                Resource.Error(ErrorMessage(exception = e))
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

    override suspend fun refreshArticles(topic: String) {
        updateArticlesFromRemoteDataSource(topic)
    }

    override suspend fun refreshArticle(articleId: String) {
        updateArticleFromRemoteDataSource(articleId)
    }

    private suspend fun updateArticlesFromRemoteDataSource(topic: String) {
        val remoteProjects = remoteDataSource.getArticles(topic)
        if (remoteProjects is Resource.Success) {
            localDataSource.deleteAllArticles()
            remoteProjects.data.forEach { article ->
                localDataSource.saveArticle(article)
            }
        } else if (remoteProjects is Resource.Error) {
            throw remoteProjects.errorMessage.exception!!
        }
    }

    private suspend fun updateArticleFromRemoteDataSource(articleId: String) {
        val remoteArticle = remoteDataSource.getArticle(articleId)
        if (remoteArticle is Resource.Success) {
            localDataSource.saveArticle(remoteArticle.data)
        }
    }

    override suspend fun saveArticle(article: Article) = withContext(ioDispatcher) {
        val remoteArticles = remoteDataSource.getArticles(FREE_NEWS_TOPIC)
        if (remoteArticles is Resource.Success) {
            remoteArticles.data.forEach { article ->
                localDataSource.saveArticle(article)
            }
        } else if (remoteArticles is Resource.Error) {
            throw remoteArticles.errorMessage.exception!!
        }
    }

    override suspend fun bookmarkArticle(article: Article) {
        coroutineScope {
            launch { remoteDataSource.bookmarkArticle(article) }
            launch { localDataSource.bookmarkArticle(article) }
        }
    }

    override suspend fun bookmarkArticle(articleId: String) {
        coroutineScope {
            launch { remoteDataSource.bookmarkArticle(articleId) }
            launch { localDataSource.bookmarkArticle(articleId) }
        }
    }

    override suspend fun deleteBookmarkArticle(article: Article) {
        coroutineScope {
            launch { remoteDataSource.deleteBookmarkArticle(article) }
            launch { localDataSource.deleteBookmarkArticle(article) }
        }
    }

    override suspend fun deleteBookmarkArticle(articleId: String) {
        coroutineScope {
            launch { remoteDataSource.deleteBookmarkArticle(articleId) }
            launch { localDataSource.deleteBookmarkArticle(articleId) }
        }
    }

    override suspend fun addArticleToHistory(article: Article) {
        coroutineScope {
            launch { remoteDataSource.addArticleToHistory(article) }
            launch { localDataSource.addArticleToHistory(article) }
        }
    }

    override suspend fun addArticleToHistory(articleId: String) {
        coroutineScope {
            launch { remoteDataSource.addArticleToHistory(articleId) }
            launch { localDataSource.addArticleToHistory(articleId) }
        }
    }

    override suspend fun deleteArticleFromHistory(article: Article) {
        coroutineScope {
            launch { remoteDataSource.deleteArticleFromHistory(article) }
            launch { localDataSource.deleteArticleFromHistory(article) }
        }
    }

    override suspend fun deleteArticleFromHistory(articleId: String) {
        coroutineScope {
            launch { remoteDataSource.deleteArticleFromHistory(articleId) }
            launch { localDataSource.deleteArticleFromHistory(articleId) }
        }
    }

    override suspend fun deleteAllArticlesFromHistory() {
        coroutineScope {
            launch { remoteDataSource.deleteAllArticlesFromHistory() }
            launch { localDataSource.deleteAllArticlesFromHistory() }
        }
    }

    override suspend fun deleteAllArticles() {
        coroutineScope {
            launch { remoteDataSource.deleteAllArticles() }
            launch { localDataSource.deleteAllArticles() }
        }
    }
}