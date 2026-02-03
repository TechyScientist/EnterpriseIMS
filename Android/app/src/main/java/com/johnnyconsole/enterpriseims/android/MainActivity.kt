package com.johnnyconsole.enterpriseims.android

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.johnnyconsole.enterpriseims.android.databinding.ActivityMainBinding
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection.HTTP_OK
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED
import java.net.URL
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection.HTTP_BAD_REQUEST

class MainActivity : AppCompatActivity() {

    private lateinit var userInfo: JSONObject

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()

        with(binding) {
            setContentView(root)
            ViewCompat.setOnApplyWindowInsetsListener(main) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
                insets
            }

            etUsername.setText(
                getSharedPreferences("EnterpriseIMS", MODE_PRIVATE)
                    .getString("username", "")
            )

            tvWarning.text = Html.fromHtml(getString(R.string.sign_in_warning),
                Html.FROM_HTML_MODE_LEGACY)

            btSignIn.setOnClickListener { _ ->
                lifecycleScope.launch {

                    indicator.visibility = VISIBLE
                    tvError.visibility = GONE
                    btSignIn.isEnabled = false

                    val response = signIn(
                        etUsername.text.toString().lowercase(),
                        etPassword.text.toString()
                    )

                    indicator.visibility = INVISIBLE
                    btSignIn.isEnabled = true
                    if (response == HTTP_OK) {
                        etPassword.text.clear()
                        getSharedPreferences("EnterpriseIMS", MODE_PRIVATE).edit(commit = true) {
                            putString("username", userInfo.getString("username"))
                        }

                        startActivity(
                            Intent(this@MainActivity, DashboardActivity::class.java)
                                .putExtra("username", userInfo.getString("username"))
                                .putExtra("name", userInfo.getString("name"))
                                .putExtra("administrator", userInfo.getBoolean("administrator"))
                        )
                    } else {
                        tvError.text = Html.fromHtml(
                            getString(
                                R.string.error_message,
                                when (response) {
                                    HTTP_UNAUTHORIZED -> "Invalid credentials, please try again."
                                    HTTP_BAD_REQUEST -> "Empty credentials, please try again."
                                    else -> "Unexpected HTTP response code: $response."
                                }
                            ), Html.FROM_HTML_MODE_LEGACY
                        )
                        tvError.visibility = VISIBLE
                    }
                }
            }
        }
    }

    private suspend fun signIn(username: String, password: String): Int = withContext(Dispatchers.IO) {
        val conn = URL("https://wildfly.johnnyconsole.com:8443/ims/api/auth/sign-in")
            .openConnection() as HttpsURLConnection
        conn.requestMethod = "POST"
        conn.hostnameVerifier = HostnameVerifier { _, _ -> true }
        conn.doOutput = true
        conn.doInput = true
        with(conn.outputStream) {
            write("username=$username".toByteArray())
            write("&password=$password".toByteArray())
            flush()
            close()
        }
        conn.connect()
        val response = conn.responseCode
        if(response == HTTP_OK) {
            val data = StringBuffer()
            val reader = BufferedReader(InputStreamReader(conn.inputStream))
            for (line in reader.readLines()) {
                data.append(line)
            }
            userInfo = JSONObject(data.toString())
        }
        conn.disconnect()
        response
    }
}