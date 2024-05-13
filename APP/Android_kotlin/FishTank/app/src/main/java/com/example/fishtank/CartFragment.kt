package com.example.fishtank

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Adapter.CartAdapter
import com.example.Helper.ChangeNumberItemsListener
import com.example.Helper.ManagmentCart
import com.example.fishtank.databinding.FragmentCartBinding
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CartFragment : Fragment() {

    private val database = FirebaseDatabase.getInstance()
    private lateinit var managmentCart: ManagmentCart
    var _binding: FragmentCartBinding? = null
    private val binding get() =_binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        val view = binding.root

        managmentCart = ManagmentCart(requireContext())

        setVariable()
        initCartList()
        calculateCart()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    private fun initCartList() {
        binding.viewCart.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.viewCart.adapter = CartAdapter(managmentCart.getListCart(), requireContext(), object : ChangeNumberItemsListener {
            override fun onChanged() {
                calculateCart()
            }
        })

        with(binding) {
            emptyTxt.visibility = if (managmentCart.getListCart().isEmpty()) View.VISIBLE else View.GONE
            scrollView2.visibility = if (managmentCart.getListCart().isEmpty()) View.GONE else View.VISIBLE
        }
    }

    private fun calculateCart() {
        var freight = 0
        val subtotal = managmentCart.getTotalFee().toInt() // 将 subtotal 转换为 Int
        freight = if (subtotal == 0) 0 else 200

        val total = subtotal + freight // 计算总额

        with(binding) {
            subTotalTxt.text = "$subtotal"
            freightTxt.text = "$freight"
            totalTxt.text = "$total"
        }
    }

    private fun setVariable() {
//        binding.backBtn.setOnClickListener {activity.finish()}

        binding.orderBtn.setOnClickListener {
            val reference = database.reference.child("order").push()

            val itemQuantitiesWithPrice = (binding.viewCart.adapter as CartAdapter).getItemQuantitiesWithPrice()



            if (itemQuantitiesWithPrice.isNotEmpty()) {
                val subtotalText = binding.subTotalTxt.text.toString().toIntOrNull()
                val freight = binding.freightTxt.text.toString().toInt()
                val total = binding.totalTxt.text.toString().toInt()

                val timestamp = System.currentTimeMillis()
                val taiwanTimestamp = timestamp + 8 * 60 * 60 * 1000  // 添加 8 小时的偏移量

                val orderTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(
                    Date(taiwanTimestamp)
                )


                val cartData = hashMapOf(
                    "訂單時間" to orderTime,
                    "商品清單" to itemQuantitiesWithPrice,
                    "小計" to subtotalText,
                    "運費" to freight,
                    "總計" to total
                )

                reference.setValue(cartData)
                    .addOnSuccessListener {
                        Toast.makeText(activity, "已送出訂單", Toast.LENGTH_SHORT).show()
                    }
//                    .addOnFailureListener { e ->
//                        Log.e(TAG, "Error adding order to database", e)
//                    }
            } else {
                Toast.makeText(activity, "訂單資訊不完整，無法送出訂單", Toast.LENGTH_SHORT).show()
            }
        }
    }

}