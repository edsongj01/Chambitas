package com.pds.chambitas.util.services;

import com.pds.chambitas.body.POSTData
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface APIService {

    @POST("/fcm/send")
    @Headers(
        "Authorization: key=AAAAxORy4Wo:APA91bHe4fEsvV4-QYl7siqHdHYgxMkSHT-eEKy6QCgf6DqILjrCMXOc9ZnwrTZW9rNGZTUeQzwo9hUQ8JnrfRQY8lEPuaG9q1102UcJicYhKIl25XN7BoYlWldm89hpWmE0KCFMoV8v",
        "Content-Type: application/json"
    )
    fun sendNotification(@Body postData: POSTData): Call<Void>

}

object ApiUtils {
    private var URL = "https://fcm.googleapis.com/"
    val apiService: APIService
    get() = RetrofitClient.getClient(URL)!!.create(APIService::class.java)
}
