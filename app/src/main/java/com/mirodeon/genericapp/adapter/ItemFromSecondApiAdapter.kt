package com.mirodeon.genericapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mirodeon.genericapp.R
import com.mirodeon.genericapp.databinding.ItemFromSecondApiBinding
import com.mirodeon.genericapp.network.dto.ItemFromSecondApi
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ItemFromSecondApiAdapter(
    private val onItemClicked: (ItemFromSecondApi) -> Unit
) : ListAdapter<ItemFromSecondApi, ItemFromSecondApiAdapter.ItemFromSecondApiViewHolder>(
    DiffCallback
) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ItemFromSecondApi>() {
            override fun areItemsTheSame(
                oldItem: ItemFromSecondApi,
                newItem: ItemFromSecondApi
            ): Boolean {
                return oldItem.time == newItem.time
            }

            override fun areContentsTheSame(
                oldItem: ItemFromSecondApi,
                newItem: ItemFromSecondApi
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemFromSecondApiViewHolder {
        val viewHolder = ItemFromSecondApiViewHolder(
            ItemFromSecondApiBinding.inflate(
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

    override fun onBindViewHolder(holder: ItemFromSecondApiViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ItemFromSecondApiViewHolder(
        private var binding: ItemFromSecondApiBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(itemFromSecondApi: ItemFromSecondApi) {
            itemFromSecondApi.time?.let { setTime(it) }
            itemFromSecondApi.priceUsd?.let { setPrice(it) }
        }

        private fun setTime(time: Long) {
            val date = Date(time)
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH)
            binding.txtDate.text = format.format(date)
        }

        private fun setPrice(price: String) {
            val number = price.toDoubleOrNull()
            val result = number?.let { roundOffDecimal(it) }
            result?.let {
                binding.txtPrice.text = String.format(
                    binding.root.context.getString(R.string.usd),
                    it.toString()
                )
            }
        }

        private fun roundOffDecimal(number: Double): Double {
            val df = DecimalFormat("###.###")
            df.roundingMode = RoundingMode.CEILING
            return df.format(number).toDouble()
        }
    }
}