package com.spectrotank.ui.imeValidation

import android.content.Context

interface ValidationContract {

    interface Presenter {
        fun start()
        fun validateImeNumber(imeNumber: String)

    }

    interface View {
        fun getAppContext(): Context
        fun showFormScreen()
        fun showInvalidImeNumber()
    }
}