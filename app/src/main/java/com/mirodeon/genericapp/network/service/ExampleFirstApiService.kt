package com.mirodeon.genericapp.network.service

import com.mirodeon.genericapp.network.dto.Data
import com.mirodeon.genericapp.network.dto.ItemFromFirstApi
import com.mirodeon.genericapp.network.utils.UrlApi
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface ExampleFirstApiService {
    @Headers("Content-type: application/json")
    @GET("assets/")
    suspend fun someDataFromFirstApi(): Response<Data<Array<ItemFromFirstApi>>>
}

class ExampleFirstApiServiceImpl : BaseService(UrlApi.UrlFirstApi) {
    suspend fun getSomeDataFromFirstApi(): Response<Data<Array<ItemFromFirstApi>>> =
        getRetrofit().create(ExampleFirstApiService::class.java).someDataFromFirstApi()
}