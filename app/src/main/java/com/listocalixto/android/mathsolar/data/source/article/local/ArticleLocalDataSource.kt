package com.listocalixto.android.mathsolar.data.source.article.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.app.CoroutinesQualifiers.IoDispatcher
import com.listocalixto.android.mathsolar.core.Resource
import com.listocalixto.android.mathsolar.data.model.Article
import com.listocalixto.android.mathsolar.data.source.article.ArticleDataSource
import com.listocalixto.android.mathsolar.utils.ArticleTopic
import com.listocalixto.android.mathsolar.utils.ErrorMessage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ArticleLocalDataSource @Inject constructor(
    private val dao: ArticleDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ArticleDataSource {

    override fun observeArticles(): LiveData<Resource<List<Article>>> {
        return dao.observePVProjects().map {
            Resource.Success(it)
        }
    }

    override fun observeArticle(articleId: String): LiveData<Resource<Article>> {
        return dao.observeProjectById(articleId).map {
            Resource.Success(it)
        }
    }

    override suspend fun getArticles(topic: ArticleTopic): Resource<List<Article>> =
        withContext(ioDispatcher) {
            return@withContext try {
                Resource.Success(dao.getArticles())
            } catch (e: Exception) {
                Resource.Error(ErrorMessage(exception = e))
            }
        }

    override suspend fun getArticle(articleId: String): Resource<Article> =
        withContext(ioDispatcher) {
            try {
                val article = dao.getArticleById(articleId)
                article?.let {
                    return@withContext Resource.Success(it)
                }
                    ?: return@withContext Resource.Error(ErrorMessage(stringRes = R.string.err_article_not_found))
            } catch (e: Exception) {
                Resource.Error(ErrorMessage(exception = e))
            }
        }

    override suspend fun refreshArticles(topic: ArticleTopic) {
        //NO-OP
    }

    override suspend fun refreshArticle(topic: ArticleTopic, articleId: String) {
        //NO-OP
    }

    override suspend fun saveArticle(article: Article) = withContext(ioDispatcher) {
        dao.insertArticle(article)
    }

    override suspend fun bookmarkArticle(article: Article) = withContext(ioDispatcher) {
        dao.updateBookmark(article.id, true)
    }

    override suspend fun bookmarkArticle(articleId: String) = withContext(ioDispatcher) {
        dao.updateBookmark(articleId, true)
    }

    override suspend fun deleteBookmarkArticle(article: Article) = withContext(ioDispatcher) {
        dao.updateBookmark(article.id, false)
    }

    override suspend fun deleteBookmarkArticle(articleId: String) = withContext(ioDispatcher) {
        dao.updateBookmark(articleId, false)
    }

    override suspend fun addArticleToHistory(article: Article) = withContext(ioDispatcher) {
        dao.updateViewed(article.id, true)
    }

    override suspend fun addArticleToHistory(articleId: String) = withContext(ioDispatcher) {
        dao.updateViewed(articleId, true)
    }

    override suspend fun deleteArticleFromHistory(article: Article) = withContext(ioDispatcher) {
        dao.updateViewed(article.id, false)
    }

    override suspend fun deleteArticleFromHistory(articleId: String) = withContext(ioDispatcher) {
        dao.updateViewed(articleId, false)
    }

    override suspend fun clearHistory() = withContext(ioDispatcher) {
        dao.deleteAllArticlesFromHistory()
    }

    override suspend fun deleteAllArticles() = withContext(ioDispatcher) { dao.deleteArticles() }

}