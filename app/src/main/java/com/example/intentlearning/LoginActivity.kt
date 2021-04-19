package com.example.intentlearning

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.backendless.Backendless
import com.backendless.BackendlessUser
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    //similar to what we did in the stopwatch with the bundle
    //to access public constants in other classes, you have to put them in a companion object
    //without static in kotlin, this is how we make the variable accessible without creating an instance of the object.
    companion object{
        val EXTRA_USERNAME = "username" // to help us remember the key
        val EXTRA_PASSWORD = "password"
        // put the request code constant here
        val REQUEST_LOGIN_INFO = 1001
        val TAG = "loginactivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // initialize backendless
        Backendless.initApp(this, Constants.APP_ID, Constants.API_KEY)

        // make the backendless call to login when they click the login button
        button_login_login.setOnClickListener {
            //extract the username and password from the edittexts
            val username = editText_login_username.text.toString()
            val password = editText_login_password.text.toString()

            // any place in the documentation you see new AsyncCallBack<...>
            // kotlin version is object : AsyncCallback<...> {}
            Backendless.UserService.login(username, password, object : AsyncCallback<BackendlessUser> {
                override fun handleResponse(response: BackendlessUser?) {
                    //?. is the same as doing if(blash != null) {//then call that function or access var}
                    Toast.makeText(this@LoginActivity, "${response?.userId} has logged in.", Toast.LENGTH_SHORT).show()
                    val gradeListIntent = Intent(this@LoginActivity, GradeListActivity::class.java)
                    startActivity(gradeListIntent)
                    finish()
                }

                override fun handleFault(fault: BackendlessFault?) {
                    Toast.makeText(this@LoginActivity, "Something went wrong. Check the logs.", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "handleFault: " + fault?.message)
                }
            })
        }

        // make an onclicklistener for the sign up button
        button_login_signup.setOnClickListener {
            // extract the current text in the username box
            val username = editText_login_username.text.toString()

            // create an Intent that will launch the Registration Activity
            // Intent needs to know where you are coming from and where you are going
            // FileName::class.java gives you access to the class location
            val registrationIntent = Intent(this, RegistrationActivity::class.java)

            // store that username in an "extra" in that Intent
            registrationIntent.putExtra(EXTRA_USERNAME, username)

            // launch the new Activity using that Intent
            startActivityForResult(registrationIntent, REQUEST_LOGIN_INFO)

            //TODO: autofill login details
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_LOGIN_INFO){
            if(resultCode == Activity.RESULT_OK){
                editText_login_username.setText(data?.getStringExtra(RegistrationActivity.EXTRA_USERNAME))
                editText_login_password.setText(data?.getStringExtra(RegistrationActivity.EXTRA_PASSWORD))
            }
        }
    }
}