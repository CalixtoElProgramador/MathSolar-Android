package com.listocalixto.android.mathsolar.data.source.article.remote

import com.listocalixto.android.mathsolar.app.Constants.FREE_NEWS_HEADER_API_KEY
import com.listocalixto.android.mathsolar.app.Constants.FREE_NEWS_HEADER_HOST
import com.listocalixto.android.mathsolar.app.Constants.FREE_NEWS_QUERY_LANG
import com.listocalixto.android.mathsolar.app.Constants.FREE_NEWS_QUERY_Q
import com.listocalixto.android.mathsolar.core.Resource
import com.listocalixto.android.mathsolar.data.model.Article
import com.listocalixto.android.mathsolar.data.model.ArticleContainer
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ArticleWebService {

    @GET("v1/search")
    suspend fun getArticles(
        @Query(FREE_NEWS_QUERY_Q) topic: String,
        @Query(FREE_NEWS_QUERY_LANG) lang: String,
        @Header(FREE_NEWS_HEADER_HOST) host: String,
        @Header(FREE_NEWS_HEADER_API_KEY) apiKey: String
    ):Response<ArticleContainer>

}