package com.magicapp.instagramcloneandroidkotlin.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.WindowManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.magicapp.instagramcloneandroidkotlin.R
import com.magicapp.instagramcloneandroidkotlin.managers.AuthManager
import com.magicapp.instagramcloneandroidkotlin.managers.PrefsManager
import com.magicapp.instagramcloneandroidkotlin.utills.Logger

//In SplashActivity, user can visit to SignInActivity or MainActivity

class SplashActivity : BaseActivity() {

    val TAG: String = SplashActivity::class.java.simpleName


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splash)
        initViews()
        countDownTimer()
    }

    private fun initViews() {
        countDownTimer()
        loadFCMToken()
    }

    private fun countDownTimer() {
        object : CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                if (AuthManager.isSignedIn()) {
                    callMainActivity(this@SplashActivity)
                } else {
                    callSignInActivity(this@SplashActivity)
                }
            }
        }.start()
    }

    private fun callSignInActivity() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun loadFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Logger.d(TAG,"Fetching FCM registration token failed")
                return@OnCompleteListener
                 }
            // Get new FCM registration token
            // Save it in locally to use later
            val token = task.result
            Logger.d("$TAG token:", token.toString())
            PrefsManager(this).storeDeviceToken(token.toString())

        })
    }

}