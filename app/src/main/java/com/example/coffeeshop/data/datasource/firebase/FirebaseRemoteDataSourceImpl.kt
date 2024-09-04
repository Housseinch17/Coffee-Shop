package com.example.coffeeshop.data.datasource.firebase

import android.util.Log
import com.example.coffeeshop.AuthenticationStatus
import com.example.coffeeshop.ui.screen.login.AuthState
import com.example.coffeeshop.ui.screen.signup.AccountStatus
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume

class FirebaseRemoteDataSourceImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FirebaseRemoteDataSource {

    override suspend fun getCurrentUser(): String? = withContext(coroutineDispatcher) {
        return@withContext try {
            auth.currentUser?.email
        }catch (e: Exception){
            Log.d("MyTag","getCurrentUser() ${e.message}")
            null
        }
    }


    //firebase login is asynchronous using suspendCancellableCoroutine ensuring that it has to wait
    //for  signInWithEmailAndPassword to complete same as using async await
    override suspend fun logIn(email: String, password: String): AuthState =
        withContext(coroutineDispatcher) {
            suspendCancellableCoroutine { continuation ->
                try {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                if (continuation.isActive) {
                                    continuation.resume(AuthState.LoggedIn)
                                }
                            } else {
                                val errorMessage = task.exception?.message ?: "Something went wrong"
                                if (continuation.isActive) {
                                    continuation.resume(AuthState.Error(errorMessage))
                                }
                            }
                        }
                } catch (e: Exception) {
                    // Handle exceptions here
                    if (continuation.isActive) {
                        continuation.resume(AuthState.Error(e.message ?: "Unknown error"))
                    }
                }
            }
        }


    override suspend fun signUp(email: String, password: String): AccountStatus =
        withContext(coroutineDispatcher) {
            suspendCancellableCoroutine { continuation ->
                try {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                if (continuation.isActive)
                                    continuation.resume(AccountStatus.IsCreated("Account is Created!"))
                            } else {
                                val errorMessage = task.exception?.message ?: "Something went wrong"
                                if (continuation.isActive)
                                    continuation.resume(AccountStatus.Error(errorMessage))
                            }
                        }
                }catch (e: Exception) {
                    // Handle exceptions here
                    if (continuation.isActive) {
                        continuation.resume(AccountStatus.Error(e.message ?: "Unknown error"))
                    }
                }
            }
        }

    override suspend fun signOut(): AuthenticationStatus = withContext(coroutineDispatcher) {
        auth.signOut()
        return@withContext AuthenticationStatus.UnAuthenticated
    }
}

