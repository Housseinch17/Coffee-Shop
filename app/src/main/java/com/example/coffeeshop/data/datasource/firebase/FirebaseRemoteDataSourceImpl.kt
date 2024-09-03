package com.example.coffeeshop.data.datasource.firebase

import com.example.coffeeshop.AuthState
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

    override suspend fun checkStatus(): AuthState = withContext(coroutineDispatcher) {
        val userName = auth.currentUser
        return@withContext  if(userName == null){
            AuthState.UnAuthenticated
        }else{
            AuthState.Authenticated
        }
    }

    override suspend fun logIn(email: String, password: String): AuthState =
        withContext(coroutineDispatcher) {
            suspendCancellableCoroutine { continuation ->
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            if (continuation.isActive) { // Check if the continuation is still active
                                continuation.resume(AuthState.Authenticated)
                            }
                        } else {
                            val errorMessage = task.exception?.message ?: "Something went wrong"
                            if (continuation.isActive) { // Check if the continuation is still active
                                continuation.resume(AuthState.Error(errorMessage))
                            }
                        }
                    }
            }
        }



    override suspend fun signUp(email: String, password: String): AccountStatus =
        withContext(coroutineDispatcher) {
            suspendCancellableCoroutine { continuation ->
                auth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener { task->
                        if(task.isSuccessful){
                            if(continuation.isActive)
                                continuation.resume(AccountStatus.IsCreated("Account is Created!"))
                        }else{
                            val errorMessage = task.exception?.message ?: "Something went wrong"
                            if(continuation.isActive)
                                continuation.resume(AccountStatus.Error(errorMessage))
                        }
                    }
            }
        }

    override suspend fun signOut(): AuthState = withContext(coroutineDispatcher) {
        auth.signOut()
        return@withContext AuthState.UnAuthenticated
    }

    override suspend fun getUsername(): String = withContext(coroutineDispatcher){
        return@withContext auth.currentUser?.email.toString()
    }
}

