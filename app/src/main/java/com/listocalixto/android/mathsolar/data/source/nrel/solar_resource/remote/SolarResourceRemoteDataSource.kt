package com.listocalixto.android.mathsolar.data.source.nrel.solar_resource.remote

import android.content.Context
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.app.CoroutinesQualifiers.IoDispatcher
import com.listocalixto.android.mathsolar.core.Resource
import com.listocalixto.android.mathsolar.data.model.nrel.solar_resource.InputsSolarResource
import com.listocalixto.android.mathsolar.data.model.nrel.solar_resource.Outputs
import com.listocalixto.android.mathsolar.data.source.nrel.solar_resource.SolarResourceDataSource
import com.listocalixto.android.mathsolar.utils.ErrorMessage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.net.UnknownHostException
import javax.inject.Inject

class SolarResourceRemoteDataSource @Inject constructor(
    private val webService: SolarResourceWebService,
    @ApplicationContext private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : SolarResourceDataSource {

    override suspend fun getOutputs(inputs: InputsSolarResource): Resource<Outputs> =
        withContext(ioDispatcher) {
            return@withContext try {
                val response =
                    webService.getOutputs(context.resources.getString(inputs.apiKey), inputs.latitude, inputs.longitude)
                val data = response.body()
                when {
                    response.message().toString().contains("timeout") -> {
                        Resource.Error(ErrorMessage(message = context.getString(R.string.err_timeout)))
                    }
                    response.code() == 402 -> {
                        Resource.Error(ErrorMessage(message = context.getString(R.string.err_api_key_limited)))
                    }
                    data == null -> {
                        Resource.Error(ErrorMessage(message = context.getString(R.string.err_solar_resources_not_found)))
                    }
                    response.isSuccessful -> {
                        Resource.Success(data.outputs)
                    }
                    else -> {
                        Resource.Error(ErrorMessage(message = response.message()))
                    }
                }
            } catch (e: Exception) {
                when (e) {
                    is UnknownHostException -> {
                        Resource.Error(ErrorMessage(message = context.getString(R.string.err_nrel_api_unknow_host)))
                    }
                    else -> {
                        Resource.Error(ErrorMessage(exception = e))
                    }
                }
            }
        }
}