package com.spectrotank.ui.login

import com.com.spectrotankapplication.DataLayer.Retrofit.RetrofitCallbacks
import com.spectrotank.DataLayer.DataRepository
import com.spectrotank.DataLayer.Model.Login
import com.spectrotank.DataLayer.Model.TokenResponse
import com.visionvalley.letuno.DataLayer.RepositorySource
import retrofit2.Call

class LoginPresenter(private val mView: LoginContract.View) : LoginContract.Presenter {

    override fun login(username: String, password: String) {
        if (username.equals("") || password.equals("")) {
            mView.showErrorLoggedInData()
            return
        }
        mView.showLoading()
        var login = Login()
        login.password = password
        login.username = username
        mRepositorySource.login(login, object : RetrofitCallbacks.TokenResponseCallback {
            override fun onSuccess(result: TokenResponse?) {
                mView.dismissLoading()
                if (result != null)
                    mView.showSyncScreen()
            }

            override fun onFailure(call: Call<TokenResponse>?, t: Throwable?) {
                mView.dismissLoading()
                mView.showError()
            }
        })
    }

    override fun start() {
        mRepositorySource = DataRepository.getInstance(mView.getAppContext())
    }

    lateinit var mRepositorySource: RepositorySource
}