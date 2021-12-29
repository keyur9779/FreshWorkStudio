package com.app.freshworkstudio.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

object ErrorUtil {

    fun showError(message: String,view: View) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }
}