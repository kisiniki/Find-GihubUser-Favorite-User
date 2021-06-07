package com.example.consumerapp.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.consumerapp.Model.FavoriteModel
import com.example.consumerapp.databinding.ListUserItemBinding

class FavoriteAdapter(private val activity: Activity) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    var listNotes = ArrayList<FavoriteModel>()
        set(listNotes) {
            if (listNotes.size > 0) {
                this.listNotes.clear()
            }
            this.listNotes.addAll(listNotes)
            notifyDataSetChanged()
        }

    inner class FavoriteViewHolder(private val binding : ListUserItemBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("StringFormatInvalid")
        fun bind(user: FavoriteModel){
            binding.tvUsername.text = user.login
            Glide.with(itemView.context)
                    .load(user.avatar_url)
                    .into(binding.imgUsers)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteAdapter.FavoriteViewHolder {
        val binding = ListUserItemBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteAdapter.FavoriteViewHolder, position: Int) {
        holder.bind(listNotes[position])
    }

    override fun getItemCount(): Int = this.listNotes.size

}