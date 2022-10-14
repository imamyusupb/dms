package seino.indomobil.dmsmobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import dagger.hilt.android.AndroidEntryPoint
import seino.indomobil.dmsmobile.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        userIdInputTextOnFocusListener()
        passwordInputTextOnFocusListener()

        activityMainBinding.btnLogin.setOnClickListener {
            login() }

    }

    private fun validPassword(): String? {
        val passwordText = activityMainBinding.tvUserPassword.toString()
        if (passwordText.length < 8)
            activityMainBinding.containerIdPasswordText.helperText = "Password terlalu pendek"
        if (!passwordText.matches(".*[A-Z].*".toRegex()))
            activityMainBinding.containerIdPasswordText.helperText = "Password setidaknya mengandung 1 huruf kapital"
        if (!passwordText.matches(".*[@#\$%^&+=].*".toRegex()))
            activityMainBinding.containerIdPasswordText.helperText ="Password setidaknya mengandung 1 spesial karakter (@#\$%^&+=)"
        return null
    }

    private fun validId(): String? {
        val useridText = activityMainBinding.tvUserId.toString()
        if (useridText.length <= 7)
            activityMainBinding.containerIdUserText.helperText = "Id yang dimasukan terlalu sedikit"
        if (useridText.matches(".*[@#\$%^&+=].*".toRegex()))
            activityMainBinding.containerIdUserText.helperText = "Id tidak boleh mengandung karakter"
        return null
    }

    private fun login() {
        validId()
        validPassword()
        val validId = activityMainBinding.containerIdUserText.helperText == null
        val validPassword = activityMainBinding.containerIdPasswordText.helperText == null

        if (validId && validPassword) {
            Toast.makeText(this, "Valid Form", Toast.LENGTH_SHORT).show()
            resetForm()
        }
    }

    private fun resetForm() {
        activityMainBinding.tvUserId.text = null
        activityMainBinding.tvUserPassword.text = null

        activityMainBinding.containerIdUserText.helperText = "Tidak boleh kosong"
        activityMainBinding.containerIdPasswordText.helperText = "Tidak boleh kosong"
    }

    private fun passwordInputTextOnFocusListener() {
        activityMainBinding.tvUserPassword.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                activityMainBinding.containerIdPasswordText.helperText = validPassword()
            }
        }
    }

    private fun userIdInputTextOnFocusListener() {
        activityMainBinding.tvUserId.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                activityMainBinding.containerIdUserText.helperText = validId()
            }
        }
    }
}