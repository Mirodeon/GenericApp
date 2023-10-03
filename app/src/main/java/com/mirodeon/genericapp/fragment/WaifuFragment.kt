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
import com.mirodeon.genericapp.adapter.WaifuAdapter
import com.mirodeon.genericapp.databinding.FragmentWaifuBinding
import com.mirodeon.genericapp.network.dto.Waifu
import com.mirodeon.genericapp.network.service.WaifuServiceImpl
import com.mirodeon.genericapp.viewModel.WaifuViewModel
import com.mirodeon.genericapp.viewModel.WaifuViewModelFactory
import kotlinx.coroutines.launch

class WaifuFragment : Fragment() {
    private var binding: FragmentWaifuBinding? = null
    private var recyclerView: RecyclerView? = null
    private val viewModel: WaifuViewModel by activityViewModels {
        WaifuViewModelFactory(WaifuServiceImpl())
    }

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
    }

    override fun onResume() {
        super.onResume()
        actualizeDataRecycler()
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    private fun toggleFav(item: Waifu) {
        /*val directions =
            ListApiFragmentDirections.actionListApiFragmentToListDetailsApiFragment(item)
        findNavController().navigate(directions)*/
    }

    private fun setupRecyclerView() {
        recyclerView = binding?.containerRecycler
        recyclerView?.layoutManager = LinearLayoutManager(activity)
        val itemAdapter = WaifuAdapter { toggleFav(it) }
        recyclerView?.adapter = itemAdapter
    }

    private fun actualizeDataRecycler() {
        lifecycle.coroutineScope.launch {
            viewModel.getRandomWaifus { data ->
                data?.toMutableList().let { listFromData ->
                    (recyclerView?.adapter as? WaifuAdapter)?.submitList(listFromData)
                }
            }
        }
    }

}