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
    @GET("assets/{id}/")
    suspend fun specificDataFromSecondApi(
        @Path("id") id: String
    ): Response<Data<ItemFromSecondApi>>
}

class ExampleSecondApiServiceImpl : BaseService(UrlApi.UrlSecondApi) {
    suspend fun getSpecificDataFromSecondApi(id: String): Response<Data<ItemFromSecondApi>> =
        getRetrofit().create(ExampleSecondApiService::class.java).specificDataFromSecondApi(id)
}