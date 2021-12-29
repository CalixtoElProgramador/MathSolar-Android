package com.listocalixto.android.mathsolar.data.source.article.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.listocalixto.android.mathsolar.data.model.Article

@Dao
interface ArticleDao {

    /**
     * Observes list of articles.
     *
     * @return all articles.
     */
    @Query("SELECT * FROM ARTICLES ORDER BY published_date DESC")
    fun observePVProjects(): LiveData<List<Article>>

    /**
     * Observes a single article.
     *
     * @param articleId the article id.
     * @return the article with articleId.
     */
    @Query("SELECT * FROM ARTICLES WHERE _id = :articleId")
    fun observeProjectById(articleId: String): LiveData<Article>

    /**
     * Select all pv projects from the articles table.
     *
     * @return all articles
     */
    @Query("SELECT * FROM ARTICLES")
    suspend fun getArticles(): List<Article>

    /**
     * Select an article by id.
     *
     * @param articleId the article id.
     * @return the article with articleId.
     */
    @Query("SELECT * FROM ARTICLES WHERE _id = :articleId")
    suspend fun getArticleById(articleId: String): Article?

    /**
     * Insert an article in the database. If the article already exists, replace it.
     *
     * @param article the article to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArticle(article: Article)

    /**
     * Update an article.
     *
     * @param article article to be updated
     * @return the number of articles updated. This should always be 1.
     */
    @Update
    suspend fun updateArticle(article: Article): Int

    /**
     * Update the bookmark status of an article
     *
     * @param articleId    id of the pv project
     * @param bookmark status to be updated
     */
    @Query("UPDATE ARTICLES SET is_bookmark = :bookmark WHERE _id = :articleId")
    suspend fun updateBookmark(articleId: String, bookmark: Boolean)

    /**
     * Update the viewed status of an article
     *
     * @param articleId id of the pv project
     * @param viewed status to be updated
     */
    @Query("UPDATE ARTICLES SET is_viewed = :viewed WHERE _id = :articleId")
    suspend fun updateViewed(articleId: String, viewed: Boolean)

    /**
     * Update the viewed status of all articles
     *
     * @param viewed status to be updated
     */
    @Query("UPDATE ARTICLES SET is_viewed = :viewed")
    suspend fun deleteAllArticlesFromHistory(viewed: Boolean = false)

    /**
     * Delete all articles.
     */
    @Query("DELETE FROM ARTICLES")
    suspend fun deleteArticles()

}