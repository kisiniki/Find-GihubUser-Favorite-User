package com.example.helpme

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.helpme.Adapter.FavoriteAdapter
import com.example.helpme.Model.FavoriteModel
import com.example.helpme.database.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.example.helpme.database.UserHelper
import com.example.helpme.databinding.ActivityFavoriteBinding
import com.example.helpme.helper.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity(){

    private lateinit var adapter: FavoriteAdapter
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var userHelper: UserHelper

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()

        showRecyclerView()

        showLoading(true)

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadNotesAsync()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            loadNotesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<FavoriteModel>(EXTRA_STATE)
            if (list != null) {
                adapter.listNotes = list
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listNotes)
    }

    private fun loadNotesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }

            val userList = deferredNotes.await()
            if (userList.size > 0) {
                adapter.listNotes = userList
            } else {
                adapter.listNotes = ArrayList()
                Toast.makeText(applicationContext,"No Data",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading(state: Boolean){
        if (state) binding.progressBar.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        loadNotesAsync()
    }

    private fun showRecyclerView(){
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        binding.rvUsers.setHasFixedSize(true)
        adapter = FavoriteAdapter(this)
        binding.rvUsers.adapter = adapter
        adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback{
            override fun onItemClicked(data: FavoriteModel) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(data: FavoriteModel){
        val intent = Intent(this ,DetailUserActivity::class.java)
        intent.putExtra(DetailUserActivity.EXTRA_USER, data.login)
        this.startActivity(intent)
    }
}