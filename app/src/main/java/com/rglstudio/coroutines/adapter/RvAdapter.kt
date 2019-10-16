package com.rglstudio.coroutines.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rglstudio.coroutines.R
import com.rglstudio.coroutines.data.ResponAlbum
import kotlinx.android.synthetic.main.rv_item.view.*

class RvAdapter(private var list: List<ResponAlbum>) : RecyclerView.Adapter<RvAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindView(list[position])
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(responAlbum: ResponAlbum){
            with(itemView){
                Glide.with(itemView)
                    .load(responAlbum.thumbnailUrl)
                    .centerCrop()
                    .into(img)
                title.text = responAlbum.title
            }
        }
    }
}