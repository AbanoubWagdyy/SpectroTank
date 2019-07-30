package com.spectrotank.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.com.spectrotankapplication.R
import com.spectrotank.ui.sync.SyncActivity
import kotlinx.android.synthetic.main.activity_login.*
import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.com.spectrotankapplication.DialogUtils
import com.com.spectrotankapplication.ErrorHandler


class LoginActivity : AppCompatActivity(), LoginContract.View {

    override fun showErrorLoggedInData() {
        Snackbar.make(
            findViewById(android.R.id.content),
            "Invalid username or password !.",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun showLoading() {
        DialogUtils.showProgressDialog(this, "Loading ...")
    }

    override fun dismissLoading() {
        DialogUtils.dismissProgressDialog()
    }

    override fun showError() {
        Snackbar.make(
            findViewById(android.R.id.content),
            "Please try again later",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun showSyncScreen() {
        val intent = Intent(this@LoginActivity, SyncActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun getAppContext(): Context {
        return applicationContext
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ErrorHandler.toCatch(this)
        setContentView(R.layout.activity_login)

        mPresenter = LoginPresenter(this)
        mPresenter.start()

        btnLogin.setOnClickListener {
            hideKeyboard(this@LoginActivity)
            mPresenter.login(etUsername.text.toString(), etPassword.text.toString())
        }
    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm!!.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

    lateinit var mPresenter: LoginContract.Presenter
}