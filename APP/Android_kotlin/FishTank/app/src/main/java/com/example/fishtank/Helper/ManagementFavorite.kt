package com.example.Helper

import android.content.Context
import android.widget.Toast
import com.example.fishtank.Helper.TinyDB
import com.example.fishtank.Model.ItemsModel

class ManagementFavorite(val context: Context) {
    private val tinyDB = TinyDB(context)

    fun insertFood(item: ItemsModel) {
        var listFavorite = getListFavorite()
        val existAlready = listFavorite.any { it.title == item.title }

        if (!existAlready) {
            listFavorite.add(item)
            tinyDB.putListObject("FavoriteList", listFavorite)
            Toast.makeText(context, "已新增到我的最愛", Toast.LENGTH_SHORT).show()
        } else {
            removeFood(item)
        }
    }

    fun removeFood(item: ItemsModel) {
        val listFavorite = getListFavorite()
        listFavorite.removeAll { it.title == item.title }
        tinyDB.putListObject("FavoriteList", listFavorite)
        Toast.makeText(context, "已從我的最愛中移除", Toast.LENGTH_SHORT).show()
    }

    fun getListFavorite(): ArrayList<ItemsModel> {
        return tinyDB.getListObject("FavoriteList") ?: arrayListOf()
    }
}
