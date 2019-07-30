package com.spectrotank.ui.sync

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.com.spectrotankapplication.DialogUtils
import com.com.spectrotankapplication.ErrorHandler
import com.google.android.material.snackbar.Snackbar
import com.spectrotank.DataLayer.db.Item
import com.com.spectrotankapplication.R
import com.spectrotank.ui.sync.adapter.SyncAdapter
import kotlinx.android.synthetic.main.activity_sync.*

class SyncActivity : AppCompatActivity(), SyncContract.View {
    override fun showSyncSuccess() {
        Snackbar.make(
            findViewById(android.R.id.content),
            "Device Item synced successfully",
            Snackbar.LENGTH_LONG
        ).show()
    }

    override fun dismissLoading() {
        DialogUtils.dismissProgressDialog()
    }

    override fun showSuccess() {
        Snackbar.make(
            findViewById(android.R.id.content),
            "Items Synced Successfully",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun showError() {
        Snackbar.make(
            findViewById(android.R.id.content),
            "Can not sync items ,please try again later",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun showLoading() {
        DialogUtils.showProgressDialog(this, "Loading ....")
    }

    override fun setNoItemsFound() {
        recyclerItems.visibility = View.GONE
        btnSync.visibility = View.GONE
        relativeNoItems.visibility = View.VISIBLE
        relativeHeader.visibility = View.GONE
    }

    override fun getAppContext(): Context {
        return applicationContext
    }

    override fun setItems(items: List<Item>) {
        recyclerItems.layoutManager = LinearLayoutManager(this)
        recyclerItems.setHasFixedSize(true)
        recyclerItems.isNestedScrollingEnabled = false
        adapter = SyncAdapter(items)
        recyclerItems.adapter = adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ErrorHandler.toCatch(this)
        setContentView(R.layout.activity_sync)

        mPresenter = SyncPresenter(this)
        mPresenter.start()

        btnSync.setOnClickListener {
            mPresenter.sync(adapter.getItems())
        }
    }

    lateinit var mPresenter: SyncContract.Presenter
    lateinit var adapter: SyncAdapter
}
