package com.spectrotank.ui.imeValidation

import androidx.room.Room
import com.com.spectrotankapplication.DataLayer.db.AppDatabase
import com.spectrotank.DataLayer.DataRepository
import com.visionvalley.letuno.DataLayer.RepositorySource

class ValidationPresenter(private val mView: ValidationContract.View) : ValidationContract.Presenter {

    override fun validateImeNumber(imeNumber: String) {
        if (imeNumber.equals("")) {
            mView.showInvalidImeNumber()
            return
        }
        var items = database.itemDAO.getItems(imeNumber)
        if (items.size != 0) {
            mRepositorySource.setItemDetails(items[0])
        } else {
            mRepositorySource.setItemDetails(null)
            mRepositorySource.saveImeNumber(imeNumber)
        }
        mView.showFormScreen()
    }

    override fun start() {
        mRepositorySource = DataRepository.getInstance(mView.getAppContext())
        database = Room.databaseBuilder(mView.getAppContext(), AppDatabase::class.java,
            "spectrotank")
            .allowMainThreadQueries()
            .build()
    }

    private lateinit var database: AppDatabase
    lateinit var mRepositorySource: RepositorySource
}