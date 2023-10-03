package com.mirodeon.genericapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mirodeon.genericapp.network.dto.Waifu
import com.mirodeon.genericapp.network.service.WaifuServiceImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class WaifuViewModel(
    private val waifuService: WaifuServiceImpl
) : ViewModel() {

    fun getRandomWaifus(handler: (data: List<Waifu>?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = waifuService.getRandomWaifus()
            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        val res = response.body()?.images
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

class WaifuViewModelFactory(
    private val waifuService: WaifuServiceImpl
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WaifuViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WaifuViewModel(waifuService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}