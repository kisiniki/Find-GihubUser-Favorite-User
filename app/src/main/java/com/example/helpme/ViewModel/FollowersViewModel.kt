package com.example.helpme.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.helpme.Model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.lang.Exception

class FollowersViewModel: ViewModel() {
    private val userMLData = MutableLiveData<ArrayList<User>>()

    fun setFollowers(query: String){
        val listUser = ArrayList<User>()
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$query/followers"
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {
                val result = String(responseBody)
                try {
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()){
                        val items = jsonArray.getJSONObject(i)
                        val user = User()
                        user.login = items.getString("login")
                        user.avatar_url = items.getString("avatar_url")
                        listUser.add(user)
                    }
                    userMLData.postValue(listUser)
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable) {
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
    fun getFollowers(): LiveData<ArrayList<User>>{
        return userMLData
    }
}