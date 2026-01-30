package com.johnnyconsole.enterpriseims.android

import android.content.DialogInterface.BUTTON_NEGATIVE
import android.content.DialogInterface.BUTTON_POSITIVE
import android.os.AsyncTask
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
import com.johnnyconsole.enterpriseims.android.databinding.ActivityAddUserBinding
import java.net.HttpURLConnection.HTTP_BAD_REQUEST
import java.net.HttpURLConnection.HTTP_CONFLICT
import java.net.HttpURLConnection.HTTP_CREATED
import java.net.HttpURLConnection.HTTP_NOT_FOUND
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED
import java.net.URL
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection

class DeleteUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddUserBinding
    private var processing = false

    private inner class AddUserTask: AsyncTask<String, Unit, Unit>() {

        private var response = HTTP_CREATED
        private val UNPROCESSABLE = 422

        override fun onPreExecute() {
            super.onPreExecute()
            binding.tvError.visibility = GONE
            binding.tvSuccess.visibility = GONE
            binding.indicator.visibility = VISIBLE
        }

        override fun doInBackground(vararg params: String?) {
            val conn = URL("https://wildfly.johnnyconsole.com:8443/ims/api/user/add")
                .openConnection() as HttpsURLConnection
            conn.requestMethod = "POST"
            conn.hostnameVerifier = HostnameVerifier { _, _ -> true }
            conn.doOutput = true
            with(conn.outputStream) {
                write("username=${params[0]}".toByteArray())
                write("&name=${params[1]}".toByteArray())
                write("&password=${params[2]}".toByteArray())
                write("&confirm-password=${params[3]}".toByteArray())
                write("&is-admin=${params[4] == "Yes"}".toByteArray())
                write("&auth-user=${params[5]}".toByteArray())
                flush()
                close()
            }
            conn.connect()
            response = conn.responseCode
            conn.disconnect()
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)
            with(binding) {
                indicator.visibility = GONE
                processing = false

                if (response == HTTP_CREATED) {
                    tvSuccess.text = Html.fromHtml(getString(R.string.success_message, "User ${etUsername.text} added successfully."), FROM_HTML_MODE_LEGACY)
                    tvSuccess.visibility = VISIBLE

                    etUsername.text.clear()
                    etName.text.clear()
                    etPassword.text.clear()
                    etConfirmPassword.text.clear()
                    spIsAdmin.setSelection(0)
                    etUsername.requestFocus()
                }
                else {
                    tvError.text = Html.fromHtml(getString(R.string.error_message,
                        when(response) {
                            HTTP_BAD_REQUEST -> "Missing or empty parameter, please try again."
                            HTTP_NOT_FOUND -> "User ${intent.getStringExtra("username")} not found."
                            HTTP_UNAUTHORIZED -> "User ${intent.getStringExtra("username")} is not an administrator."
                            HTTP_CONFLICT -> "User ${etUsername.text} already exists, please try a different username."
                            UNPROCESSABLE -> "Your passwords do not match, please try again."
                            else -> "Unexpected HTTP response code: $response."
                        }
                    ), FROM_HTML_MODE_LEGACY)
                    tvError.visibility = VISIBLE
                }
            }
        }

    }

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

            btBack.setOnClickListener {_ -> confirmExit()}

            btSubmit.setOnClickListener { _ ->
                if(!processing) {
                    processing = true
                    AddUserTask().execute(
                        etUsername.text.toString(),
                        etName.text.toString(),
                        etPassword.text.toString(),
                        etConfirmPassword.text.toString(),
                        spIsAdmin.selectedItem.toString(),
                        this@DeleteUserActivity.intent.getStringExtra("username")
                    )
                }
            }

            onBackPressedDispatcher.addCallback(this@DeleteUserActivity, object: OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    confirmExit()
                }
            })
        }
    }

    private fun confirmExit() {
        val dialog = AlertDialog.Builder(this@DeleteUserActivity)
            .setTitle(R.string.confirm_exit_title)
            .setMessage(R.string.confirm_exit_message)
            .setPositiveButton(R.string.yes) { _, _ -> finish() }
            .setNegativeButton(R.string.no, null)
            .create()
        dialog.show()
        dialog.getButton(BUTTON_POSITIVE).setTextColor(getColor(R.color.success))
        dialog.getButton(BUTTON_NEGATIVE).setTextColor(getColor(R.color.error))}
}