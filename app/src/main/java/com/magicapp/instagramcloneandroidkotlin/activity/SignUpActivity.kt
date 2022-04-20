package com.magicapp.instagramcloneandroidkotlin.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.magicapp.instagramcloneandroidkotlin.R
import com.magicapp.instagramcloneandroidkotlin.managers.AuthManager
import com.magicapp.instagramcloneandroidkotlin.managers.DatabaseManager
import com.magicapp.instagramcloneandroidkotlin.managers.PrefsManager
import com.magicapp.instagramcloneandroidkotlin.managers.handler.AuthHandler
import com.magicapp.instagramcloneandroidkotlin.managers.handler.DBUserHandler
import com.magicapp.instagramcloneandroidkotlin.model.User
import com.magicapp.instagramcloneandroidkotlin.utills.Extensions.toast
import com.magicapp.instagramcloneandroidkotlin.utills.Utils
import kotlin.Exception

//In SignUpActivity, user can signup using fullname, email, password

class SignUpActivity : BaseActivity() {
    val TAG = SignUpActivity::class.java.toString()
    lateinit var et_fullname: EditText
    lateinit var et_password: EditText
    lateinit var et_email: EditText
    lateinit var et_cpassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        initViews()
    }

    private fun initViews() {
        et_fullname = findViewById(R.id.et_fullname)
        et_email = findViewById(R.id.et_email)
        et_password = findViewById(R.id.et_password)
        et_cpassword = findViewById(R.id.et_cpassword)

        val b_signUp = findViewById<Button>(R.id.b_signup)
        b_signUp.setOnClickListener {
            val fullname = et_fullname.text.toString().trim()
            val email = et_email.text.toString().trim()
            val password = et_password.text.toString().trim()
            if (fullname.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                val user = User(fullname, email, password, "")
                firebaseSignUp(user)
            }
        }
        val tv_signIn = findViewById<TextView>(R.id.tv_signin)
        tv_signIn.setOnClickListener { finish() }
    }

    private fun firebaseSignUp(user: User){
        showLoading(this)
        AuthManager.signUp(user.email, user.password, object : AuthHandler {
            override fun onSuccess(uid: String) {
                dismissLoading()
                user.uid = uid
                storeUserToDB(user)
                toast("Signed up successfully")
            }

            override fun onError(exception: Exception?) {
                dismissLoading()
                toast("Sign up failed")
            }

        })
    }

    private fun storeUserToDB(user: User) {
        user.device_token = PrefsManager(this).loadDeviceToken()!!
        user.device_id = Utils.getDeviceID(this)


        DatabaseManager.storeUser(user, object : DBUserHandler{
            override fun onSuccess(user: User?) {
                    dismissLoading()
                    callMainActivity(context)
                }
                override fun onError(e: Exception) {

                }
        })
    }
}