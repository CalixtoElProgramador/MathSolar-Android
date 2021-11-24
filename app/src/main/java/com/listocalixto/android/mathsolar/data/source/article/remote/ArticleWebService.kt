package com.listocalixto.android.mathsolar.data.source.article.remote

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
        @Query("q") query: String,
        @Query("lang") lang: String,
        @Header("x-rapidapi-host") host: String,
        @Header("x-rapidapi-key") apiKey: String
    ):Response<ArticleContainer>

}