package com.app.freshworkstudio.ui.adapter

import androidx.recyclerview.widget.RecyclerView

abstract class BaseGifAdapter<T:RecyclerView.ViewHolder>() : RecyclerView.Adapter<T>() {


    abstract fun addGif(dList:Any)
    abstract fun showErrorPage(message: String)
    abstract fun clear()

}