package com.mirodeon.genericapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mirodeon.genericapp.R
import com.mirodeon.genericapp.adapter.ItemFromFirstApiAdapter
import com.mirodeon.genericapp.databinding.FragmentListApiBinding
import com.mirodeon.genericapp.network.service.ExampleFirstApiServiceImpl
import com.mirodeon.genericapp.network.service.ExampleSecondApiServiceImpl
import com.mirodeon.genericapp.viewModel.itemFromApi.FromApiViewModel
import com.mirodeon.genericapp.viewModel.itemFromApi.FromApiViewModelFactory
import kotlinx.coroutines.launch

class ListApiFragment : Fragment() {
    private var binding: FragmentListApiBinding? = null
    private var recyclerView: RecyclerView? = null
    private val viewModel: FromApiViewModel by activityViewModels {
        FromApiViewModelFactory(
            ExampleFirstApiServiceImpl(),
            ExampleSecondApiServiceImpl()
        )
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
        recyclerView = binding?.containerRecycler
        recyclerView?.layoutManager = LinearLayoutManager(activity)
        val itemFromFirstApiAdapter = ItemFromFirstApiAdapter { navigateToDetails(it) }
        recyclerView?.adapter = itemFromFirstApiAdapter
        lifecycle.coroutineScope.launch {
            viewModel.getSomeDataFromFirstApi { data ->
                data?.toMutableList().let { listFromData ->
                    itemFromFirstApiAdapter.submitList(listFromData)
                }
            }
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    private fun navigateToDetails(id: String) {
        val directions = ListApiFragmentDirections.actionListApiFragmentToListDetailsApiFragment(id)
        findNavController().navigate(directions)
    }
}