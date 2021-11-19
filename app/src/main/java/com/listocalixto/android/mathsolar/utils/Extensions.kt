package com.listocalixto.android.mathsolar.utils

import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.google.android.material.color.MaterialColors
import com.google.android.material.snackbar.Snackbar
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.data.model.PVProject
import com.listocalixto.android.mathsolar.data.model.PVProjectRemote

fun PVProject.asRemoteModel(downloadUrl: String, userUid: String): PVProjectRemote =
    PVProjectRemote(
        name = this.name,
        location = this.location,
        createdAt = this.createdAt,
        description = this.description,
        imageUrl = downloadUrl,
        type = this.type.ordinal,
        deleted = this.isDeleted,
        favorite = this.isFavorite,
        ambTemperature = this.ambTemperature,
        batteryCapacity = this.batteryCapacity,
        batteryDepthDischarge = this.batteryDepthDischarge,
        batteryVoltage = this.batteryVoltage,
        daysAutonomy = this.daysAutonomy,
        inverterEfficiency = this.inverterEfficiency,
        loadMax = this.loadMax,
        loadMonth = this.loadMonth,
        latitude = this.latitude,
        longitude = this.longitude,
        moduleCurrent = this.moduleCurrent,
        modulePower = this.modulePower,
        moduleVoltage = this.moduleVoltage,
        sunHours = this.sunHours,
        uid = this.uid,
        userUid = userUid
    )

fun PVProjectRemote.asLocalModel(): PVProject =
    PVProject(
        this.name,
        this.location,
        this.createdAt,
        this.description,
        this.imageUrl,
        type = when (this.type) {
            0 -> {
                PVProjectType.ALL_PROJECTS
            }
            1 -> {
                PVProjectType.CONNECTED_TO_THE_GRID
            }
            2 -> {
                PVProjectType.HYBRID
            }
            3 -> {
                PVProjectType.ISOLATED
            }
            else -> {
                PVProjectType.FAVORITE
            }
        },
        this.deleted,
        this.favorite,
        this.ambTemperature,
        this.batteryCapacity,
        this.batteryDepthDischarge,
        this.batteryVoltage,
        this.daysAutonomy,
        this.inverterEfficiency,
        this.loadMax,
        this.loadMonth,
        this.latitude,
        this.longitude,
        this.moduleCurrent,
        this.modulePower,
        this.moduleVoltage,
        this.sunHours,
        this.uid
    )

fun List<PVProjectRemote>.asLocalModel(): List<PVProject> {
    val resultList = mutableListOf<PVProject>()
    this.forEach { pvProjectRemote ->
        resultList.add(pvProjectRemote.asLocalModel())
    }
    return resultList
}

/*
fun List<PVProject>.asRemoteModel(downloadUrl: String, userUid: String): List<PVProjectRemote> {
    val resultList = mutableListOf<PVProjectRemote>()
    this.forEach { pvProject ->
        resultList.add(pvProject.asRemoteModel(downloadUrl, userUid))
    }
    return resultList
}
*/

/**
 * Transforms static java function Snackbar.make() to an extension function on View.
 */
fun View.showSnackbar(snackbarText: String, timeLength: Int, anchorView: View, isError: Boolean) {
    Snackbar.make(this, snackbarText, timeLength).run {
        if (isError) {
            setBackgroundTint(MaterialColors.getColor(this@showSnackbar, R.attr.colorError))
            setTextColor(MaterialColors.getColor(this@showSnackbar, R.attr.colorOnError))
            setAction(R.string.ok) { this.dismiss() }
            setActionTextColor(MaterialColors.getColor(this@showSnackbar, R.attr.colorOnError))
        }
        setAnchorView(anchorView)
        show()
    }
}

/**
 * Triggers a snackbar message when the value contained by snackbarTaskMessageLiveEvent is modified.
 */
fun View.setupSnackbar(
    lifecycleOwner: LifecycleOwner,
    snackbarEvent: LiveData<Event<Int>>,
    timeLength: Int,
    anchorView: View,
    isError: Boolean
) {

    snackbarEvent.observe(lifecycleOwner, { event ->
        event.getContentIfNotHandled()?.let {
            showSnackbar(context.getString(it), timeLength, anchorView, isError)
        }
    })
}
