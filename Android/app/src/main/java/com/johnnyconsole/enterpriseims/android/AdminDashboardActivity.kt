package com.johnnyconsole.enterpriseims.android

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.johnnyconsole.enterpriseims.android.databinding.ActivityAdminDashboardBinding

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        with(binding) {
            setContentView(root)
            ViewCompat.setOnApplyWindowInsetsListener(main) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
                insets
            }

            btProvisionUser.setOnClickListener { _ ->
                startActivity(
                    Intent(this@AdminDashboardActivity, AddUserActivity::class.java)
                        .putExtras(intent)
                )
            }

            btDeleteUser.setOnClickListener { _ ->
                startActivity(
                    Intent(this@AdminDashboardActivity, DeleteUserActivity::class.java)
                        .putExtras(intent)
                )
            }

            btBack.setOnClickListener { _ ->
                finish()
            }

        }
    }
}