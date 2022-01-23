package com.listocalixto.android.mathsolar.data.model.nrel.solar_resource


import com.google.gson.annotations.SerializedName

data class Monthly(
    @SerializedName("jan")
    val jan: Double,
    @SerializedName("feb")
    val feb: Double,
    @SerializedName("mar")
    val mar: Double,
    @SerializedName("apr")
    val apr: Double,
    @SerializedName("may")
    val may: Double,
    @SerializedName("jun")
    val jun: Double,
    @SerializedName("jul")
    val jul: Double,
    @SerializedName("aug")
    val aug: Double,
    @SerializedName("sep")
    val sep: Double,
    @SerializedName("oct")
    val oct: Double,
    @SerializedName("nov")
    val nov: Double,
    @SerializedName("dec")
    val dec: Double
)