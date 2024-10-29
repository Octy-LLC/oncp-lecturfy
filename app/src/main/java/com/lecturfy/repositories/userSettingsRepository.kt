package com.lecturfy.repositories

import com.lecturfy.entities.*
import com.lecturfy.api.UsersettingsService
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

class UsersettingsRepository @Inject constructor (
    private val apiService: UsersettingsService,
) {
    suspend fun get(



authorizationHeaderParam: String,



): Response<UsersettingsEntity>{
        
        
        return apiService.get(
        
        
        
        
        authorizationHeaderParam,
        );
    }
    suspend fun patch(

settingsEntity: UsersettingsEntity,





authorizationHeaderParam: String,



): Response<UsersettingsEntity>{
        
        
        return apiService.patch(settings=settingsEntity,
        
        
        
        
        
        authorizationHeaderParam,
        );
    }
}
