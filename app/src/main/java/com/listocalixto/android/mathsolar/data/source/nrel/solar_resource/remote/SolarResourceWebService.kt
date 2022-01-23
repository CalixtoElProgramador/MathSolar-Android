package com.listocalixto.android.mathsolar.data.source.nrel.solar_resource.remote

import com.listocalixto.android.mathsolar.app.Constants.SOLAR_RESOURCE_QUERY_API_KEY
import com.listocalixto.android.mathsolar.app.Constants.SOLAR_RESOURCE_QUERY_LAT
import com.listocalixto.android.mathsolar.app.Constants.SOLAR_RESOURCE_QUERY_LON
import com.listocalixto.android.mathsolar.data.model.nrel.solar_resource.SolarResource
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SolarResourceWebService {

    @GET("v1.json")
    suspend fun getOutputs(
        @Query(SOLAR_RESOURCE_QUERY_API_KEY) apiKey: String,
        @Query(SOLAR_RESOURCE_QUERY_LAT) latitude: Double,
        @Query(SOLAR_RESOURCE_QUERY_LON) longitude: Double
    ): Response<SolarResource>

}