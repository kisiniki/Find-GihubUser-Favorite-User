package com.example.helpme

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LiveData

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.helpme.Adapter.MainAdapter
import com.example.helpme.Model.User
import com.example.helpme.databinding.ActivityMainBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    companion object {
        val TAG = MainActivity::class.java.simpleName
    }

    private lateinit var binding:ActivityMainBinding
    private lateinit var adapter: MainAdapter
    private val userMLData = MutableLiveData<ArrayList<User>>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = MainAdapter()
        adapter.notifyDataSetChanged()

        showRecyclerView()

        getSearchListUser().observe(this, Observer { userData ->
            if (userData != null){
                adapter.setlist(userData)
                showLoading(false)
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.oprion_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.username)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                setSearchListUser(query!!)
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                setSearchListUser(newText!!)
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.language) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }else if(item.itemId == R.id.fav){
            val mIntent = Intent(this, FavoriteActivity::class.java)
            startActivity(mIntent)
        }else if (item.itemId == R.id.alarm){
            val mIntent = Intent(this, AlarmActivity::class.java)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setSearchListUser(query: String){

        val listUser = ArrayList<User>()

        binding.progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=$query"
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        client.get(url, object :  AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                Log.d(TAG, result)

                try {
                    val responseObject = JSONObject(result)
                    val items = responseObject.getJSONArray("items")
                    for (i in 0 until items.length()){
                        val indxItm = items.getJSONObject(i)
                        val user = User()
                        user.login = indxItm.getString("login")
                        user.avatar_url = indxItm.getString("avatar_url")
                        listUser.add(user)
                    }
                    userMLData.postValue(listUser)
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Log.d("onFailure : $errorMessage", error.message.toString())
            }

        })
    }
    private fun getSearchListUser(): LiveData<ArrayList<User>>{
        return userMLData
    }

    private fun showLoading(state: Boolean){
        if (state)
            binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }
    private fun showRecyclerView(){
        binding.rvUsers.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.rvUsers.adapter = adapter

        adapter.setOnItemClickCallback(object : MainAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(user: User){
        val intent = Intent(this@MainActivity ,DetailUserActivity::class.java)
        intent.putExtra(DetailUserActivity.EXTRA_USER, user)
        this@MainActivity.startActivity(intent)
    }
}