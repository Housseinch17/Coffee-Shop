package com.example.coffeeshop.data.datasource.firebase.firebaseReadData

import android.content.Context
import com.example.coffeeshop.data.model.menu.Menu
import com.example.coffeeshop.ui.screen.homepage.FirebaseCategoryResponse
import com.example.coffeeshop.ui.screen.homepage.FirebaseOffersResponse
import com.example.coffeeshop.ui.util.isInternetAvailable
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.resume

class FirebaseReadDataDataSourceImpl @Inject constructor(
    @Named("menuReference") private val databaseReference: DatabaseReference,
    @ApplicationContext private val context: Context,
    @Named("DispatchersIO")
    private val coroutineDispatcher: CoroutineDispatcher,
) : FirebaseReadDataDataSource {

    override suspend fun readCategoryDataFromFirebase(): FirebaseCategoryResponse =
        withContext(coroutineDispatcher) {
            suspendCancellableCoroutine { continuation ->
                val hasInternet = context.isInternetAvailable()
                if (hasInternet) {
                    try {
                        databaseReference.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val categoryHashMap =
                                    snapshot.getValue(Menu::class.java)?.category?.toHashMap()
                                if (categoryHashMap != null) {
                                    if (continuation.isActive) {
                                        continuation.resume(
                                            FirebaseCategoryResponse.Success(
                                                categoryHashMap
                                            )
                                        )
                                    }
                                } else {
                                    continuation.resume(FirebaseCategoryResponse.Error("Data is null"))
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                val errorMessage = "Failed to read menu data$error"
                                if (continuation.isActive) {
                                    continuation.resume(FirebaseCategoryResponse.Error(errorMessage))
                                }
                            }
                        })
                    } catch (e: Exception) {
                        val errorMessage = e.message ?: "Something went wrong"
                        if (continuation.isActive) {
                            continuation.resume(FirebaseCategoryResponse.Error(errorMessage))
                        }
                    }
                } else {
                    if (continuation.isActive) {
                        continuation.resume(FirebaseCategoryResponse.Error("No internet connection!"))
                    }
                }
            }
        }

    override suspend fun readOffersDataFromFirebase(): FirebaseOffersResponse =
        withContext(coroutineDispatcher) {
            suspendCancellableCoroutine { continuation ->
                val hasInternet = context.isInternetAvailable()
                if (hasInternet) {
                    try {
                        databaseReference.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val offers =
                                    snapshot.getValue(Menu::class.java)?.offers
                                if (offers != null) {
                                    if (continuation.isActive) {
                                        continuation.resume(
                                            FirebaseOffersResponse.Success(
                                                offers
                                            )
                                        )
                                    }
                                } else {
                                    continuation.resume(FirebaseOffersResponse.Error("Data is null"))
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                val errorMessage = "Failed to read menu data$error"
                                if (continuation.isActive) {
                                    continuation.resume(FirebaseOffersResponse.Error(errorMessage))
                                }
                            }
                        })
                    } catch (e: Exception) {
                        val errorMessage = e.message ?: "Something went wrong"
                        if (continuation.isActive) {
                            continuation.resume(FirebaseOffersResponse.Error(errorMessage))
                        }
                    }
                } else {
                    if (continuation.isActive) {
                        continuation.resume(FirebaseOffersResponse.Error("No internet connection!"))
                    }
                }
            }
        }
}