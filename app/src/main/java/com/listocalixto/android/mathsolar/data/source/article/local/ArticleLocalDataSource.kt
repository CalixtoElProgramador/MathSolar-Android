package com.listocalixto.android.mathsolar.data.source.article.local

import com.listocalixto.android.mathsolar.app.CoroutinesQualifiers.IoDispatcher
import com.listocalixto.android.mathsolar.core.Resource
import com.listocalixto.android.mathsolar.data.model.Article
import com.listocalixto.android.mathsolar.data.model.ArticleContainer
import com.listocalixto.android.mathsolar.data.source.article.ArticleDataSource
import com.listocalixto.android.mathsolar.utils.ErrorMessage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ArticleLocalDataSource @Inject constructor(
    private val dao: ArticleDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ArticleDataSource {

    override suspend fun getArticles(query: String): Resource<ArticleContainer> =
        withContext(ioDispatcher) {
            return@withContext try {
                Resource.Success(ArticleContainer(dao.getArticles()))
            } catch (e: Exception) {
                Resource.Error(ErrorMessage(exception = e))
            }
        }

    override suspend fun saveArticle(article: Article) = withContext(ioDispatcher) {
        dao.insertArticle(article)
    }

    override suspend fun deleteAllArticles() = withContext(ioDispatcher) { dao.deleteArticles() }
}