package com.magicapp.instagramcloneandroidkotlin.managers.handler

import com.magicapp.instagramcloneandroidkotlin.model.Post


interface DBPostsHandler {
    fun onSuccess(posts: ArrayList<Post>)
    fun onError(e: Exception)
}