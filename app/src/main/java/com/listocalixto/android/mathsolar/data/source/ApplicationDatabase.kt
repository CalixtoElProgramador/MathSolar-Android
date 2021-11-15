package com.listocalixto.android.mathsolar.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.listocalixto.android.mathsolar.data.model.PVProject
import com.listocalixto.android.mathsolar.data.source.pv_project.local.PVProjectDao

@Database(entities = [PVProject::class], version = 1, exportSchema = false)
abstract class ApplicationDatabase: RoomDatabase() {

    abstract val pvProjectDao: PVProjectDao

}