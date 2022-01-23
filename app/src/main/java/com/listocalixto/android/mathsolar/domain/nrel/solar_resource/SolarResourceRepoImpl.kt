package com.listocalixto.android.mathsolar.domain.nrel.solar_resource

import com.listocalixto.android.mathsolar.core.Resource
import com.listocalixto.android.mathsolar.data.model.nrel.solar_resource.InputsSolarResource
import com.listocalixto.android.mathsolar.data.model.nrel.solar_resource.Outputs
import com.listocalixto.android.mathsolar.data.source.nrel.solar_resource.SolarResourceDataSource
import javax.inject.Inject

class SolarResourceRepoImpl @Inject constructor(
    private val remoteDataSource: SolarResourceDataSource
) : SolarResourceRepo {

    override suspend fun getOutputs(inputs: InputsSolarResource): Resource<Outputs> =
        remoteDataSource.getOutputs(inputs)
}