package com.sonny.gpt.repository

import com.google.gson.JsonObject
import com.sonny.gpt.network.Apis
import com.sonny.gpt.network.RetrofitInstance
import org.json.JSONObject

class NetWorkRepository {

    private val chatGPTClient = RetrofitInstance.getInstance().create(Apis::class.java)

    suspend fun postResponse(jsonData : JsonObject) = chatGPTClient.postRequest(jsonData)
}