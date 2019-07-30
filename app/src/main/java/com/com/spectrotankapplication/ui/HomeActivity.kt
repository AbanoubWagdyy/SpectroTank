package com.spectrotank.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.com.spectrotankapplication.R
import com.spectrotank.ui.imeValidation.ImeValidationActivity
import com.spectrotank.ui.login.LoginActivity
import com.spectrotank.ui.sync.SyncActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        relativeImeValidation.setOnClickListener {
            val intent = Intent(this@HomeActivity, ImeValidationActivity::class.java)
            startActivity(intent)
        }

        relativeSync.setOnClickListener {
            val intent = Intent(this@HomeActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}