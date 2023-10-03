package com.mirodeon.genericapp.network.service

import com.mirodeon.genericapp.network.dto.DataWaifu
import com.mirodeon.genericapp.network.utils.UrlApi
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface WaifuService {
    @Headers("Content-type: application/json")
    @GET("search")
    suspend fun randomWaifus(
        @Query("is_nsfw", encoded = true) isNsfw: String,
        @Query("many", encoded = true) many: String
    ): Response<DataWaifu>

}

class WaifuServiceImpl : BaseService(UrlApi.waifuApi) {
    suspend fun getRandomWaifus(): Response<DataWaifu> =
        getRetrofit().create(WaifuService::class.java).randomWaifus("true", "true")
}