package com.spectrotank.DataLayer.Retrofit

import com.com.spectrotankapplication.DataLayer.Retrofit.RestClient
import com.com.spectrotankapplication.DataLayer.Retrofit.RetrofitCallbacks
import com.spectrotank.DataLayer.Model.Login
import com.spectrotank.DataLayer.Model.TokenResponse
import com.spectrotank.DataLayer.db.Item
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteDataSourceUsingRetrofit private constructor() {

    fun login(login: Login, callback: RetrofitCallbacks.TokenResponseCallback) {
        RestClient.getRetrofitService()
            .login(login)
            .enqueue(object : Callback<TokenResponse> {
                override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                    callback.onSuccess(response.body())
                }

                override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                    callback.onFailure(call, t)
                }
            })
    }

    fun sync(token: String, items: List<Item>, callback: RetrofitCallbacks.StringCallback) {
        RestClient.getRetrofitService(token)
            .sync(items)
            .enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    callback.onFailure(call, t)
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    callback.onSuccess(response.body())
                }
            })
    }

    companion object {

        private var INSTANCE: RemoteDataSourceUsingRetrofit? = null

        val instance: RemoteDataSourceUsingRetrofit
            get() {
                if (INSTANCE == null)
                    INSTANCE = RemoteDataSourceUsingRetrofit()
                return INSTANCE!!
            }
    }
}