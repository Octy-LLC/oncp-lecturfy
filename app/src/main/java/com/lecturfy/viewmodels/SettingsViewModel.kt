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
import com.lecturfy.repositories.UsersettingsRepository
import com.lecturfy.repositories.UserprofileRepository
import com.lecturfy.entities.*

data class SettingsViewModelState (
  val userProfile: UsergetprofileEntity = UsergetprofileEntity(
    id = "",
    firstName = "",
    lastName = "",
    email = "",
    createdAt = "",
  ),
  val userProfileToUpdate: UserprofileEntity = UserprofileEntity(
    firstName = "",
    lastName = "",
  ),
  val userSettings: UsersettingsEntity = UsersettingsEntity(
    shouldSendEmailOnTranscribe = false,
  ),

  
  
  
  
  
  
  
  
  
  
  
  
  
  

  

  val snackbar: SnackbarEntity? = null,
  val navController: NavController? = null,
  val googleSignInRequest: GoogleSignInRequest? = null
)
@HiltViewModel
class SettingsViewModel @Inject constructor (
  private val usersettingsRepository : UsersettingsRepository,
  private val userprofileRepository : UserprofileRepository,
  private val savedStateHandle: SavedStateHandle,
  private val sharedPreferences: SharedPreferences,
  private val googleSignInHelper: GoogleSignInHelper,
  @ApplicationContext private val context: Context
) : ViewModel() {
  private val _state = MutableStateFlow<SettingsViewModelState>(SettingsViewModelState());
  val state = _state.asStateFlow()

  fun init() {
    viewModelScope.launch {
    val accessTokenLocal = sharedPreferences.getString("accessTokenLocal", "emptyToken")!!
    
    
    if(accessTokenLocal == "emptyToken"){
      var tokenCheckResponse = userProfilegetProfile(
        authorizationHeaderParam=accessTokenLocal,
      )
      if(tokenCheckResponse == null) return@launch  
      
      
      if(tokenCheckResponse.code() == 200){
      }
      else {
        setNavController(
            "LogIn"
        )
        
      }
    }
    else {
      setNavController(
          "LogIn"
      )
      
    }
    }
  }

  override fun onCleared() {
    super.onCleared()
  }


fun setUserprofileId(id: String) {
    _state.update { currentState ->
        currentState.copy(userProfile = currentState.userProfile.copy(id = id))
    }
}
fun setUserprofileFirstName(firstName: String) {
    _state.update { currentState ->
        currentState.copy(userProfile = currentState.userProfile.copy(firstName = firstName))
    }
}
fun setUserprofileLastName(lastName: String) {
    _state.update { currentState ->
        currentState.copy(userProfile = currentState.userProfile.copy(lastName = lastName))
    }
}
fun setUserprofileEmail(email: String) {
    _state.update { currentState ->
        currentState.copy(userProfile = currentState.userProfile.copy(email = email))
    }
}
fun setUserprofileCreatedAt(createdAt: String) {
    _state.update { currentState ->
        currentState.copy(userProfile = currentState.userProfile.copy(createdAt = createdAt))
    }
}
fun setUserprofile(item: UsergetprofileEntity) {
    _state.update { currentState ->
        currentState.copy(userProfile = item)
    }
}




fun setUserprofiletoupdateFirstName(firstName: String) {
    _state.update { currentState ->
        currentState.copy(userProfileToUpdate = currentState.userProfileToUpdate.copy(firstName = firstName))
    }
}
fun setUserprofiletoupdateLastName(lastName: String) {
    _state.update { currentState ->
        currentState.copy(userProfileToUpdate = currentState.userProfileToUpdate.copy(lastName = lastName))
    }
}
fun setUserprofiletoupdate(item: UserprofileEntity) {
    _state.update { currentState ->
        currentState.copy(userProfileToUpdate = item)
    }
}




fun setUsersettingsShouldSendEmailOnTranscribe(shouldSendEmailOnTranscribe: Boolean) {
    _state.update { currentState ->
        currentState.copy(userSettings = currentState.userSettings.copy(shouldSendEmailOnTranscribe = shouldSendEmailOnTranscribe))
    }
}
fun setUsersettings(item: UsersettingsEntity) {
    _state.update { currentState ->
        currentState.copy(userSettings = item)
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

  
  
  
  
  
  
  
  
  
  
  
  
  
  
      
  suspend fun userSettingsget(



authorizationHeaderParam: String,

): Response<UsersettingsEntity>?{
    val accessTokenLocal = sharedPreferences.getString("accessTokenLocal", "emptyToken")
    
    
    if(accessTokenLocal == "emptyToken"){
      var tokenCheckResponse = userProfilegetProfile(
        authorizationHeaderParam=accessTokenLocal,
      )
      if(tokenCheckResponse == null) return null;
      
      
      
      if(tokenCheckResponse.code() == 200){
      }
      else {
        setNavController(
            "LogIn"
        )
        
      }
    }
    else {
      setNavController(
          "LogIn"
      )
      
    }

    
    


    val response = this.usersettingsRepository.get(



authorizationHeaderParam,



)


    return response
  }
  suspend fun userSettingspatch(
settingsEntity: UsersettingsEntity,





authorizationHeaderParam: String,

): Response<UsersettingsEntity>?{
    val accessTokenLocal = sharedPreferences.getString("accessTokenLocal", "emptyToken")
    
    
    if(accessTokenLocal == "emptyToken"){
      var tokenCheckResponse = userProfilegetProfile(
        authorizationHeaderParam=accessTokenLocal,
      )
      if(tokenCheckResponse == null) return null;
      
      
      
      if(tokenCheckResponse.code() == 200){
      }
      else {
        setNavController(
            "LogIn"
        )
        
      }
    }
    else {
      setNavController(
          "LogIn"
      )
      
    }

    
    
    
    
    
    


    val response = this.usersettingsRepository.patch(    settingsEntity,




authorizationHeaderParam,



)


    return response
  }
  
  suspend fun userProfilegetProfile(



authorizationHeaderParam: String,

): Response<UsergetprofileEntity>?{

    
    


    val response = this.userprofileRepository.getProfile(



authorizationHeaderParam,



)


    return response
  }
  suspend fun userProfilepatchProfile(
profileEntity: UserprofileEntity,





authorizationHeaderParam: String,

): Response<UserprofileEntity>?{
    val accessTokenLocal = sharedPreferences.getString("accessTokenLocal", "emptyToken")
    
    
    if(accessTokenLocal == "emptyToken"){
      var tokenCheckResponse = userProfilegetProfile(
        authorizationHeaderParam=accessTokenLocal,
      )
      if(tokenCheckResponse == null) return null;
      
      
      
      if(tokenCheckResponse.code() == 200){
      }
      else {
        setNavController(
            "LogIn"
        )
        
      }
    }
    else {
      setNavController(
          "LogIn"
      )
      
    }

    
    
    
    
    
    


    val response = this.userprofileRepository.patchProfile(    profileEntity,




authorizationHeaderParam,



)


    return response
  }
}