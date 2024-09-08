package com.example.coffeeshop.data.datasource.firebase.firebaseReadData

import com.example.coffeeshop.data.model.menu.Menu
import com.example.coffeeshop.ui.screen.homepage.FirebaseResponse
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.resume

class FirebaseReadDataDataSourceImpl @Inject constructor(
    @Named("menuReference")
    private val databaseReference: DatabaseReference,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
): FirebaseReadDataDataSource {

    override suspend fun readDataFromFirebase(): FirebaseResponse = withContext(coroutineDispatcher){
        suspendCancellableCoroutine{ continuation->
            try{
                databaseReference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val menuData = snapshot.getValue(Menu::class.java)
                        if (menuData != null) {
                            if (continuation.isActive) {
                                continuation.resume(FirebaseResponse.Success(menuData))
                            }
                            else {
                                continuation.resume(FirebaseResponse.Error("Data is null"))
                            }
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        val errorMessage = "Failed to read menu data$error"
                        if(continuation.isActive){
                            continuation.resume(FirebaseResponse.Error(errorMessage))
                        }
                    }
                })
            }catch (e:Exception){
                val errorMessage = e.message ?: "Something went wrong"
                if(continuation.isActive){
                    continuation.resume(FirebaseResponse.Error(errorMessage))
                }
            }
        }
    }
}