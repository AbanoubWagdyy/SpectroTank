package com.spectrotank.DataLayer

import android.content.Context
import com.com.spectrotankapplication.DataLayer.Retrofit.RetrofitCallbacks
import com.spectrotank.DataLayer.Model.Login
import com.spectrotank.DataLayer.Model.TokenResponse
import com.spectrotank.DataLayer.Retrofit.RemoteDataSourceUsingRetrofit
import com.spectrotank.DataLayer.db.Item
import com.visionvalley.letuno.DataLayer.RepositorySource

import retrofit2.Call

class DataRepository
private constructor(context: Context) : RepositorySource {

    override fun sync(syncedItems: List<Item>, callback: RetrofitCallbacks.StringCallback) {

        mRetrofitService.sync(tokenResponse!!.token, syncedItems, object : RetrofitCallbacks.StringCallback {
            override fun onSuccess(result: String?) {
                callback.onSuccess(result)
            }

            override fun onFailure(call: Call<String>?, t: Throwable?) {
                callback.onFailure(call, t)
            }
        })
    }

    override fun getImeNumber(): String? {
        return imeNumber
    }

    override fun saveImeNumber(imeNumber: String) {
        this.imeNumber = imeNumber
    }

    override fun setItemDetails(item: Item?) {
        this.item = item
    }

    override fun getItemDetails(): Item? {
        return this.item
    }

    private var tokenResponse: TokenResponse? = null
    private var item: Item? = null

    private var imeNumber: String = ""

    private var mRetrofitService: RemoteDataSourceUsingRetrofit

    init {
        mRetrofitService = RemoteDataSourceUsingRetrofit.instance
    }

    override fun login(login: Login, callback: RetrofitCallbacks.TokenResponseCallback) {
        mRetrofitService.login(login, object : RetrofitCallbacks.TokenResponseCallback {
            override fun onSuccess(result: TokenResponse?) {
                this@DataRepository.tokenResponse = result
                callback.onSuccess(result)
            }

            override fun onFailure(call: Call<TokenResponse>?, t: Throwable?) {
                callback.onFailure(call, t)
            }
        })
    }

    companion object {

        private var INSTANCE: DataRepository? = null

        fun getInstance(context: Context): DataRepository {
            if (INSTANCE == null) {
                INSTANCE = DataRepository(context)
            }
            return INSTANCE as DataRepository
        }
    }
}