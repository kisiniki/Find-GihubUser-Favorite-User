package com.example.helpme.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.helpme.Model.User
import com.example.helpme.databinding.ListUserItemBinding

class   MainAdapter : RecyclerView.Adapter<MainAdapter.MainAdapterHolder>() {

    var listUser = ArrayList<User>()

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class MainAdapterHolder(private val binding: ListUserItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User){
            with(binding){
                tvUsername.text = user.login
                Glide.with(itemView.context)
                    .load(user.avatar_url)
                    .into(imgUsers)

                binding.root.setOnClickListener { onItemClickCallback?.onItemClicked(user) }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainAdapterHolder {
        val binding = ListUserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainAdapterHolder((binding))
    }

    override fun onBindViewHolder(holder: MainAdapterHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size

    fun setlist(data: ArrayList<User>) {
        listUser.clear()
        listUser.addAll(data)
        notifyDataSetChanged()
    }
    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
}