package com.lecturfy.repositories

import com.lecturfy.entities.*
import com.lecturfy.api.UserprofileService
import okhttp3.MediaType
import com.lecturfy.components.jsonpath.JsonPathParser
import com.lecturfy.components.jsonpath.DomainObjectProperty
import com.lecturfy.components.jsonpath.DomainObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import kotlinx.coroutines.Dispatchers
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.jayway.jsonpath.JsonPath
import okhttp3.ResponseBody
import retrofit2.Response

class UserprofileRepository @Inject constructor (
    private val apiService: UserprofileService,
) {
    suspend fun getProfile(



authorizationHeaderParam: String,



): Response<UsergetprofileEntity>{
        
        
        return apiService.getProfile(
        
        
        
        
        authorizationHeaderParam,
        );
    }
    suspend fun patchProfile(

profileEntity: UserprofileEntity,





authorizationHeaderParam: String,



): Response<UserprofileEntity>{
        
        
        return apiService.patchProfile(profile=profileEntity,
        
        
        
        
        
        authorizationHeaderParam,
        );
    }
}
