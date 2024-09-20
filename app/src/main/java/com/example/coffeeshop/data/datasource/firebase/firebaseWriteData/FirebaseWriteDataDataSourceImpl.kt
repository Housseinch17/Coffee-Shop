package com.example.coffeeshop.data.datasource.firebase.firebaseWriteData

import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class FirebaseWriteDataDataSourceImpl @Inject constructor(
    @Named("usersReference") databaseReference: DatabaseReference,
    @Named("DispatchersIO") private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FirebaseWriteDataDataSource {
    override suspend fun addEmailToUsers(email: String) = withContext(coroutineDispatcher){
        
    }

}