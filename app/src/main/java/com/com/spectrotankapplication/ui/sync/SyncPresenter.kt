package com.spectrotank.ui.sync

import android.util.Log
import androidx.room.Room
import com.com.spectrotankapplication.DataLayer.Retrofit.RetrofitCallbacks
import com.com.spectrotankapplication.DataLayer.db.AppDatabase
import com.spectrotank.DataLayer.DataRepository
import com.spectrotank.DataLayer.db.Item
import com.spectrotank.ui.sync.SyncContract
import com.visionvalley.letuno.DataLayer.RepositorySource
import retrofit2.Call

class SyncPresenter(private val mView: SyncContract.View) : SyncContract.Presenter {

    override fun sync(items: List<Item>) {
        Log.d("items", "items")

        var syncedItems = items.filter {
            it.isSynced
        }

        if (syncedItems.isNotEmpty()) {
            mView.showLoading()
            mRepositorySource.sync(syncedItems, object : RetrofitCallbacks.StringCallback {
                override fun onSuccess(result: String?) {
                    mView.dismissLoading()
                    mView.showSyncSuccess()
                    for (item in syncedItems) {
                        database.itemDAO.delete(item)
                    }

                    var allItems = database.itemDAO.allItems
                    if (allItems != null && allItems.size > 0)
                        mView.setItems(database.itemDAO.allItems)
                    else
                        mView.setNoItemsFound()
                }

                override fun onFailure(call: Call<String>?, t: Throwable?) {
                    mView.dismissLoading()
                    mView.showError()
                }
            })
        }
    }

    override fun start() {
        mRepositorySource = DataRepository.getInstance(mView.getAppContext())
        database = Room.databaseBuilder(
            mView.getAppContext(), AppDatabase::class.java,
            "spectrotank"
        ).allowMainThreadQueries().build()

        var allItems = database.itemDAO.allItems
        if (allItems != null && allItems.size > 0)
            mView.setItems(database.itemDAO.allItems)
        else
            mView.setNoItemsFound()
    }

    private lateinit var database: AppDatabase
    lateinit var mRepositorySource: RepositorySource
}