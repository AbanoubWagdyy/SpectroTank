package com.spectrotank.ui.form

import android.content.Context
import com.spectrotank.DataLayer.db.Item

interface FormContract {

    interface Presenter {
        fun start()
        fun handleFormType(type: String?, position: Int)
        fun saveItemIntoDatabase(item: Item)
    }

    interface View {
        fun getAppContext(): Context
        fun handleSavedImeNumber(imeNumber: String?)
        fun handleSavedItem(item: Item)
        fun showSuccessUpdate()
        fun showSuccessInsert()
        fun finalizeScreen()
        fun showInvalidItemType()
        fun showCompleteAllFieldsMessage()
    }
}