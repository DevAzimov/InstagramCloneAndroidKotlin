package com.magicapp.instagramcloneandroidkotlin.managers.handler

import com.magicapp.instagramcloneandroidkotlin.model.Post

interface DBFollowHandler {
    fun onSuccess(isDone: Boolean)
    fun onError(e: Exception)
}