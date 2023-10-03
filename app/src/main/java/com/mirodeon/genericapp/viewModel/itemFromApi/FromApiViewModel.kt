package com.mirodeon.genericapp.viewModel.itemFromApi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mirodeon.genericapp.network.dto.ItemFromFirstApi
import com.mirodeon.genericapp.network.dto.ItemFromSecondApi
import com.mirodeon.genericapp.network.service.ExampleFirstApiServiceImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class FromApiViewModel(
    private val firstApiService: ExampleFirstApiServiceImpl
) : ViewModel() {

    fun getSomeDataFromFirstApi(handler: (data: Array<ItemFromFirstApi>?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = firstApiService.getSomeDataFromFirstApi()
            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        val res = response.body()?.data
                        handler(res)
                    }
                } catch (e: HttpException) {
                    print(e)
                } catch (e: Throwable) {
                    print(e)
                }
            }
        }
    }

    fun getSpecificDataFromFirstApi(
        id: String,
        handler: (data: Array<ItemFromSecondApi>?) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = firstApiService.getSpecificDataFromFirstApi(id)
            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        val res = response.body()?.data
                        handler(res)
                    }
                } catch (e: HttpException) {
                    print(e)
                } catch (e: Throwable) {
                    print(e)
                }
            }
        }
    }

}

class FromApiViewModelFactory(
    private val firstApiService: ExampleFirstApiServiceImpl
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FromApiViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FromApiViewModel(firstApiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}