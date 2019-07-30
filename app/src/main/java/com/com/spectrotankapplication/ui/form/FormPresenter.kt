package com.spectrotank.ui.imeValidation

import android.os.Handler
import androidx.room.Room
import com.com.spectrotankapplication.DataLayer.db.AppDatabase
import com.spectrotank.DataLayer.DataRepository
import com.spectrotank.DataLayer.db.Item
import com.spectrotank.ui.form.FormContract
import com.visionvalley.letuno.DataLayer.RepositorySource

class FormPresenter(private val mView: FormContract.View) : FormContract.Presenter {

    override fun saveItemIntoDatabase(item: Item) {

        when (position) {
            0 -> {
                mView.showInvalidItemType()
                return
            }
            1, 2 -> {
                if (item.length!!.trim().equals("") || item.width!!.trim().equals("") || item.height!!.trim().equals("")) {
                    mView.showCompleteAllFieldsMessage()
                    return
                }
            }
            3 -> {
                if (item.length!!.trim().equals("") || item.diameter!!.trim().equals("")) {
                    mView.showCompleteAllFieldsMessage()
                    return
                }
            }
            4 -> {
                if (item.height!!.trim().equals("") || item.diameter!!.trim().equals("")) {
                    mView.showCompleteAllFieldsMessage()
                    return
                }
            }
        }

        item.type = type

        if (this.item != null) {
            item.isSynced = this.item!!.isSynced
            mView.showSuccessUpdate()
            database.itemDAO.update(item)
            Handler().postDelayed({
                mView.finalizeScreen()
            }, 1500)

        } else {
            mView.showSuccessInsert()
            database.itemDAO.insert(item)
            Handler().postDelayed({
                mView.finalizeScreen()
            }, 1500)
        }
    }

    override fun handleFormType(type: String?, position: Int) {
        this.type = type
        this.position = position
    }

    override fun start() {
        mRepositorySource = DataRepository.getInstance(mView.getAppContext())
        database = Room.databaseBuilder(mView.getAppContext(), AppDatabase::class.java, "spectrotank")
            .allowMainThreadQueries()
            .build()

        item = mRepositorySource.getItemDetails()

        Handler().postDelayed({
            if (item != null) {
                type = item!!.type
                mView.handleSavedItem(item!!)
            } else {
                mView.handleSavedImeNumber(mRepositorySource.getImeNumber())
            }
        }, 500)
    }

    private var position: Int = 0
    private var type: String? = ""
    private lateinit var database: AppDatabase
    lateinit var mRepositorySource: RepositorySource
    var item: Item? = null
}