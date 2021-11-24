package com.listocalixto.android.mathsolar.domain.article

import androidx.core.content.ContextCompat
import com.google.api.Context
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.app.Constants.FREE_NEWS_QUERY
import com.listocalixto.android.mathsolar.app.CoroutinesQualifiers.LocalDataSourceArticle
import com.listocalixto.android.mathsolar.app.CoroutinesQualifiers.RemoteDataSourceArticle
import com.listocalixto.android.mathsolar.app.CoroutinesQualifiers.IoDispatcher
import com.listocalixto.android.mathsolar.core.Resource
import com.listocalixto.android.mathsolar.data.model.Article
import com.listocalixto.android.mathsolar.data.model.ArticleContainer
import com.listocalixto.android.mathsolar.data.source.article.ArticleDataSource
import com.listocalixto.android.mathsolar.utils.ErrorMessage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ArticleRepoImpl @Inject constructor(
    @RemoteDataSourceArticle private val remoteDataSource: ArticleDataSource,
    @LocalDataSourceArticle private val localDataSource: ArticleDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ArticleRepo {

    override suspend fun getArticles(query: String): Resource<ArticleContainer> =
        withContext(ioDispatcher) {
            return@withContext try {
                updateProjectsFromRemoteDataSource(query)
                localDataSource.getArticles(query)
            } catch (e: Exception) {
                Resource.Error(ErrorMessage(exception = e))
            }
        }

    private suspend fun updateProjectsFromRemoteDataSource(query: String) {
        val remoteProjects = remoteDataSource.getArticles(query)
        if (remoteProjects is Resource.Success) {
            localDataSource.deleteAllArticles()
            remoteProjects.data.articles.forEach { article ->
                localDataSource.saveArticle(article)
            }
        } else if (remoteProjects is Resource.Error) {
            throw remoteProjects.errorMessage.exception!!
        }
    }

    override suspend fun saveArticle(article: Article) = withContext(ioDispatcher) {
        val remoteArticles = remoteDataSource.getArticles(FREE_NEWS_QUERY)
        if (remoteArticles is Resource.Success) {
            remoteArticles.data.articles.forEach { article ->
                localDataSource.saveArticle(article)
            }
        } else if (remoteArticles is Resource.Error) {
            throw remoteArticles.errorMessage.exception!!
        }
    }

    override suspend fun deleteAllArticles() {
        //NO-OP
    }
}