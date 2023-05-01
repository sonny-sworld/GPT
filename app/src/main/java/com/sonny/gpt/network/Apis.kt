package com.sonny.gpt.network

import com.google.gson.JsonObject
import com.sonny.gpt.model.GptResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface Apis {
    @Headers(
        "Content-Type:application/json",
        "Authorization:Bearer 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")
    @POST("v1/completions")
    suspend fun postRequest(
        @Body json : JsonObject
    ) : GptResponse

}