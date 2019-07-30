package com.spectrotank.ui.login

import android.content.Context
import com.spectrotank.DataLayer.db.Item

interface LoginContract {

    interface Presenter {
        fun start()
        fun login(username: String, password: String)
    }

    interface View {
        fun getAppContext(): Context
        fun showSyncScreen()
        fun showError()
        fun showLoading()
        fun dismissLoading()
        fun showErrorLoggedInData()
    }
}