package com.listocalixto.android.mathsolar.data.source.article.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.app.Constants.FREE_NEWS_API_KEY
import com.listocalixto.android.mathsolar.app.Constants.FREE_NEWS_HOST
import com.listocalixto.android.mathsolar.app.Constants.FREE_NEWS_LANG
import com.listocalixto.android.mathsolar.app.CoroutinesQualifiers.IoDispatcher
import com.listocalixto.android.mathsolar.core.Resource
import com.listocalixto.android.mathsolar.data.model.Article
import com.listocalixto.android.mathsolar.data.source.article.ArticleDataSource
import com.listocalixto.android.mathsolar.utils.ErrorMessage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ArticleRemoteDataSource @Inject constructor(
    private val webService: ArticleWebService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ArticleDataSource {

    private val observableArticles = MutableLiveData<Resource<List<Article>>>()

    override fun observeArticles(): LiveData<Resource<List<Article>>> {
        return observableArticles
    }

    override fun observeArticle(articleId: String): LiveData<Resource<Article>> {
        return observableArticles.map { articles ->
            when (articles) {
                is Resource.Loading -> Resource.Loading()
                is Resource.Error -> Resource.Error(ErrorMessage(exception = articles.errorMessage.exception))
                is Resource.Success -> {
                    val article = articles.data.firstOrNull { it.id == articleId }
                        ?: return@map Resource.Error(ErrorMessage(stringRes = R.string.err_article_not_found))
                    Resource.Success(article)
                }
            }
        }
    }

    override suspend fun getArticles(topic: String): Resource<List<Article>> =
        withContext(ioDispatcher) {
            return@withContext try {
                val response = webService.getArticles(
                    topic,
                    FREE_NEWS_LANG,
                    FREE_NEWS_HOST,
                    FREE_NEWS_API_KEY
                )
                when {
                    response.message().toString().contains("timeout") -> {
                        Resource.Error(ErrorMessage(stringRes = R.string.err_timeout))
                    }
                    response.code() == 402 -> {
                        Resource.Error(ErrorMessage(stringRes = R.string.err_api_key_limited))
                    }
                    response.body()?.articles.isNullOrEmpty() -> {
                        Resource.Error(ErrorMessage(stringRes = R.string.err_articles_not_found))
                    }
                    response.isSuccessful -> {
                        response.body()?.let {
                            Resource.Success(it.articles)
                        } ?: Resource.Error(ErrorMessage(message = response.message()))
                    }
                    else -> {
                        Resource.Error(ErrorMessage(message = response.message()))
                    }
                }
            } catch (e: Exception) {
                Resource.Error(ErrorMessage(exception = e))
            }
        }

    override suspend fun getArticle(articleId: String): Resource<Article> =
        withContext(ioDispatcher) {
            // Isn't possible request an article by Id to the webService
            return@withContext Resource.Success(Article())
        }

    override suspend fun refreshArticles(topic: String) = withContext(ioDispatcher) {
        getArticles(topic).let { observableArticles.value = it }
    }

    override suspend fun refreshArticle(topic: String, articleId: String) =
        withContext(ioDispatcher) {
            refreshArticles(topic)
        }

    override suspend fun saveArticle(article: Article) {
        //NO-OP - FIREBASE
    }

    override suspend fun bookmarkArticle(article: Article) {
        //NO-OP - FIREBASE
    }

    override suspend fun bookmarkArticle(articleId: String) {
        //NO-OP - FIREBASE - NO REQUIRED
    }

    override suspend fun deleteBookmarkArticle(article: Article) {
        //NO-OP - FIREBASE
    }

    override suspend fun deleteBookmarkArticle(articleId: String) {
        //NO-OP - FIREBASE - NO REQUIRED
    }

    override suspend fun addArticleToHistory(article: Article) {
        //NO-OP - FIREBASE
    }

    override suspend fun addArticleToHistory(articleId: String) {
        //NO-OP - FIREBASE - NO REQUIRED
    }

    override suspend fun deleteArticleFromHistory(article: Article) {
        //NO-OP - FIREBASE
    }

    override suspend fun deleteArticleFromHistory(articleId: String) {
        //NO-OP - FIREBASE - NO REQUIRED
    }

    override suspend fun deleteAllArticlesFromHistory() {
        //NO-OP - FIREBASE
    }

    override suspend fun deleteAllArticles() {
        //NO-OP - FIREBASE
    }
}