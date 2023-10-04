package com.mirodeon.genericapp.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mirodeon.genericapp.adapter.TagSpinnerAdapter
import com.mirodeon.genericapp.adapter.WaifuAdapter
import com.mirodeon.genericapp.databinding.FragmentWaifuBinding
import com.mirodeon.genericapp.network.dto.WaifuDto
import com.mirodeon.genericapp.network.service.WaifuServiceImpl
import com.mirodeon.genericapp.room.entity.Tag
import com.mirodeon.genericapp.room.entity.WaifuWithTag
import com.mirodeon.genericapp.viewModel.WaifuViewModel
import com.mirodeon.genericapp.viewModel.WaifuViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class WaifuFragment : Fragment() {
    private var binding: FragmentWaifuBinding? = null
    private var recyclerView: RecyclerView? = null
    private val viewModel: WaifuViewModel by activityViewModels {
        WaifuViewModelFactory()
    }
    private var jobWaifu: Job? = null
    private var tagFilter: String = "All"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWaifuBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        switchSetup()
        activity?.let { setSpinner(it) }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onRefresh()
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    private fun toggleFav(item: WaifuWithTag) {
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.setWaifuFav(id = item.waifu.waifuId, isFav = !item.waifu.isFav)
        }
    }

    private fun setupRecyclerView() {
        recyclerView = binding?.containerRecycler
        recyclerView?.layoutManager = LinearLayoutManager(activity)
        val itemAdapter = WaifuAdapter { toggleFav(it) }
        recyclerView?.adapter = itemAdapter
        jobWaifu = lifecycle.coroutineScope.launch {
            viewModel.fullWaifu().collect { waifusInDb ->
                itemAdapter.submitList(waifusInDb)
            }
        }
    }

    private fun switchSetup() {
        binding?.switchFav?.setOnCheckedChangeListener { _, isChecked ->
            jobWaifu?.cancel()
            jobWaifu = if (isChecked) {
                lifecycle.coroutineScope.launch {
                    viewModel.fullWaifuIsFav(true).collect { waifusInDb ->
                        (recyclerView?.adapter as? WaifuAdapter)?.submitList(waifusInDb)
                    }
                }
            } else {
                lifecycle.coroutineScope.launch {
                    viewModel.fullWaifu().collect { waifusInDb ->
                        (recyclerView?.adapter as? WaifuAdapter)?.submitList(waifusInDb)
                    }
                }
            }
        }
    }

    private fun setSpinner(context: Context) {
        with(binding?.spinnerTags) {
            lifecycle.coroutineScope.launch {
                viewModel.fullTag().collect { tagsInDb ->
                    val tagsForSpinner = tagsInDb.toMutableList()
                    tagsForSpinner.add(0, Tag(-1, "All"))
                    this@with?.adapter = TagSpinnerAdapter(tagsForSpinner)

                    this@with?.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                val selectedItem = tagsForSpinner[position]
                                Toast.makeText(
                                    context,
                                    "${selectedItem.name} selected.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                jobWaifu?.cancel()
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                Toast.makeText(
                                    context,
                                    "Nothing selected.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                jobWaifu?.cancel()
                            }
                        }


                }
            }
        }
    }

    private fun changeJob() {
        jobWaifu?.cancel()
        jobWaifu = if (binding?.switchFav?.isChecked == true) {
            lifecycle.coroutineScope.launch {
                viewModel.fullWaifuIsFav(true).collect { waifusInDb ->
                    (recyclerView?.adapter as? WaifuAdapter)?.submitList(waifusInDb)
                }
            }
        } else {
            lifecycle.coroutineScope.launch {
                viewModel.fullWaifu().collect { waifusInDb ->
                    (recyclerView?.adapter as? WaifuAdapter)?.submitList(waifusInDb)
                }
            }
        }
    }

}