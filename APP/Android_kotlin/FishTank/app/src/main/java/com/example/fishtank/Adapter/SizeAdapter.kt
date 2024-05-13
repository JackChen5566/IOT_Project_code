package com.example.Adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fishtank.Model.BrandModel
import com.example.fishtank.R
import com.example.fishtank.databinding.ViewholderBrandBinding
import com.example.fishtank.databinding.ViewholderColorBinding
import com.example.fishtank.databinding.ViewholderSizeBinding

class SizeAdapter (val items:MutableList<String>):
    RecyclerView.Adapter<SizeAdapter.Viewholder>(){
    private var selectedPosition = -1
    private var lastSelectedPosition = -1
    private  lateinit var context: Context

    class Viewholder(val binding: ViewholderSizeBinding):
        RecyclerView.ViewHolder(binding.root) {
        val sizeTxt: TextView = binding.sizeTxt
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeAdapter.Viewholder {
        context=parent.context
        val binding=ViewholderSizeBinding.inflate(LayoutInflater.from(context),parent,false)
        return Viewholder(binding)

    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.sizeTxt.text = items[position]

        holder.binding.root.setOnClickListener {
            lastSelectedPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(lastSelectedPosition)
            notifyItemChanged(selectedPosition)
        }

        if (selectedPosition == position) {

            holder.binding.colorLayout.setBackgroundResource(R.drawable.grey_bg_select)
            holder.binding.sizeTxt.setTextColor(context.resources.getColor(R.color.purple))

        } else {
            holder.binding.colorLayout.setBackgroundColor(R.drawable.grey_bg)
            holder.binding.sizeTxt.setTextColor(context.resources.getColor(R.color.black))

        }
    }
    override fun getItemCount(): Int = items.size
}