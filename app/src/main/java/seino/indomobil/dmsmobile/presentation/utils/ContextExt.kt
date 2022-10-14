package seino.indomobil.dmsmobile.presentation.utils

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import seino.indomobil.dmsmobile.R

fun Context.showToast(message: String){
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.showGenericAlertDialog(message: String){
    AlertDialog.Builder(this).apply {
        setMessage(message)
        setPositiveButton(getString(R.string.ok)){ dialog, _ ->
             dialog.dismiss()
        }
    }.show()
}