package com.listocalixto.android.mathsolar.data.source.article

import androidx.lifecycle.LiveData
import com.listocalixto.android.mathsolar.core.Resource
import com.listocalixto.android.mathsolar.data.model.Article
import com.listocalixto.android.mathsolar.data.model.ArticleContainer
import com.listocalixto.android.mathsolar.data.model.PVProject
import retrofit2.Response

interface ArticleDataSource {

    fun observeArticles(): LiveData<Resource<List<Article>>>

    fun observeArticle(articleId: String): LiveData<Resource<Article>>

    suspend fun getArticles(topic: String): Resource<List<Article>>

    suspend fun getArticle(articleId: String): Resource<Article>

    suspend fun refreshArticles(topic: String)

    suspend fun refreshArticle(topic:String, articleId: String)

    suspend fun saveArticle(article: Article)

    suspend fun bookmarkArticle(article: Article)

    suspend fun bookmarkArticle(articleId: String)

    suspend fun deleteBookmarkArticle(article: Article)

    suspend fun deleteBookmarkArticle(articleId: String)

    suspend fun addArticleToHistory(article: Article)

    suspend fun addArticleToHistory(articleId: String)

    suspend fun deleteArticleFromHistory(article: Article)

    suspend fun deleteArticleFromHistory(articleId: String)

    suspend fun deleteAllArticlesFromHistory()

    suspend fun deleteAllArticles()

}