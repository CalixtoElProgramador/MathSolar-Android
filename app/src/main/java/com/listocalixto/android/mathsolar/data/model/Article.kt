package com.listocalixto.android.mathsolar.data.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.listocalixto.android.mathsolar.utils.ArticleTopic

@Entity(tableName = "articles")
data class Article @JvmOverloads constructor (
    @ColumnInfo(name = "clean_url")
    @SerializedName("clean_url")
    val cleanUrl: String? = "",

    @PrimaryKey
    @ColumnInfo(name = "_id")
    @SerializedName("_id")
    val id: String = "",

    @ColumnInfo(name = "link")
    @SerializedName("link")
    val link: String = "",

    @ColumnInfo(name = "media")
    @SerializedName("media")
    val media: String? = "",

    @ColumnInfo(name = "published_date")
    @SerializedName("published_date")
    val publishedDate: String = "",

    @ColumnInfo(name = "summary")
    @SerializedName("summary")
    val summary: String? = "",

    @ColumnInfo(name = "title")
    @SerializedName("title")
    val title: String = "",

    @ColumnInfo(name = "is_bookmark")
    val bookmark: Boolean = false,

    @ColumnInfo(name = "is_viewed")
    val viewed: Boolean = false,

    @ColumnInfo(name = "topic")
    val topic: ArticleTopic = ArticleTopic.SOLAR_POWER

)

data class ArticleContainer(
    @SerializedName("articles")
    val articles: List<Article>
)