package com.mirodeon.genericapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mirodeon.genericapp.R
import com.mirodeon.genericapp.adapter.ItemFromFirstApiAdapter
import com.mirodeon.genericapp.adapter.ItemFromSecondApiAdapter
import com.mirodeon.genericapp.databinding.FragmentListApiBinding
import com.mirodeon.genericapp.databinding.FragmentListDetailsApiBinding
import com.mirodeon.genericapp.network.service.ExampleFirstApiServiceImpl
import com.mirodeon.genericapp.viewModel.itemFromApi.FromApiViewModel
import com.mirodeon.genericapp.viewModel.itemFromApi.FromApiViewModelFactory
import kotlinx.coroutines.launch

class ListDetailsApiFragment : Fragment() {
    private val args: ListDetailsApiFragmentArgs by navArgs()
    private var binding: FragmentListDetailsApiBinding? = null
    private var recyclerView: RecyclerView? = null
    private val viewModel: FromApiViewModel by activityViewModels {
        FromApiViewModelFactory(ExampleFirstApiServiceImpl())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListDetailsApiBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setContent()
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

    private fun setContent() {
        binding?.txtSymbol?.text = args.itemfromFirstApi.symbol
        binding?.txtName?.text = args.itemfromFirstApi.name
    }

    private fun setupRecyclerView() {
        recyclerView = binding?.containerRecyclerDetails
        recyclerView?.layoutManager = LinearLayoutManager(activity)
        val itemAdapter = ItemFromSecondApiAdapter {}
        recyclerView?.adapter = itemAdapter
    }

    private fun actualizeDataRecycler() {
        lifecycle.coroutineScope.launch {
            args.itemfromFirstApi.id?.let {
                viewModel.getSpecificDataFromFirstApi(it) { data ->
                    data?.toMutableList().let { listFromData ->
                        listFromData?.sortedBy { it.time }
                        (recyclerView?.adapter as? ItemFromSecondApiAdapter)?.submitList(
                            listFromData
                        )
                    }
                }
            }
        }
    }
}