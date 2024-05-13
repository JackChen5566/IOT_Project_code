package com.example.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.Helper.ChangeNumberItemsListener
import com.example.Helper.ManagmentCart
import com.example.fishtank.Model.ItemsModel
import com.example.fishtank.databinding.ViewholderCartBinding


class CartAdapter (private val listItemSelected:ArrayList<ItemsModel>,
    context: Context,
    val changeNumberItemsListener: ChangeNumberItemsListener? = null):RecyclerView.Adapter<CartAdapter.ViewHolder>(){
    class ViewHolder (val binding: ViewholderCartBinding): RecyclerView.ViewHolder(binding.root){

    }
    private val managementCart = ManagmentCart(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.ViewHolder {
        val binding=
            ViewholderCartBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartAdapter.ViewHolder, position: Int) {
        val item=listItemSelected[position]

        holder.binding.title.text=item.title
        holder.binding.sizeEachItem.text=item.size.joinToString(", ")
        holder.binding.feeEachItem.text="$${item.price}"
        holder.binding.totalEachItem.text="$${Math.round((item.numberInCart*item.price).toDouble())}"
        holder.binding.numberItemTxt.text=item.numberInCart.toString()

        Glide.with(holder.itemView.context)
            .load(item.picUrl[0])
            .apply(RequestOptions().transform(CenterCrop()))
            .into(holder.binding.pic)

        holder.binding.plusCartBtn.setOnClickListener {
            managementCart.plusItem(listItemSelected,position,object : ChangeNumberItemsListener {
                override fun onChanged() {
                    notifyDataSetChanged()
                    changeNumberItemsListener?.onChanged()
                }

            })
        }
        holder.binding.minusCartBtn.setOnClickListener {
            managementCart.minusItem(listItemSelected, position, object : ChangeNumberItemsListener {
                    override fun onChanged() {
                        notifyDataSetChanged()
                        changeNumberItemsListener?.onChanged()
                }

            })
        }


    }

    override fun getItemCount(): Int =listItemSelected.size

    fun getItemQuantitiesWithPrice(): HashMap<String, Triple<Int, Int, ArrayList<String>>> {
        val itemQuantitiesWithPrice = HashMap<String, Triple<Int, Int, ArrayList<String>>>()
        for (item in listItemSelected) {
            itemQuantitiesWithPrice[item.title] = Triple(
                first = item.numberInCart,
                second = item.price,
                third = item.size
            )
        }
        return itemQuantitiesWithPrice
    }

}