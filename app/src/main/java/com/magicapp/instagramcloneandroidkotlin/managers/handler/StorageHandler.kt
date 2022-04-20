package com.magicapp.instagramcloneandroidkotlin.managers.handler

import java.lang.Exception

interface StorageHandler {
    fun onSuccess(imgUrl: String)
    fun onError(exception: Exception?)
}