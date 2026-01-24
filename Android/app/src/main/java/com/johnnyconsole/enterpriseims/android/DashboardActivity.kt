package com.johnnyconsole.enterpriseims.android

import android.content.DialogInterface.BUTTON_NEGATIVE
import android.content.DialogInterface.BUTTON_POSITIVE
import android.os.Bundle
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.johnnyconsole.enterpriseims.android.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        with(binding) {
            setContentView(root)
            ViewCompat.setOnApplyWindowInsetsListener(main) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
                insets
            }

            tvDisplay.text = getString(R.string.signed_in_as,
                intent.getStringExtra("username")
            )

            if(intent.getBooleanExtra("administrator", false)) {
                tvAdminBadge.visibility = VISIBLE
                btAdministration.visibility = VISIBLE
            }

            btSignOut.setOnClickListener { _ ->
                val dialog = AlertDialog.Builder(this@DashboardActivity)
                    .setTitle(R.string.confirm_sign_out_title)
                    .setMessage(R.string.confirm_sign_out_message)
                    .setPositiveButton(R.string.yes) { _, _ -> finish() }
                    .setNegativeButton(R.string.no, null)
                    .create()
                dialog.show()
                dialog.getButton(BUTTON_POSITIVE).setTextColor(getColor(R.color.success))
                dialog.getButton(BUTTON_NEGATIVE).setTextColor(getColor(R.color.error))

            }

        }
    }
}