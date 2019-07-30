package com.spectrotank.ui.sync

import android.content.Context
import com.spectrotank.DataLayer.db.Item

interface SyncContract {

    interface Presenter {
        fun start()
        fun sync(items: List<Item>)
    }

    interface View {
        fun getAppContext(): Context
        fun setItems(items: List<Item>)
        fun setNoItemsFound()
        fun showLoading()
        fun dismissLoading()
        fun showSuccess()
        fun showError()
        fun showSyncSuccess()
    }
}