package com.mirodeon.genericapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mirodeon.genericapp.adapter.ItemFromFirstApiAdapter
import com.mirodeon.genericapp.databinding.FragmentListApiBinding
import com.mirodeon.genericapp.network.dto.ItemFromFirstApi
import com.mirodeon.genericapp.network.service.ExampleFirstApiServiceImpl
import com.mirodeon.genericapp.viewModel.itemFromApi.FromApiViewModel
import com.mirodeon.genericapp.viewModel.itemFromApi.FromApiViewModelFactory
import kotlinx.coroutines.launch

class ListApiFragment : Fragment() {
    private var binding: FragmentListApiBinding? = null
    private var recyclerView: RecyclerView? = null
    private val viewModel: FromApiViewModel by activityViewModels {
        FromApiViewModelFactory(ExampleFirstApiServiceImpl())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListApiBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        actualizeDataRecycler()
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    private fun navigateToDetails(item: ItemFromFirstApi) {
        val directions =
            ListApiFragmentDirections.actionListApiFragmentToListDetailsApiFragment(item)
        findNavController().navigate(directions)
    }

    private fun setupRecyclerView() {
        recyclerView = binding?.containerRecycler
        recyclerView?.layoutManager = LinearLayoutManager(activity)
        val itemAdapter = ItemFromFirstApiAdapter { navigateToDetails(it) }
        recyclerView?.adapter = itemAdapter
    }

    private fun actualizeDataRecycler() {
        lifecycle.coroutineScope.launch {
            viewModel.getSomeDataFromFirstApi { data ->
                data?.toMutableList().let { listFromData ->
                    (recyclerView?.adapter as? ItemFromFirstApiAdapter)?.submitList(listFromData)
                }
            }
        }
    }
}