package com.listocalixto.android.mathsolar.domain.article

import com.listocalixto.android.mathsolar.core.Resource
import com.listocalixto.android.mathsolar.data.model.Article
import com.listocalixto.android.mathsolar.data.model.ArticleContainer

interface ArticleRepo {

    suspend fun getArticles(query: String): Resource<ArticleContainer>
    suspend fun saveArticle(article: Article)
    suspend fun deleteAllArticles()
}