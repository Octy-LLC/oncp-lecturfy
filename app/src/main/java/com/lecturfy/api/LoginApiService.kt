package com.lecturfy.api

import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.DELETE
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.Path
import retrofit2.http.Query
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Headers
import okhttp3.MultipartBody
import retrofit2.http.Streaming
import retrofit2.http.Multipart
import retrofit2.http.Part
import retrofit2.http.Header
import okhttp3.RequestBody
import com.lecturfy.entities.*

interface LoginService {



    @POST("google")



    suspend fun google(
@Body code: GoogleauthEntity,





): Response<AuthtokenresponseEntity>
}
