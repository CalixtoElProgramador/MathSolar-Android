package com.listocalixto.android.mathsolar.data.source.article.remote

import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.app.Constants.FREE_NEWS_HEADER_API_KEY
import com.listocalixto.android.mathsolar.app.Constants.FREE_NEWS_HEADER_HOST
import com.listocalixto.android.mathsolar.app.Constants.FREE_NEWS_LANGUAGE
import com.listocalixto.android.mathsolar.app.CoroutinesQualifiers.IoDispatcher
import com.listocalixto.android.mathsolar.core.Resource
import com.listocalixto.android.mathsolar.data.model.Article
import com.listocalixto.android.mathsolar.data.model.ArticleContainer
import com.listocalixto.android.mathsolar.data.source.article.ArticleDataSource
import com.listocalixto.android.mathsolar.utils.ErrorMessage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ArticleRemoteDataSource @Inject constructor(
    private val webService: ArticleWebService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ArticleDataSource {

    override suspend fun getArticles(query: String): Resource<ArticleContainer> =
        withContext(ioDispatcher) {
            return@withContext try {
                val response = webService.getArticles(
                    query,
                    FREE_NEWS_LANGUAGE,
                    FREE_NEWS_HEADER_HOST,
                    FREE_NEWS_HEADER_API_KEY
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
                            Resource.Success(it)
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

    override suspend fun saveArticle(article: Article) {
        //NO-OP
    }

    override suspend fun deleteAllArticles() {
        // NO-OP
    }
}