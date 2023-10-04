package com.mirodeon.genericapp.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mirodeon.genericapp.R
import com.mirodeon.genericapp.application.MyApp
import com.mirodeon.genericapp.databinding.WaifuCellBinding
import com.mirodeon.genericapp.network.dto.WaifuDto
import com.mirodeon.genericapp.room.entity.WaifuWithTag
import com.squareup.picasso.Picasso

class WaifuAdapter(
    private val onItemClicked: (item: WaifuWithTag) -> Unit
) : ListAdapter<WaifuWithTag, WaifuAdapter.WaifuViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<WaifuWithTag>() {
            override fun areItemsTheSame(
                oldItem: WaifuWithTag,
                newItem: WaifuWithTag
            ): Boolean {
                return oldItem.waifu.waifuId == newItem.waifu.waifuId
            }

            override fun areContentsTheSame(
                oldItem: WaifuWithTag,
                newItem: WaifuWithTag
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

        fun bind(waifu: WaifuWithTag, onItemClicked: (item: WaifuWithTag) -> Unit) {
            waifu.waifu.url.let { setupImage(it) }
            binding.txtSource.text = waifu.waifu.source
            binding.txtTags.text = waifu.tags.joinToString(" - ") { it.name }
            binding.btnFav.imageTintList =
                ColorStateList.valueOf(
                    if (waifu.waifu.isFav)
                        ContextCompat.getColor(
                            MyApp.instance,
                            R.color.red
                        )
                    else
                        ContextCompat.getColor(
                            MyApp.instance,
                            R.color.black
                        )
                )
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