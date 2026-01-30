package com.johnnyconsole.enterpriseims.android

import android.content.DialogInterface.BUTTON_NEGATIVE
import android.content.DialogInterface.BUTTON_POSITIVE
import android.content.Intent
import android.os.Bundle
import android.view.View.VISIBLE
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.johnnyconsole.enterpriseims.android.databinding.ActivityAddUserBinding

class AddUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddUserBinding

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

            onBackPressedDispatcher.addCallback(this@AddUserActivity, object: OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    confirmExit()
                }
            })
        }
    }

    private fun confirmExit() {val dialog = AlertDialog.Builder(this@AddUserActivity)
        .setTitle(R.string.confirm_exit_title)
        .setMessage(R.string.confirm_exit_message)
        .setPositiveButton(R.string.yes) { _, _ -> finish() }
        .setNegativeButton(R.string.no, null)
        .create()
        dialog.show()
        dialog.getButton(BUTTON_POSITIVE).setTextColor(getColor(R.color.success))
        dialog.getButton(BUTTON_NEGATIVE).setTextColor(getColor(R.color.error))}
}