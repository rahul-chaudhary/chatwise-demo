package com.example.chatwise.repository

import android.util.Log
import com.example.chatwise.RetrofitInstance
import com.example.chatwise.TAG
import com.example.chatwise.model.Root
import okio.IOException
import retrofit2.HttpException
import retrofit2.Response

suspend fun getResponse(): Response<Root>? {
    return try {
        RetrofitInstance.api.getProducts()
    } catch (e: IOException) {
        Log.e(TAG, "IOException: Network error or API not reachable", e)
        null  // Return null or handle it in a way that makes sense for your app
    } catch (e: HttpException) {
        Log.e(TAG, "HttpException: Non-2xx HTTP response", e)
        null  // Return null or handle it accordingly
    }
}