package com.example.intentlearning

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.backendless.Backendless
import com.backendless.BackendlessUser
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import kotlinx.android.synthetic.main.activity_registration.*


class RegistrationActivity : AppCompatActivity() {
    companion object{
        val EXTRA_USERNAME = "username" // to help us remember the key
        val EXTRA_PASSWORD = "password"
        // put the request code constant here
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        // test to see if the username came across

        // to "check the mail", we get the Intent that started this Activity by just using intent
        // "open the mail" by getting the extra from that intent
        val username = intent.getStringExtra(LoginActivity.EXTRA_USERNAME)

        // TODO: fill in the username field with whatever was in the intent.

        Toast.makeText(this, username, Toast.LENGTH_SHORT).show()

        button_register_cancel.setOnClickListener {

            finish()
        }

        button_register_register.setOnClickListener {
            when {
                editText_registration_username.text.toString().length < 3 -> {
                    Toast.makeText(this, "username too short", Toast.LENGTH_SHORT).show()
                }
                editText_registration_password.text.toString() != editText_registration_passwordconfirm.text.toString() -> {
                    Toast.makeText(this, "passwords don't match", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    registerUser()
                }
            }
        }
    }

    private fun registerUser(){
        // TODO: DATA VALIDATION TO MAKE SURE THESE FIELDS AREN'T EMPTY
        val user = BackendlessUser()
        user.setProperty("email", editText_registration_email.text.toString())
        user.setProperty("name", editText_registration_name.text.toString())
        user.setProperty("username", editText_registration_username.text.toString())
        user.password = editText_registration_password.text.toString()

        Backendless.UserService.register(user, object : AsyncCallback<BackendlessUser?> {
            override fun handleResponse(registeredUser: BackendlessUser?) {

                val intent = Intent().apply {
                    putExtra(EXTRA_USERNAME, editText_registration_username.text.toString())
                    putExtra(EXTRA_PASSWORD, editText_registration_password.text.toString())
                }
                Toast.makeText(this@RegistrationActivity, "REGISTRATION SUCCESSFUL", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK, intent)
                finish()
            }

            override fun handleFault(fault: BackendlessFault) {
                // an error has occurred, the error code can be retrieved with fault.getCode()
                Toast.makeText(this@RegistrationActivity, fault.code.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }
}