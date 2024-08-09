package com.example.chatwise

import com.example.chatwise.model.Root
import retrofit2.Response
import retrofit2.http.GET

interface ProductAPI {
    @GET("/products")
    suspend fun getProducts(): Response<Root>
}