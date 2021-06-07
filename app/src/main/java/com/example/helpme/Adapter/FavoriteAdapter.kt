package com.example.helpme.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.helpme.DetailUserActivity
import com.example.helpme.Model.FavoriteModel
import com.example.helpme.Model.User
import com.example.helpme.databinding.ListUserItemBinding

class FavoriteAdapter(private val activity: Activity) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

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

        val data = listNotes[position]
        holder.itemView.setOnClickListener {
            val dataUser = User(
                    data.login,
                    null,
                    data.avatar_url,
                    data.favorite
            )
            val Intent = Intent(it.context, DetailUserActivity::class.java)
            Intent.putExtra(DetailUserActivity.EXTRA_USER, dataUser)
            it.context.startActivity(Intent)
            (it.context as Activity).finish()
        }
    }

    override fun getItemCount(): Int = this.listNotes.size

    interface OnItemClickCallback {
        fun onItemClicked(data: FavoriteModel)
    }
}