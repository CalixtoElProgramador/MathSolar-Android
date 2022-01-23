package com.listocalixto.android.mathsolar.domain.nrel.solar_resource

import com.listocalixto.android.mathsolar.core.Resource
import com.listocalixto.android.mathsolar.data.model.nrel.solar_resource.InputsSolarResource
import com.listocalixto.android.mathsolar.data.model.nrel.solar_resource.Outputs

interface SolarResourceRepo {

    suspend fun getOutputs(inputs: InputsSolarResource): Resource<Outputs>

}