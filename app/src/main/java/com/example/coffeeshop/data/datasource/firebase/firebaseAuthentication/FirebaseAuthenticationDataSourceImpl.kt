package com.example.coffeeshop.data.datasource.firebase.firebaseAuthentication

import android.util.Log
import com.example.coffeeshop.data.datasource.firebase.firebaseWriteData.FirebaseWriteDataDataSourceImpl
import com.example.coffeeshop.ui.screen.login.AuthState
import com.example.coffeeshop.ui.screen.settingspage.PasswordChangement
import com.example.coffeeshop.ui.screen.signup.AccountStatus
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class FirebaseAuthenticationDataSourceImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firebaseWriteDataDataSourceImpl: FirebaseWriteDataDataSourceImpl,
    @Named("DispatchersIO") private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,

    ) : FirebaseAuthenticationDataSource {

    override suspend fun getCurrentUser(): String? = withContext(coroutineDispatcher) {
        return@withContext try {
            auth.currentUser?.email
        } catch (e: Exception) {
            Log.d("MyTag", "getCurrentUser() ${e.message}")
            null
        }
    }

    override suspend fun changePassword(email: String, newPassword: String): PasswordChangement =
        withContext(coroutineDispatcher) {
            var passwordChangement: PasswordChangement = PasswordChangement.InitialState
            try {
                auth.currentUser?.updatePassword(newPassword)?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        passwordChangement = PasswordChangement.Success("Password changed successfully!")

                    } else {
                        passwordChangement = PasswordChangement.Error("Password didnt change!")
                    }
                }?.await()
            } catch (e: Exception) {
                passwordChangement = PasswordChangement.Error(e.message ?: "Check your internet")
            }
            return@withContext passwordChangement
        }

    override suspend fun resetPassword(): PasswordChangement = withContext(coroutineDispatcher) {
        var resetPassword: PasswordChangement = PasswordChangement.InitialState
        try {
            auth.sendPasswordResetEmail(auth.currentUser?.email.toString()).addOnCompleteListener { task->
                if (task.isSuccessful) {
                    resetPassword = PasswordChangement.Success("Check your email, to reset your password ! ")
                } else {
                    resetPassword = PasswordChangement.Error("Password didn't change!")
                }
            }.await()
        }catch (e: Exception){
            resetPassword = PasswordChangement.Error(e.message ?: "Check your internet")
        }
        return@withContext resetPassword
    }

    //firebase login is asynchronous using await ensuring that it has to wait
    //for  signInWithEmailAndPassword to complete same as using async await
    override suspend fun logIn(email: String, password: String): AuthState =
        withContext(coroutineDispatcher) {
            return@withContext try {
                auth.signInWithEmailAndPassword(email, password).await()
                AuthState.LoggedIn

            } catch (e: Exception) {
                val errorMessage = e.message ?: "Something went wrong"
                AuthState.Error(errorMessage)
            }
        }

    //firebase signup is asynchronous using await ensuring that it has to wait
    //for  createUserWithEmailAndPassword to complete same as using async await
    override suspend fun signUp(email: String, password: String): AccountStatus =
        withContext(coroutineDispatcher) {
            return@withContext try {
                //async to run in concurrently
                val deferred1 = async {
                    auth.createUserWithEmailAndPassword(email, password)
                }
                val deferred2 = async {
                    firebaseWriteDataDataSourceImpl.addEmailToUsers(email)
                }
                //waiting for both to finish concurrently
                awaitAll(deferred1, deferred2)

                AccountStatus.IsCreated("Account Created")

            } catch (e: Exception) {
                AccountStatus.Error(e.message ?: "Check your Internet")
            }
        }

    override suspend fun signOut() = withContext(coroutineDispatcher) {
        auth.signOut()
    }


}

