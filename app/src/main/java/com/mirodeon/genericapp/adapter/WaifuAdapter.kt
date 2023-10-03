package com.mirodeon.genericapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mirodeon.genericapp.R
import com.mirodeon.genericapp.databinding.WaifuCellBinding
import com.mirodeon.genericapp.network.dto.Waifu
import com.squareup.picasso.Picasso

class WaifuAdapter(
    private val onItemClicked: (item: Waifu) -> Unit
) : ListAdapter<Waifu, WaifuAdapter.WaifuViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Waifu>() {
            override fun areItemsTheSame(
                oldItem: Waifu,
                newItem: Waifu
            ): Boolean {
                return oldItem.url == newItem.url
            }

            override fun areContentsTheSame(
                oldItem: Waifu,
                newItem: Waifu
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WaifuViewHolder {
        val viewHolder = WaifuViewHolder(
            WaifuCellBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        /*viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            onItemClicked(getItem(position))
        }*/
        return viewHolder
    }

    override fun onBindViewHolder(holder: WaifuViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClicked)
    }

    class WaifuViewHolder(
        private var binding: WaifuCellBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(waifu: Waifu, onItemClicked: (item: Waifu) -> Unit) {
            waifu.url?.let { setupImage(it) }
            binding.txtSource.text = waifu.source
            binding.txtTags.text = waifu.tags.map { it.name }.joinToString(" - ")
            binding.btnFav.setOnClickListener {
                onItemClicked(waifu)
            }
        }

        private fun setupImage(url: String) {
            Picasso.get()
                .load(url)
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder)
                .fit()
                .centerCrop()
                .into(binding.imgWaifu)
        }

    }
}