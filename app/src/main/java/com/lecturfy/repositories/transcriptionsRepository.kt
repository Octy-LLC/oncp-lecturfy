package com.lecturfy.repositories

import com.lecturfy.entities.*
import com.lecturfy.api.TranscriptionsService
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

class TranscriptionsRepository @Inject constructor (
    private val apiService: TranscriptionsService,
) {
    suspend fun make(

fileEntity: String,




contentlanguageHeaderParam: String,
contentdispositionHeaderParam: String,

authorizationHeaderParam: String,

fileProgressListener: (bytesRead: Long, contentLength: Long, done: Boolean) -> Unit,


totalProgressListener: (bytesRead: Long, contentLength: Long, done: Boolean) -> Unit
): Response<ResponseBody>{
        var fileFile = File(fileEntity)
        var fileTotalSize = fileFile.length()
        val totalSize = fileTotalSize
        var uploadedFileBytes = 0L
        var uploadedTotalBytes = 0L
        val GSON = Gson()
        val fileRequestBody = object : RequestBody() {
          override fun contentType(): MediaType? {
            return "application/octet-stream".toMediaTypeOrNull()
          }
        
          override fun contentLength(): Long = fileTotalSize
        
          override fun writeTo(sink: BufferedSink) {
            fileFile.inputStream().use { inputStream ->
              val buffer = ByteArray(8 * 1024)
              var read: Int
              while (inputStream.read(buffer).also { read = it } != -1) {
                uploadedFileBytes += read
                uploadedTotalBytes += read
                sink.write(buffer, 0, read)
                fileProgressListener(uploadedFileBytes, totalSize, uploadedFileBytes == totalSize)
                totalProgressListener(uploadedTotalBytes,totalSize,uploadedTotalBytes == totalSize)
              }
            }
          }
        }
        val filePart = MultipartBody.Part.createFormData("file", fileFile.name, fileRequestBody)
        
        
        return apiService.make(file=fileRequestBody,
        
        
        
        
        contentlanguageHeaderParam,
        contentdispositionHeaderParam,
        
        authorizationHeaderParam,
        )
    }
    suspend fun getTranscriptions(



authorizationHeaderParam: String,



): Response<List<TranscriptionspaginationEntity>>{
        
        
        
          val response: Response<ResponseBody> = apiService.getTranscriptions(
        
        
        100,
        0,
        
        
        authorizationHeaderParam,
        )
          val responseBody: ResponseBody? = response.body()  
          
          return if (response.isSuccessful) {
              val json = responseBody!!.string()
              val domainObject = DomainObject(
          properties = listOf(
            DomainObjectProperty(
              name = "id",
              type = "string",
              jsonPath = "$.transcriptions[*].id",
              isOptional = false
            ),
            DomainObjectProperty(
              name = "title",
              type = "string",
              jsonPath = "$.transcriptions[*].title",
              isOptional = false
            ),
            DomainObjectProperty(
              name = "characters",
              type = "int",
              jsonPath = "$.transcriptions[*].characters",
              isOptional = false
            ),
            DomainObjectProperty(
              name = "linkToFile",
              type = "string",
              jsonPath = "$.transcriptions[*].linkToFile",
              isOptional = false
            )
          )
        )
        
              val jsonParser = JsonPathParser()
              val responseBodyTransformed = jsonParser.parseList(json, domainObject, TranscriptionspaginationEntity::class.java)
        
              Response.success(response.code(), responseBodyTransformed)
          } else {
              Response.error(response.code(), response.errorBody() ?: ResponseBody.create("application/json".toMediaTypeOrNull(), "Error"))
          }
        
        
    }
    suspend fun getOne(
idPathParam: String,



authorizationHeaderParam: String,



): Response<TranscriptionEntity>{
        
        
        
        return apiService.getOne(
        
        idPathParam,
        
        
        
        authorizationHeaderParam,
        );
        
        
    }
    suspend fun patchOne(

transcriptionEntity: TranscriptionpatchEntity?,


idPathParam: String,



authorizationHeaderParam: String,



): Response<TranscriptionpatchedEntity>{
        
        
        
        return apiService.patchOne(transcription=transcriptionEntity,
        
        
        idPathParam,
        
        
        
        authorizationHeaderParam,
        );
        
        
    }
    suspend fun deleteOne(
idPathParam: String,



authorizationHeaderParam: String,



): Response<ResponseBody>{
        
        
        return apiService.deleteOne(
        
        idPathParam,
        
        
        
        authorizationHeaderParam,
        )
    }
    suspend fun makeSummary(
idPathParam: String,



authorizationHeaderParam: String,



): Response<SummarycreatedEntity>{
        
        
        
          val response: Response<ResponseBody> = apiService.makeSummary(
        
        idPathParam,
        
        
        
        authorizationHeaderParam,
        );
          val responseBody: ResponseBody? = response.body()
          return if (response.isSuccessful) {
            val json = responseBody!!.string()
            val domainObject = DomainObject(
          properties = listOf(
            DomainObjectProperty(
              name = "id",
              type = "string",
              jsonPath = "$.id",
              isOptional = false
            ),
            DomainObjectProperty(
              name = "characters",
              type = "string",
              jsonPath = "$.characters",
              isOptional = false
            ),
            DomainObjectProperty(
              name = "linkToFile",
              type = "string",
              jsonPath = "$.linkToFile",
              isOptional = false
            )
          )
        )
        
            val jsonParser = JsonPathParser()
            val responseBodyTransformed = jsonParser.parseEntity(json, domainObject, SummarycreatedEntity::class.java)
        
            Response.success(response.code(), responseBodyTransformed)
          } else {
            Response.error(response.code(), response.errorBody() ?: ResponseBody.create("application/json".toMediaTypeOrNull(), "Error"))
          }
        
        
    }
    suspend fun getSummary(
idPathParam: String,



authorizationHeaderParam: String,



): Response<SummaryEntity>{
        
        
        
        return apiService.getSummary(
        
        idPathParam,
        
        
        
        authorizationHeaderParam,
        );
        
        
    }
}
