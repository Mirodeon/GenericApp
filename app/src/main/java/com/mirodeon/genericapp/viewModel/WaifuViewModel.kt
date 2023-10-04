package com.mirodeon.genericapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mirodeon.genericapp.application.MyApp
import com.mirodeon.genericapp.network.dto.TagDto
import com.mirodeon.genericapp.network.dto.WaifuDto
import com.mirodeon.genericapp.network.service.WaifuServiceImpl
import com.mirodeon.genericapp.room.entity.Tag
import com.mirodeon.genericapp.room.entity.Waifu
import com.mirodeon.genericapp.room.entity.WaifuTagCrossRef
import com.mirodeon.genericapp.room.entity.WaifuWithTag
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class WaifuViewModel : ViewModel() {

    private val waifuService = WaifuServiceImpl()
    private val waifuDao = MyApp.instance.database.waifuDao()
    private val tagDao = MyApp.instance.database.tagDao()

    fun fullWaifu(): Flow<List<WaifuWithTag>> = waifuDao.getAll()

    fun fullTag(): Flow<List<Tag>> = tagDao.getAll()

    fun fullWaifuIsFav(isFav: Boolean): Flow<List<WaifuWithTag>> = waifuDao.getIsFav(isFav)

    fun setWaifuFav(id: Long, isFav: Boolean) = waifuDao.isFav(id, isFav)

    private fun getRandomWaifus(handler: (data: List<WaifuDto>?) -> Unit) {
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

    private fun insertNewWaifuData(allWaifu: List<WaifuWithTag>, waifuDto: WaifuDto): Long? {
        return if (allWaifu.find { waifu -> waifu.waifu.url == waifuDto.url } == null) {
            val waifuId = waifuDao.insert(
                Waifu(
                    source = waifuDto.source ?: "",
                    url = waifuDto.url ?: ""
                )
            )
            waifuId
        } else {
            null
        }
    }

    private fun insertNewTagData(allTag: List<Tag>, tagDto: TagDto): Long {
        allTag.find { tag -> tag.name == tagDto.name }?.let {
            return it.tagId
        } ?: run {
            return tagDao.insert(Tag(name = tagDto.name ?: ""))
        }
    }

    fun onRefresh() {
        getRandomWaifus { waifuData ->
            CoroutineScope(Dispatchers.IO).launch {
                val waifusInDb = waifuDao.getAllCold()

                waifuData?.forEach { singleWaifuData ->
                    insertNewWaifuData(waifusInDb, singleWaifuData)?.let { waifuId ->
                        val tagIds: MutableList<Long> = mutableListOf()
                        val tagsInDb = tagDao.getAllCold()

                        singleWaifuData.tags.forEach { singleTagData ->
                            tagIds.add(insertNewTagData(tagsInDb, singleTagData))
                        }

                        tagIds.forEach { tagId ->
                            waifuDao.insertRef(
                                WaifuTagCrossRef(
                                    waifuId = waifuId,
                                    tagId = tagId
                                )
                            )
                        }


                    }
                }


            }
        }
    }

}

class WaifuViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WaifuViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WaifuViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}