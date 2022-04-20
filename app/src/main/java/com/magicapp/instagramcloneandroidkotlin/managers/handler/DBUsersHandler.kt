package com.magicapp.instagramcloneandroidkotlin.managers.handler

import com.magicapp.instagramcloneandroidkotlin.model.User


interface DBUsersHandler {
    fun onSuccess(users: ArrayList<User>)
    fun onError(e: Exception)
}