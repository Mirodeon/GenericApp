package com.mirodeon.genericapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mirodeon.genericapp.R
import com.mirodeon.genericapp.databinding.ItemFromFirstApiBinding
import com.mirodeon.genericapp.network.dto.ItemFromFirstApi
import java.math.RoundingMode
import java.text.DecimalFormat

class ItemFromFirstApiAdapter(
    private val onItemClicked: (item: ItemFromFirstApi) -> Unit
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
            onItemClicked(getItem(position))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ItemFromFirstApiViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ItemFromFirstApiViewHolder(
        private var binding: ItemFromFirstApiBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(itemFromFirstApi: ItemFromFirstApi) {
            binding.txtSymbol.text = itemFromFirstApi.symbol
            binding.txtName.text = itemFromFirstApi.name
            setPercent(itemFromFirstApi.changePercent24Hr)
        }

        private fun setPercent(percent: String?) {
            val number = percent?.toDoubleOrNull()
            val result = number?.let { roundOffDecimal(it) }
            result?.let {
                binding.txtPercent.text = String.format(
                    binding.root.context.getString(R.string.percent),
                    it.toString()
                )
                binding.txtPercent.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        if (it > 0) R.color.green else R.color.red
                    )
                )
            }
        }

        private fun roundOffDecimal(number: Double): Double {
            val df = DecimalFormat("###.#")
            df.roundingMode = RoundingMode.CEILING
            return df.format(number).toDouble()
        }
    }
}