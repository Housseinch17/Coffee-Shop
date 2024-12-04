package com.example.coffeeshop.data.datasource.firebase.firebaseAuthentication

import android.util.Log
import com.example.coffeeshop.data.datasource.firebase.firebaseWriteData.FirebaseWriteDataDataSourceImpl
import com.example.coffeeshop.ui.screen.login.AuthState
import com.example.coffeeshop.ui.screen.settingspage.PasswordChangement
import com.example.coffeeshop.ui.screen.signup.AccountStatus
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirebaseAuthenticationDataSourceImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firebaseWriteDataDataSourceImpl: FirebaseWriteDataDataSourceImpl,
    @Named("DispatchersIO") private val coroutineDispatcher: CoroutineDispatcher,

    ) : FirebaseAuthenticationDataSource {

    override suspend fun getCurrentUser(): String? = withContext(coroutineDispatcher){
        return@withContext try {
            auth.currentUser?.email
        } catch (e: Exception) {
            Log.d("MyTag", "getCurrentUser() ${e.message}")
            null
        }
    }

    override suspend fun changePassword(email: String, newPassword: String): PasswordChangement =
        withContext(coroutineDispatcher) {
            suspendCoroutine { continuation->
                try {
                    auth.currentUser?.updatePassword(newPassword)?.addOnCompleteListener { task ->
                         if (task.isSuccessful) {
                            continuation.resume(PasswordChangement.Success("Password changed successfully!"))
                        } else {
                            continuation.resume(PasswordChangement.Error("Password didn't change!"))
                        }
                    }
                } catch (e: Exception) {
                    continuation.resume(PasswordChangement.Error(e.message ?: "Check your internet"))
                }
            }
        }

    override suspend fun resetPassword(email: String): PasswordChangement = withContext(coroutineDispatcher) {
        suspendCoroutine { continuation ->
            try {
                Log.d("MyTag", "f1")
                auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(PasswordChangement.Success("Check your email, to reset your password ! "))
                    } else {
                        continuation.resume(PasswordChangement.Error("Failed to reset password, check internet!"))
                    }
                }
            } catch (e: Exception) {
                continuation.resume(
                    PasswordChangement.Error(
                        e.message ?: "Check your internet"
                    )
                )
            }
        }
    }

    //firebase login is asynchronous using await ensuring that it has to wait
    //for  signInWithEmailAndPassword to complete same as using async await
    override suspend fun logIn(email: String, password: String): AuthState =
        withContext(coroutineDispatcher) {
            suspendCoroutine { continuation ->
                try {
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task->
                        if(task.isSuccessful){
                            continuation.resume(AuthState.LoggedIn)
                        }else{
                            continuation.resume(AuthState.Error("error: ${task.exception}"))
                        }
                    }
                }catch (e: Exception){
                    continuation.resume(AuthState.Error("Error: ${e.message}"))
                }
            }
        }

    //firebase signup is asynchronous using await ensuring that it has to wait
    //for  createUserWithEmailAndPassword to complete same as using async await
    override suspend fun signUp(email: String, password: String): AccountStatus =
        withContext(coroutineDispatcher) {
            try {
                // Perform the first operation with suspendCoroutine for Firebase Auth
                suspendCoroutine { continuation ->
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                continuation.resume(Unit) // Resume if successful
                            } else {
                                continuation.resumeWithException(task.exception ?: Exception("Auth failed"))
                            }
                        }
                }
                //this is a suspend function so it will perform directly
                firebaseWriteDataDataSourceImpl.addEmailToUsers(email)

                // If both operations succeed
                AccountStatus.IsCreated("Account Created")
            } catch (e: Exception) {
                AccountStatus.Error(e.message ?: "Check your Internet")
            }
        }



    override suspend fun signOut() = withContext(coroutineDispatcher) {
        auth.signOut()
    }


}

