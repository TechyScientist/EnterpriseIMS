package com.johnnyconsole.enterpriseims.android

import android.content.Intent
import android.os.AsyncTask
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
import java.net.HttpURLConnection.HTTP_BAD_REQUEST

class MainActivity : AppCompatActivity() {

    private inner class SignInTask: AsyncTask<String, Unit, Unit>() {

        private var response = 200
        private lateinit var userInfo: JSONObject

        override fun onPreExecute() {
            super.onPreExecute()
            binding.indicator.visibility = VISIBLE
            binding.tvError.visibility = GONE
        }

        override fun doInBackground(vararg params: String?) {
            val conn = URL("https://wildfly.johnnyconsole.com:8443/ims/api/auth/sign-in")
                .openConnection() as HttpsURLConnection
            conn.requestMethod = "POST"
            conn.hostnameVerifier = HostnameVerifier { _, _ -> true }
            conn.doOutput = true
            conn.doInput = true
            with(conn.outputStream) {
                write("username=${params[0]}".toByteArray())
                write("&password=${params[1]}".toByteArray())
                flush()
                close()
            }
            conn.connect()
            if(conn.responseCode == HTTP_OK) {
                val data = StringBuffer();
                val reader = BufferedReader(InputStreamReader(conn.inputStream))
                for (line in reader.readLines()) {
                    data.append(line)
                }
                userInfo = JSONObject(data.toString())
            }
            else {
                response = conn.responseCode
            }
            conn.disconnect()
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)
            binding.indicator.visibility = INVISIBLE
            if(response == HTTP_OK) {
                binding.etPassword.text.clear()
                getSharedPreferences("EnterpriseIMS", MODE_PRIVATE).edit(commit=true) {
                    putString("username", userInfo.getString("username"))
                }

                startActivity(
                    Intent(this@MainActivity, DashboardActivity::class.java)
                        .putExtra("username", userInfo.getString("username"))
                        .putExtra("name", userInfo.getString("name"))
                        .putExtra("administrator", userInfo.getBoolean("administrator"))
                )
            }
            else {
                binding.tvError.text = Html.fromHtml(getString(R.string.error_message,
                    when(response) {
                        HTTP_UNAUTHORIZED -> "Invalid credentials, please try again."
                        HTTP_BAD_REQUEST -> "Empty credentials, please try again."
                        else -> "Unexpected HTTP response code: $response."
                    }), Html.FROM_HTML_MODE_LEGACY)
                binding.tvError.visibility = VISIBLE
            }
        }

    }

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
                SignInTask().execute(
                    etUsername.text.toString().lowercase(),
                    etPassword.text.toString()
                )
            }
        }
    }
}