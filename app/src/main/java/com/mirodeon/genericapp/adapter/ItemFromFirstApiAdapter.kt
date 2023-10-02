package com.mirodeon.genericapp.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mirodeon.genericapp.databinding.ItemFromFirstApiBinding
import com.mirodeon.genericapp.network.dto.ItemFromFirstApi
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class ItemFromFirstApiAdapter(
    private val onItemClicked: (id: String) -> Unit
) : ListAdapter<ItemFromFirstApi, ItemFromFirstApiAdapter.ItemFromFirstApiViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ItemFromFirstApi>() {
            override fun areItemsTheSame(
                oldItem: ItemFromFirstApi,
                newItem: ItemFromFirstApi
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ItemFromFirstApi,
                newItem: ItemFromFirstApi
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemFromFirstApiViewHolder {
        val viewHolder = ItemFromFirstApiViewHolder(
            ItemFromFirstApiBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            getItem(position).id?.let { id -> onItemClicked(id) }
        }
        return viewHolder
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ItemFromFirstApiViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ItemFromFirstApiViewHolder(
        private var binding: ItemFromFirstApiBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(itemFromFirstApi: ItemFromFirstApi) {

        }
    }
}