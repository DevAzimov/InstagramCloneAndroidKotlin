package com.magicapp.instagramcloneandroidkotlin.managers.handler

import com.magicapp.instagramcloneandroidkotlin.model.Post

interface DBPostHandler {
    fun onSuccess(post: Post)
    fun onError(e: Exception)
}