package com.noteapp.core.service

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AuthServiceImpl(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
):AuthService {
    override suspend fun login(context: Context): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val idToken = getGoogleCredential(context)
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                val authResult = firebaseAuth.signInWithCredential(credential).await()
                authResult.user != null
            } catch (e: GetCredentialException) {
                Log.d("AuthService","Google Sign-in failed", e)
                false
            } catch (e: Exception) {
                Log.d("AuthService","Something went wrong", e)
                false
            }
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }

    override fun getLoggedInUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    override fun getUid(): String? {
        return firebaseAuth.uid
    }

    private suspend fun getGoogleCredential(context: Context): String?{
        val credentialManager = CredentialManager.create(context)
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId("760422378751-8o9ifbtro1n9bc9abr953kf7e43o7tf6.apps.googleusercontent.com")
            .build()

        val req = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return try {
            val result = credentialManager.getCredential(context,req)
            Log.d("debugging", result.credential.data.toString())
            result.credential.data.getString("com.google.android.libraries.identity.googleid.BUNDLE_KEY_ID_TOKEN")
        }catch (e:Exception){
            Log.e("authService","Error fetching credential", e)
            null
        }
    }

}