package com.mirodeon.genericapp.viewModel.itemFromApi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mirodeon.genericapp.network.dto.ItemFromFirstApi
import com.mirodeon.genericapp.network.dto.ItemFromSecondApi
import com.mirodeon.genericapp.network.service.ExampleFirstApiServiceImpl
import com.mirodeon.genericapp.network.service.ExampleSecondApiServiceImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class FromApiViewModel(
    private val firstApiService: ExampleFirstApiServiceImpl,
    private val secondApiService: ExampleSecondApiServiceImpl,
) : ViewModel() {

    private fun getSomeDataFromFirstApi(handler: (data: Array<ItemFromFirstApi>?) -> Unit) {
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

    private fun getSpecificDataFromSecondApi(
        id: String,
        handler: (data: Array<ItemFromSecondApi>?) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = secondApiService.getSpecificDataFromSecondApi(id)
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
    private val firstApiService: ExampleFirstApiServiceImpl,
    private val secondApiService: ExampleSecondApiServiceImpl,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FromApiViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FromApiViewModel(firstApiService, secondApiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}