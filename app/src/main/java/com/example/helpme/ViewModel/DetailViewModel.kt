package com.example.helpme.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.helpme.Model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class DetailViewModel: ViewModel() {

    val listUser = MutableLiveData<User>()

    fun setDetailUser(query: String){
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$query"
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                try {
                    val responseObject = JSONObject(result)
                    val user = User()
                    user.apply {
                        login = responseObject.getString("login")
                        name = responseObject.getString("name")
                        avatar_url = responseObject.getString("avatar_url")
                        location = responseObject.getString("location")
                        company = responseObject.getString("company")
                        public_repos = responseObject.getInt("public_repos")
                        followers = responseObject.getInt("followers")
                        following = responseObject.getInt("followers")
                    }
                    listUser.postValue(user)
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
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
    fun getDetailUser(): LiveData<User>{
        return listUser
    }
}