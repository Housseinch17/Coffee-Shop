package com.example.coffeeshop.data.di

import android.content.Context
import com.example.coffeeshop.data.datasource.firebase.FirebaseRepositoryImpl
import com.example.coffeeshop.data.datasource.firebase.firebaseAuthentication.FirebaseAuthenticationDataSource
import com.example.coffeeshop.data.datasource.firebase.firebaseAuthentication.FirebaseAuthenticationDataSourceImpl
import com.example.coffeeshop.data.datasource.firebase.firebaseReadData.FirebaseReadDataDataSource
import com.example.coffeeshop.data.datasource.firebase.firebaseReadData.FirebaseReadDataDataSourceImpl
import com.example.coffeeshop.domain.repository.FirebaseRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun provideFirebaseAuthenticationDataSource(
        firebaseAuth: FirebaseAuth,
    ): FirebaseAuthenticationDataSource {
        return FirebaseAuthenticationDataSourceImpl(firebaseAuth)
    }

    @Provides
    @Singleton
    @Named("menuReference")
    fun provideDatabaseReference(): DatabaseReference {
        // Return the reference to your specific path or the default database reference
        return FirebaseDatabase.getInstance().getReference("menu")
    }

    @Provides
    @Singleton
    fun provideFirebaseReadDataDataSource(databaseReference: DatabaseReference,@ApplicationContext  context: Context): FirebaseReadDataDataSource {
        return FirebaseReadDataDataSourceImpl(databaseReference,context)
    }

    @Provides
    @Singleton
    fun provideFirebaseRepository(
        firebaseRemoteDataSourceImpl: FirebaseAuthenticationDataSourceImpl,
        firebaseReadDataDataSourceImpl: FirebaseReadDataDataSourceImpl
    ): FirebaseRepository {
        return FirebaseRepositoryImpl(firebaseRemoteDataSourceImpl, firebaseReadDataDataSourceImpl)
    }
}