package com.listocalixto.android.mathsolar.data.source.pv_project.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.listocalixto.android.mathsolar.data.model.PVProject

/**
 * Data Access Object for the pv project table.
 */
@Dao
interface PVProjectDao {
    /**
     * Observes list of pv projects.
     *
     * @return all pv projects.
     */
    @Query("SELECT * FROM PV_Projects")
    fun observePVProjects(): LiveData<List<PVProject>>

    /**
     * Observes a single pv project.
     *
     * @param projectId the pv project id.
     * @return the pv project with projectId.
     */
    @Query("SELECT * FROM PV_Projects WHERE entry_id = :projectId")
    fun observeProjectById(projectId: String): LiveData<PVProject>

    /**
     * Select all pv projects from the pv_projects table.
     *
     * @return all pv projects.
     */
    @Query("SELECT * FROM PV_Projects")
    suspend fun getPVProjects(): List<PVProject>

    /**
     * Select a pv project by id.
     *
     * @param projectId the pv project id.
     * @return the pv project with projectId.
     */
    @Query("SELECT * FROM PV_Projects WHERE entry_id = :projectId")
    suspend fun getPVProjectById(projectId: String): PVProject?

    /**
     * Insert a pv project in the database. If the pv project already exists, replace it.
     *
     * @param project the pv project to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPVProject(project: PVProject)

    /**
     * Update a pv project.
     *
     * @param project pv project to be updated
     * @return the number of pv projects updated. This should always be 1.
     */
    @Update
    suspend fun updatePVProject(project: PVProject): Int

    /**
     * Update the favorite status of a pv project
     *
     * @param projectId    id of the pv project
     * @param favorite status to be updated
     */
    @Query("UPDATE pv_projects SET is_favorite = :favorite WHERE entry_id = :projectId")
    suspend fun updateFavorite(projectId: String, favorite: Boolean)

    /**
     * Delete a pv project by id.
     *
     * @param projectId the pv project id.
     * @return the number of pv projects deleted. This should always be 1.
     */
    @Query("DELETE FROM PV_Projects WHERE entry_id = :projectId")
    suspend fun deletePVProjectById(projectId: String): Int

    /**
     * Delete all projects.
     */
    @Query("DELETE FROM pv_projects")
    suspend fun deletePVProjects()

}