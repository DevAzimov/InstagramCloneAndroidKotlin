package com.magicapp.instagramcloneandroidkotlin.managers.handler

import com.magicapp.instagramcloneandroidkotlin.model.User


interface DBUserHandler {
    fun onSuccess(user: User? = null)
    fun onError(e: Exception)
}