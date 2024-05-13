package com.example.fishtank

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Adapter.ColorAdapter
import com.example.Adapter.SizeAdapter
import com.example.Adapter.SliderAdapter
import com.example.Helper.ManagementFavorite
import com.example.Helper.ManagmentCart
import com.example.fishtank.Model.ItemsModel
import com.example.fishtank.Model.SliderModel
import com.example.fishtank.databinding.ActivityDetalBinding

class DetalActivity : BaseActivity() {
    private lateinit var binding:ActivityDetalBinding
    private lateinit var item:ItemsModel

    private var numberOrder=1
    private lateinit var managmentCart: ManagmentCart
    private lateinit var managementFavorite: ManagementFavorite
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDetalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managmentCart= ManagmentCart(this)
        managementFavorite = ManagementFavorite(this)

        getBundle()
        banners()
        initLists()



    }

    private fun initLists() {
        val sizeList=ArrayList<String>()
        for (size in item.size){
            sizeList.add(size.toString())
        }

        binding.sizeList.adapter= SizeAdapter(sizeList)
        binding.sizeList.layoutManager=
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)

        val colorList=ArrayList<String>()
        for (imageUrl in item.picUrl){
            colorList.add(imageUrl)
        }
        binding.colorList.adapter= ColorAdapter(colorList)
        binding.colorList.layoutManager=
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
    }

    private fun banners() {
        var sliderItems=ArrayList<SliderModel>()
        for (imageUrl in item.picUrl){
            sliderItems.add(SliderModel(imageUrl))
        }

        binding.slider.adapter= SliderAdapter(sliderItems,binding.slider)
        binding.slider.clipToPadding=true
        binding.slider.clipChildren=true
        binding.slider.offscreenPageLimit=1



        if(sliderItems.size>1){
            binding.dotIndicator.visibility = View.VISIBLE
            binding.dotIndicator.attachTo(binding.slider)
        }
    }

    private fun getBundle() {
        item=intent.getParcelableExtra("object")!!

        binding.title.text=item.title
        binding.descriptionTxt.text=item.description
        binding.priceTxt.text="$"+item.price
        binding.ratingTxt.text="${item.rating}評價"
        binding.addToCarBtn.setOnClickListener{
            item.numberInCart=numberOrder
            managmentCart.insertFood(item)
        }
        binding.backBtn.setOnClickListener{finish()}
        binding.cartBtn.setOnClickListener {
            Toast.makeText(this, "已加入購物車", Toast.LENGTH_SHORT).show()
        }
        binding.lovBtn.setOnClickListener{
            managementFavorite.insertFood(item)
            binding.addToCarBtn.isEnabled = false
        }
    }


}