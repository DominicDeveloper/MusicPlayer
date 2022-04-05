package com.dominic.musicplayer2022.Adapter

import com.dominic.musicplayer2022.Models.Music
import com.dominic.musicplayer2022.R
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_music.view.*

class RvAdapter(val context: Context, val list:List<Music>, val rvItemClick:RvItemClick) : RecyclerView.Adapter<RvAdapter.VH>() {

    inner class VH(var itemRv: View): RecyclerView.ViewHolder(itemRv){
        @SuppressLint("SetTextI18n")
        fun onBind(music: Music,position: Int){

            itemRv.my_actor_name.text = music.author
            itemRv.my_music_name.isSelected = true
            itemRv.my_music_name.text  = music.title
            if (music.imagePath != ""){
                val bm = BitmapFactory.decodeFile(list[position].imagePath)
                itemRv.my_musci_image.setImageBitmap(bm)
            }
            itemRv.setOnClickListener {
                rvItemClick.onClick(music,position)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return  VH(LayoutInflater.from(parent.context).inflate(R.layout.item_music,parent,false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(list[position],position)

    }

    override fun getItemCount(): Int {
        return  list.size
    }
    interface RvItemClick{
        fun onClick(music: Music,position: Int)

    }

}