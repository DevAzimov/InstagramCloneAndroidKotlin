package com.magicapp.instagramcloneandroidkotlin.utills

import android.util.Log

object Logger {
    private const val IS_TESTER = true
    fun d(tag: String, msg: String) {
        if (IS_TESTER) Log.d(tag, msg)
    }

    fun i(tag: String, msg: String) {
        if (IS_TESTER) Log.i(tag, msg)
    }

    fun v(tag: String, msg: String) {
        if (IS_TESTER) Log.v(tag, msg)
    }

    fun e(tag: String, msg: String) {
        if (IS_TESTER) Log.e(tag, msg)
    }
}