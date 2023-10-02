package com.mirodeon.genericapp.network.service

import com.mirodeon.genericapp.network.dto.Data
import com.mirodeon.genericapp.network.dto.ItemFromSecondApi
import com.mirodeon.genericapp.network.utils.UrlApi
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface ExampleSecondApiService {
    @Headers("Content-type: application/json")
    @GET("assets/{id}/history?interval=d1")
    suspend fun specificDataFromSecondApi(
        @Path("id") id: String
    ): Response<Data<Array<ItemFromSecondApi>>>
}

class ExampleSecondApiServiceImpl : BaseService(UrlApi.UrlFirstApi) {
    suspend fun getSpecificDataFromSecondApi(id: String): Response<Data<Array<ItemFromSecondApi>>> =
        getRetrofit().create(ExampleSecondApiService::class.java).specificDataFromSecondApi(id)
}