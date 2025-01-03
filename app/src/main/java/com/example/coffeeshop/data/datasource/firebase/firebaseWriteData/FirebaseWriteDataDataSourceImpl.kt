package com.example.coffeeshop.data.datasource.firebase.firebaseWriteData

import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseWriteDataDataSourceImpl @Inject constructor(
    @Named("usersReference") private val databaseReference: DatabaseReference,
    @Named("DispatchersIO") private val coroutineDispatcher: CoroutineDispatcher,
) : FirebaseWriteDataDataSource {
    override suspend fun addEmailToUsers(email: String): Boolean = withContext(coroutineDispatcher){
        suspendCoroutine {  continuation ->
            //cant use . as child in firebase so had to rename it with !
            val safeEmail = email.replace(".", "!")

            databaseReference.child(safeEmail).setValue("").addOnCompleteListener { task->
                continuation.resume(task.isSuccessful)
            }
        }
    }
}