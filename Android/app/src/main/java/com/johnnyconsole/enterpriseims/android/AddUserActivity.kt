package com.johnnyconsole.enterpriseims.android

import android.content.DialogInterface.BUTTON_NEGATIVE
import android.content.DialogInterface.BUTTON_POSITIVE
import android.os.Bundle
import android.text.Html
import android.text.Html.FROM_HTML_MODE_LEGACY
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.johnnyconsole.enterpriseims.android.databinding.ActivityAddUserBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection.HTTP_BAD_REQUEST
import java.net.HttpURLConnection.HTTP_CONFLICT
import java.net.HttpURLConnection.HTTP_CREATED
import java.net.HttpURLConnection.HTTP_NOT_FOUND
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED
import java.net.URL
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection

class AddUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddUserBinding
    private val UNPROCESSABLE = 422

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUserBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        with(binding) {
            setContentView(root)
            ViewCompat.setOnApplyWindowInsetsListener(main) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
                insets
            }

            btBack.setOnClickListener { _ -> confirmExit() }

            btSubmit.setOnClickListener { _ ->

                lifecycleScope.launch {
                    tvError.visibility = GONE
                    tvSuccess.visibility = GONE
                    indicator.visibility = VISIBLE
                    btSubmit.isEnabled = false

                    val response = addUser(
                        etUsername.text.toString(),
                        etName.text.toString(),
                        etPassword.text.toString(),
                        etConfirmPassword.text.toString(),
                        spIsAdmin.selectedItem.toString() == "Yes",
                        this@AddUserActivity.intent.getStringExtra("username")!!
                    )
                    indicator.visibility = GONE
                    btSubmit.isEnabled = true

                    if (response == HTTP_CREATED) {
                        tvSuccess.text = Html.fromHtml(
                            getString(
                                R.string.success_message,
                                "User ${etUsername.text} added successfully."
                            ), FROM_HTML_MODE_LEGACY
                        )
                        tvSuccess.visibility = VISIBLE

                        etUsername.text.clear()
                        etName.text.clear()
                        etPassword.text.clear()
                        etConfirmPassword.text.clear()
                        spIsAdmin.setSelection(0)
                        etUsername.requestFocus()
                    } else {
                        tvError.text = Html.fromHtml(
                            getString(
                                R.string.error_message,
                                when (response) {
                                    HTTP_BAD_REQUEST -> "Missing or empty parameter, please try again."
                                    HTTP_NOT_FOUND -> "User ${intent.getStringExtra("username")} not found."
                                    HTTP_UNAUTHORIZED -> "User ${intent.getStringExtra("username")} is not an administrator."
                                    HTTP_CONFLICT -> "User ${etUsername.text} already exists, please try a different username."
                                    UNPROCESSABLE -> "Your passwords do not match, please try again."
                                    else -> "Unexpected HTTP response code: $response."
                                }
                            ), FROM_HTML_MODE_LEGACY
                        )
                        tvError.visibility = VISIBLE
                    }

                }
            }

            onBackPressedDispatcher.addCallback(
                this@AddUserActivity,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        confirmExit()
                    }
                })
        }
    }

    private fun confirmExit() {
        val dialog = AlertDialog.Builder(this@AddUserActivity)
            .setTitle(R.string.confirm_exit_title)
            .setMessage(R.string.confirm_exit_message)
            .setPositiveButton(R.string.yes) { _, _ -> finish() }
            .setNegativeButton(R.string.no, null)
            .create()
        dialog.show()
        dialog.getButton(BUTTON_POSITIVE).setTextColor(getColor(R.color.success))
        dialog.getButton(BUTTON_NEGATIVE).setTextColor(getColor(R.color.error))
    }

    private suspend fun addUser(username: String, name: String, password: String,
        confirmPassword: String, isAdmin: Boolean, adminUser: String): Int =
        withContext(Dispatchers.IO) {
        val conn = URL("https://wildfly.johnnyconsole.com:8443/ims/api/user/add")
            .openConnection() as HttpsURLConnection
        conn.requestMethod = "POST"
        conn.hostnameVerifier = HostnameVerifier { _, _ -> true }
        conn.doOutput = true
        with(conn.outputStream) {
            write("username=$username".toByteArray())
            write("&name=$name".toByteArray())
            write("&password=$password".toByteArray())
            write("&confirm-password=$confirmPassword".toByteArray())
            write("&is-admin=$isAdmin".toByteArray())
            write("&auth-user=$adminUser".toByteArray())
            flush()
            close()
        }
        conn.connect()
        val response = conn.responseCode
        conn.disconnect()
        response
    }
}