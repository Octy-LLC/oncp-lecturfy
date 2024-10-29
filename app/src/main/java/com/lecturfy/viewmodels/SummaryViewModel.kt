package com.lecturfy.viewmodels

import androidx.lifecycle.SavedStateHandle
import android.content.SharedPreferences
import android.net.Uri
import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import dagger.hilt.android.qualifiers.ApplicationContext
import android.content.Context
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import okhttp3.ResponseBody
import retrofit2.Response
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.CompletableDeferred
import kotlin.coroutines.resume
import com.lecturfy.components.utils.FileUtils
import com.lecturfy.components.utils.FileResult
import com.lecturfy.components.google.GoogleSignInHelper
import com.lecturfy.components.google.GoogleSignInRequest
import com.lecturfy.components.system.NavController
import com.lecturfy.components.system.NavControllerViewModelDirectionEnum
import com.lecturfy.components.system.SnackbarEntity
import com.lecturfy.repositories.TranscriptionsRepository
import com.lecturfy.entities.*

data class SummaryViewModelState (
  val summary: SummaryEntity = SummaryEntity(
    linkToFile = "",
  ),

  
  val fileProgress: Int = 0,
  
  val makeTotalProgress: Int = 0,
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  

  

  val snackbar: SnackbarEntity? = null,
  val navController: NavController? = null,
  val googleSignInRequest: GoogleSignInRequest? = null
)
@HiltViewModel
class SummaryViewModel @Inject constructor (
  private val transcriptionsRepository : TranscriptionsRepository,
  private val savedStateHandle: SavedStateHandle,
  private val sharedPreferences: SharedPreferences,
  private val googleSignInHelper: GoogleSignInHelper,
  @ApplicationContext private val context: Context
) : ViewModel() {
  private val _state = MutableStateFlow<SummaryViewModelState>(SummaryViewModelState());
  val state = _state.asStateFlow()

  fun init() {
    viewModelScope.launch {
    val accessTokenLocal = sharedPreferences.getString("accessTokenLocal", "emptyToken")!!
    
    val summaryId = savedStateHandle.get<String>("id") ?:  "" 
    
    var summary = transcriptionsgetSummary(
      authorizationHeaderParam=accessTokenLocal,
      idPathParam=summaryId,
    )
    if(summary == null) return@launch  
    
    setSummary(summary.body()!!)
    
    }
  }

  override fun onCleared() {
    super.onCleared()
  }


fun setSummaryLinkToFile(linkToFile: String) {
    _state.update { currentState ->
        currentState.copy(summary = currentState.summary.copy(linkToFile = linkToFile))
    }
}
fun setSummary(item: SummaryEntity) {
    _state.update { currentState ->
        currentState.copy(summary = item)
    }
}



  fun setSnackbar(
    message: String,
    actionLabel: String? = null,
    withDismissAction: Boolean = false,
    duration: SnackbarDuration = SnackbarDuration.Short
  ) {
    _state.update { currentState ->
      currentState.copy(snackbar = SnackbarEntity(message, actionLabel, withDismissAction, duration))
    }
  }

  fun clearSnackbar() {
    _state.update { currentState ->
      currentState.copy(snackbar = null)
    }
  }

  fun setNavController(
    path: String,
    direction: NavControllerViewModelDirectionEnum = NavControllerViewModelDirectionEnum.TO
  ) {
    _state.update { currentState ->
      currentState.copy(
        navController = NavController(path, direction)
      )
    }
  }

  fun clearNavController() {
    _state.update { currentState ->
      currentState.copy(navController = null)
    }
  }

  suspend fun initiateGoogleSignIn(): String? = suspendCoroutine { cont ->
    _state.update { currentState ->
      currentState.copy(googleSignInRequest = GoogleSignInRequest {
          cont.resume(it)
      })
    }
  }

  fun resetGoogleSignInRequest() {
    _state.update { currentState ->
      currentState.copy(googleSignInRequest = null)
    }
  }

  
  fun setFileProgressListener(progress: Int) {
    _state.update { it.copy(fileProgress = progress) }
  }
  
  fun setmakeTotalProgress(progress: Int) {
    _state.update { it.copy(makeTotalProgress = progress) }
  }
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
    
  suspend fun transcriptionsmake(
fileEntity: String,




contentlanguageHeaderParam: String,
contentdispositionHeaderParam: String,

authorizationHeaderParam: String,

): Response<ResponseBody>?{

    
    
    setFileProgressListener(0)
    
    
    
    
    setmakeTotalProgress(0)


    val response = this.transcriptionsRepository.make(    fileEntity,



contentlanguageHeaderParam,
contentdispositionHeaderParam,

authorizationHeaderParam,

      fileProgressListener = { bytesRead, contentLength, done ->
        val progress = (100 * bytesRead / contentLength).toInt()
        setFileProgressListener(progress)
      },


totalProgressListener = { bytesRead, contentLength, done ->
  val progress = (100 * bytesRead / contentLength).toInt()
  setmakeTotalProgress(progress)
}
)

    if (response.isSuccessful) {
      
      
      setFileProgressListener(100)
      
      
      
      
      setmakeTotalProgress(100)
    } 
    else {
      
      
      setFileProgressListener(0)
      
      
      
      
      setmakeTotalProgress(0)
    }

    return response
  }
  suspend fun transcriptionsgetTranscriptions(



authorizationHeaderParam: String,

): Response<List<TranscriptionspaginationEntity>>?{

    
    


    val response = this.transcriptionsRepository.getTranscriptions(



authorizationHeaderParam,



)


    return response
  }
  suspend fun transcriptionsgetOne(
idPathParam: String,



authorizationHeaderParam: String,

): Response<TranscriptionEntity>?{

    
    


    val response = this.transcriptionsRepository.getOne(
idPathParam,



authorizationHeaderParam,



)


    return response
  }
  suspend fun transcriptionspatchOne(
transcriptionEntity: TranscriptionpatchEntity?,


idPathParam: String,



authorizationHeaderParam: String,

): Response<TranscriptionpatchedEntity>?{

    
    
    
    
    
    


    val response = this.transcriptionsRepository.patchOne(    transcriptionEntity,

idPathParam,



authorizationHeaderParam,



)


    return response
  }
  suspend fun transcriptionsdeleteOne(
idPathParam: String,



authorizationHeaderParam: String,

): Response<ResponseBody>?{

    
    


    val response = this.transcriptionsRepository.deleteOne(
idPathParam,



authorizationHeaderParam,



)


    return response
  }
  suspend fun transcriptionsmakeSummary(
idPathParam: String,



authorizationHeaderParam: String,

): Response<SummarycreatedEntity>?{

    
    


    val response = this.transcriptionsRepository.makeSummary(
idPathParam,



authorizationHeaderParam,



)


    return response
  }
  suspend fun transcriptionsgetSummary(
idPathParam: String,



authorizationHeaderParam: String,

): Response<SummaryEntity>?{

    
    


    val response = this.transcriptionsRepository.getSummary(
idPathParam,



authorizationHeaderParam,



)


    return response
  }
}