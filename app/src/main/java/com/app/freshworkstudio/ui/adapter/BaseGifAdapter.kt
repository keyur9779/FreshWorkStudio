package com.app.freshworkstudio.ui.adapter

import androidx.recyclerview.widget.RecyclerView

abstract class BaseGifAdapter<T:RecyclerView.ViewHolder>() : RecyclerView.Adapter<T>() {


    abstract fun addGif(gif:Any)
    abstract fun showErrorPage(error: String)
    abstract fun clear()

}