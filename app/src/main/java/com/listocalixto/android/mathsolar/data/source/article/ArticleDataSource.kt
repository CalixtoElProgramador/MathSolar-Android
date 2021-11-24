package com.listocalixto.android.mathsolar.data.source.article

import androidx.lifecycle.LiveData
import com.listocalixto.android.mathsolar.core.Resource
import com.listocalixto.android.mathsolar.data.model.Article
import com.listocalixto.android.mathsolar.data.model.ArticleContainer
import com.listocalixto.android.mathsolar.data.model.PVProject
import retrofit2.Response

interface ArticleDataSource {

    //fun observeArticles(): LiveData<Resource<List<Article>>>

    //fun observeArticle(articleId: String): LiveData<Resource<Article>>

    suspend fun getArticles(query: String): Resource<ArticleContainer>

    //suspend fun getArticle(articleId: String): Resource<Article>

    //suspend fun refreshArticles()

    //suspend fun refreshArticle(articleId: String)

    suspend fun saveArticle(article: Article)

    suspend fun deleteAllArticles()

}