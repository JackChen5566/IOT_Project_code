package com.example.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.Helper.ChangeNumberItemsListener
import com.example.Helper.ManagementFavorite
import com.example.fishtank.Model.ItemsModel
import com.example.fishtank.databinding.ViewholderFavoriteBinding


class FavoriteAdapter(
    private val listItemSelected: ArrayList<ItemsModel>,
    private val context: Context,
    private val changeNumberItemsListener: ChangeNumberItemsListener? = null
) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    class ViewHolder(val binding: ViewholderFavoriteBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listItemSelected[position]

        holder.binding.title.text = item.title
        holder.binding.sizeEachItem.text = item.size.joinToString(", ")
        holder.binding.feeEachItem.text = "$${item.price}"

        Glide.with(holder.itemView.context)
            .load(item.picUrl[0])
            .apply(RequestOptions().transform(CenterCrop()))
            .into(holder.binding.pic)

        holder.binding.cancelFavBtn.setOnClickListener {
            val managementFavorite = ManagementFavorite(context)
            managementFavorite.removeFood(item)
            listItemSelected.removeAt(position)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = listItemSelected.size
}