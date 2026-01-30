package com.johnnyconsole.enterpriseims.android

import android.os.AsyncTask
import android.os.Bundle
import android.text.Html
import android.text.Html.FROM_HTML_MODE_LEGACY
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.johnnyconsole.enterpriseims.android.databinding.ActivityDeleteUserBinding
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection.HTTP_ACCEPTED
import java.net.HttpURLConnection.HTTP_BAD_REQUEST
import java.net.HttpURLConnection.HTTP_NOT_FOUND
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED
import java.net.URL
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection

class DeleteUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeleteUserBinding
    private var users = ArrayList<String>()

    private inner class UserAdapter: ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item) {

        override fun getCount(): Int {
            return users.size
        }

        override fun getItem(position: Int): String {
            return users[position]
        }

    }

    private inner class GetAllUsersTask: AsyncTask<String, Unit, Unit>() {

        var response = HTTP_ACCEPTED

        override fun onPreExecute() {
            super.onPreExecute()
            users.clear()
            binding.tvError.visibility = GONE
            binding.tvSuccess.visibility = GONE
            binding.indicator.visibility = VISIBLE
            binding.btSubmit.isEnabled = false

        }

        override fun doInBackground(vararg params: String?) {
            val conn = URL("https://wildfly.johnnyconsole.com:8443/ims/api/user/get-all?except=${params[0]}&auth-user=${params[0]}")
                .openConnection() as HttpsURLConnection
            conn.doInput = true
            conn.hostnameVerifier = HostnameVerifier { _, _ -> true }
            conn.connect()
            response = conn.responseCode

            if(response == HTTP_ACCEPTED) {
                val json = StringBuffer()
                with(BufferedReader(InputStreamReader(conn.inputStream))) {
                    for (line in readLines()) {
                        json.append(line)
                    }
                }

                val array = JSONArray(json.toString())
                for (i in 0 until array.length()) {
                    val user = array.get(i) as JSONObject
                    users.add("${user.getString("name")} (${user.getString("username")})")
                }
            }
            conn.disconnect()
        }

        override fun onPostExecute(result: Unit) {
            super.onPostExecute(result)
            with(binding) {
                indicator.visibility = GONE
                if(response == HTTP_ACCEPTED) {
                    (spUser.adapter as UserAdapter).notifyDataSetChanged()
                    if(users.isNotEmpty()) {
                        spUser.setSelection(0)
                        btSubmit.isEnabled = true
                    } else {
                        tvError.visibility = VISIBLE
                        tvError.text = Html.fromHtml(getString(R.string.error_message, "No Users Found."), FROM_HTML_MODE_LEGACY)
                    }
                }
                else {
                    tvError.visibility = VISIBLE
                    tvError.text = Html.fromHtml(getString(R.string.error_message,
                        when(response) {
                            HTTP_BAD_REQUEST -> "Missing or empty parameter, please try again."
                            HTTP_UNAUTHORIZED -> "User ${intent.getStringExtra("username")} is not an administrator."
                            HTTP_NOT_FOUND -> "User ${intent.getStringExtra("username")} is not found."
                            else -> "Unexpected HTTP response code: $response."
                        }
                    ), FROM_HTML_MODE_LEGACY)
                }
            }
        }

    }

    private inner class DeleteUserTask: AsyncTask<String, Unit, Unit>() {

        private var response = HTTP_ACCEPTED
        private lateinit var userDeleted: String

        override fun onPreExecute() {
            super.onPreExecute()
            binding.tvError.visibility = GONE
            binding.tvSuccess.visibility = GONE
            binding.indicator.visibility = VISIBLE
            binding.btSubmit.isEnabled = false
        }

        override fun doInBackground(vararg params: String?) {
            userDeleted = params[0]!!
            val conn = URL("https://wildfly.johnnyconsole.com:8443/ims/api/user/delete")
                .openConnection() as HttpsURLConnection
            conn.requestMethod = "POST"
            conn.doOutput = true
            with(conn.outputStream) {
                write("username=${params[0]}".toByteArray())
                write("&auth-user=${params[1]}".toByteArray())
                flush()
                close()
            }
            conn.hostnameVerifier = HostnameVerifier { _, _ -> true }
            conn.connect()
            response = conn.responseCode
            conn.disconnect()
        }

        override fun onPostExecute(result: Unit) {
            super.onPostExecute(result)
            val task = GetAllUsersTask()
            task.execute(intent.getStringExtra("username"))
            task.get()

            with(binding) {
                indicator.visibility = VISIBLE
                if(users.isNotEmpty()) btSubmit.isEnabled = true

                if (response == HTTP_ACCEPTED) {
                    tvSuccess.text = Html.fromHtml(getString(R.string.success_message, "User $userDeleted deleted successfully."), FROM_HTML_MODE_LEGACY)
                    tvSuccess.visibility = VISIBLE
                }
                else {
                    tvError.text = Html.fromHtml(getString(R.string.error_message,
                        when(response) {
                            HTTP_BAD_REQUEST -> "Missing or empty parameter, please try again."
                            HTTP_NOT_FOUND -> "User ${intent.getStringExtra("username")} not found."
                            HTTP_UNAUTHORIZED -> "User ${intent.getStringExtra("username")} is not an administrator."
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
        binding = ActivityDeleteUserBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        with(binding) {
            setContentView(root)
            ViewCompat.setOnApplyWindowInsetsListener(main) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
                insets
            }

            btBack.setOnClickListener { _ -> finish() }

            btSubmit.setOnClickListener { _ ->
                var username = spUser.selectedItem.toString()
                username = username.substring(username.indexOf('(')+ 1, username.indexOf(')'))
                DeleteUserTask().execute(username, intent.getStringExtra("username"))
            }

            spUser.adapter = UserAdapter()
            GetAllUsersTask()
                .execute(this@DeleteUserActivity.intent.getStringExtra("username"))
        }

        onBackPressedDispatcher.addCallback(
            this@DeleteUserActivity,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    finish()
                }
            }
        )
    }
}