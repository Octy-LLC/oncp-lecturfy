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

interface TranscriptionsService {



    @POST("transcriptions")


    @Streaming

    suspend fun make(
@Body file: RequestBody,




@Header("Content-Language") contentlanguageHeaderParam: String? =null,
@Header("Content-Disposition") contentdispositionHeaderParam: String ,

@Header("Authorization") authorizationHeaderParam: String ,
): Response<ResponseBody>


    @GET("transcriptions/user/me")



    suspend fun getTranscriptions(

@Query("limit") limitQueryParam: Int ,
@Query("offset") offsetQueryParam: Int ,


@Header("Authorization") authorizationHeaderParam: String ,
): Response<ResponseBody>


    @GET("transcriptions/{id}")



    suspend fun getOne(
@Path("id") idPathParam: String,



@Header("Authorization") authorizationHeaderParam: String ,
): Response<TranscriptionEntity>


    @PATCH("transcriptions/{id}")



    suspend fun patchOne(
@Body transcription: TranscriptionpatchEntity?,


@Path("id") idPathParam: String,



@Header("Authorization") authorizationHeaderParam: String ,
): Response<TranscriptionpatchedEntity>


    @DELETE("transcriptions/{id}")



    suspend fun deleteOne(
@Path("id") idPathParam: String,



@Header("Authorization") authorizationHeaderParam: String ,
): Response<ResponseBody>


    @POST("transcriptions/{id}/summary")



    suspend fun makeSummary(
@Path("id") idPathParam: String,



@Header("Authorization") authorizationHeaderParam: String ,
): Response<ResponseBody>


    @GET("transcriptions/{id}/summary")



    suspend fun getSummary(
@Path("id") idPathParam: String,



@Header("Authorization") authorizationHeaderParam: String ,
): Response<SummaryEntity>
}
