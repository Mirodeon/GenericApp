package com.mirodeon.genericapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.mirodeon.genericapp.databinding.RowTagBinding
import com.mirodeon.genericapp.databinding.SelectorTagBinding
import com.mirodeon.genericapp.room.entity.Tag

class TagSpinnerAdapter(var tags: List<Tag>) : BaseAdapter() {
    override fun getCount() = tags.size

    override fun getItem(position: Int) = tags[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding: SelectorTagBinding = if (convertView == null) {
            SelectorTagBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
        } else {
            SelectorTagBinding.bind(convertView)
        }

        binding.selectedTag.text = tags[position].name

        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding: RowTagBinding = if (convertView == null) {
            RowTagBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
        } else {
            RowTagBinding.bind(convertView)
        }

        binding.choiceTag.text = tags[position].name

        return binding.root
    }
}