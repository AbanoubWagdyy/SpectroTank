package com.visionvalley.letuno.DataLayer

import com.com.spectrotankapplication.DataLayer.Retrofit.RetrofitCallbacks
import com.spectrotank.DataLayer.Model.Login
import com.spectrotank.DataLayer.db.Item

interface RepositorySource {
    fun login(login: Login, callback: RetrofitCallbacks.TokenResponseCallback)
    fun setItemDetails(item: Item?)
    fun getItemDetails(): Item?
    fun saveImeNumber(imeNumber: String)
    fun getImeNumber(): String?
    fun sync(syncedItems: List<Item>, callback: RetrofitCallbacks.StringCallback)
}