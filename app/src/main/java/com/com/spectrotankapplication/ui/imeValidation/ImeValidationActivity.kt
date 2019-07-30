package com.spectrotank.ui.imeValidation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.com.spectrotankapplication.ErrorHandler
import com.google.android.material.snackbar.Snackbar
import com.com.spectrotankapplication.R
import com.spectrotank.ui.form.FormActivity
import kotlinx.android.synthetic.main.activity_ime_validation.*

class ImeValidationActivity : AppCompatActivity(),ValidationContract.View {

    override fun showInvalidImeNumber() {
        Snackbar.make(
            findViewById(android.R.id.content),
            "Invalid IMEI Number",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun showFormScreen() {
        val intent = Intent(this@ImeValidationActivity, FormActivity::class.java)
        startActivity(intent)
    }

    override fun getAppContext(): Context {
        return applicationContext
    }

    lateinit var mPresenter : ValidationContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ErrorHandler.toCatch(this)
        setContentView(R.layout.activity_ime_validation)

        mPresenter = ValidationPresenter(this)
        mPresenter.start()

        btnSubmit.setOnClickListener {
            mPresenter.validateImeNumber(etIMEINumber.text.toString())
        }
    }
}
