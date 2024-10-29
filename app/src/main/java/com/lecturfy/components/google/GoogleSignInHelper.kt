package com.lecturfy.components.google

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

data class GoogleSignInRequest(
    val setCode: (String?) -> Unit
)

class GoogleSignInHelper(
    private val context: Context
) {
    private lateinit var googleSignInClient: GoogleSignInClient

    fun configure(
        webClientId: String,
        scopes: List<String> = emptyList()
    ) {
        val gsoBuilder = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)

        gsoBuilder.requestServerAuthCode(webClientId, false)

        if (scopes.isNotEmpty()) {
            scopes.forEach { scope ->
                gsoBuilder.requestScopes(Scope(scope))
            }
        }

        val gso = gsoBuilder.build()
        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }

    fun getSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }

    fun handleSignInResult(completedTask: Task<GoogleSignInAccount>): String? {
        return try {
            val account = completedTask.getResult(ApiException::class.java)
            account.serverAuthCode
        } catch (e: ApiException) {
            null
        }
    }
}


suspend fun launchGoogleSignIn(
    signInLauncher: ActivityResultLauncher<Intent>,
    googleSignInHelper: GoogleSignInHelper,
): String? = suspendCoroutine { cont ->
    val callback = ActivityResultCallback<ActivityResult> { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        val authCode = googleSignInHelper.handleSignInResult(task)
        cont.resume(authCode)
    }

    signInLauncher.launch(googleSignInHelper.getSignInIntent())
}