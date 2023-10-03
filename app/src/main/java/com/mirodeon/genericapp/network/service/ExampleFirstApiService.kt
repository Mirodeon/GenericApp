package com.mirodeon.genericapp.network.service

import com.mirodeon.genericapp.network.dto.Data
import com.mirodeon.genericapp.network.dto.ItemFromFirstApi
import com.mirodeon.genericapp.network.dto.ItemFromSecondApi
import com.mirodeon.genericapp.network.utils.UrlApi
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import javax.inject.Singleton

@Singleton
interface ExampleFirstApiService {
    @Headers("Content-type: application/json")
    @GET("assets/")
    suspend fun someDataFromFirstApi(): Response<Data<Array<ItemFromFirstApi>>>

    @Headers("Content-type: application/json")
    @GET("assets/{id}/history?interval=d1")
    suspend fun specificDataFromFirstApi(
        @Path("id") id: String
    ): Response<Data<Array<ItemFromSecondApi>>>
}

class ExampleFirstApiServiceImpl : BaseService(UrlApi.UrlFirstApi) {
    suspend fun getSomeDataFromFirstApi(): Response<Data<Array<ItemFromFirstApi>>> =
        getRetrofit().create(ExampleFirstApiService::class.java).someDataFromFirstApi()

    suspend fun getSpecificDataFromFirstApi(id: String): Response<Data<Array<ItemFromSecondApi>>> =
        getRetrofit().create(ExampleFirstApiService::class.java).specificDataFromFirstApi(id)
}