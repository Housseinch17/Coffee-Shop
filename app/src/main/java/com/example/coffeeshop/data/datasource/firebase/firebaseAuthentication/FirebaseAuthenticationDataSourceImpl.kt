package com.example.coffeeshop.data.datasource.firebase.firebaseAuthentication

import android.util.Log
import com.example.coffeeshop.ui.screen.login.AuthState
import com.example.coffeeshop.ui.screen.signup.AccountStatus
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseAuthenticationDataSourceImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FirebaseAuthenticationDataSource {

    override suspend fun getCurrentUser(): String? = withContext(coroutineDispatcher) {
        return@withContext try {
            auth.currentUser?.email
        } catch (e: Exception) {
            Log.d("MyTag", "getCurrentUser() ${e.message}")
            null
        }
    }

    //firebase login is asynchronous using suspendCancellableCoroutine ensuring that it has to wait
    //for  signInWithEmailAndPassword to complete same as using async await
    override suspend fun logIn(email: String, password: String): AuthState =
        withContext(coroutineDispatcher) {
               return@withContext try {
                   auth.signInWithEmailAndPassword(email, password).await()
                   AuthState.LoggedIn
               }catch (e:Exception) {
                   val errorMessage = e.message ?: "Something went wrong"
                   AuthState.Error(errorMessage)
               }
        }

    //firebase signup is asynchronous using suspendCancellableCoroutine ensuring that it has to wait
    //for  createUserWithEmailAndPassword to complete same as using async await
    override suspend fun signUp(email: String, password: String): AccountStatus =
        withContext(coroutineDispatcher) {
            return@withContext try {
                auth.createUserWithEmailAndPassword(email, password).await()
                AccountStatus.IsCreated("Account Created")
            } catch (e: Exception) {
                AccountStatus.Error(e.message ?: "Check your Internet")
            }
        }

    override suspend fun signOut() = withContext(coroutineDispatcher) {
        auth.signOut()
    }
}

