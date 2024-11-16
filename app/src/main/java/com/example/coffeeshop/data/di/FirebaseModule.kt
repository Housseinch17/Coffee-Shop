package com.example.coffeeshop.data.di

import android.content.Context
import com.example.coffeeshop.data.datasource.firebase.FirebaseRepositoryImpl
import com.example.coffeeshop.data.datasource.firebase.firebaseAuthentication.FirebaseAuthenticationDataSource
import com.example.coffeeshop.data.datasource.firebase.firebaseAuthentication.FirebaseAuthenticationDataSourceImpl
import com.example.coffeeshop.data.datasource.firebase.firebaseReadData.FirebaseReadDataDataSource
import com.example.coffeeshop.data.datasource.firebase.firebaseReadData.FirebaseReadDataDataSourceImpl
import com.example.coffeeshop.data.datasource.firebase.firebaseWriteData.FirebaseWriteDataDataSource
import com.example.coffeeshop.data.datasource.firebase.firebaseWriteData.FirebaseWriteDataDataSourceImpl
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
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun provideFirebaseAuthenticationDataSource(
        firebaseAuth: FirebaseAuth,
        firebaseWriteDataDataSourceImpl: FirebaseWriteDataDataSourceImpl,
        @Named("DispatchersIO") coroutineDispatcher: CoroutineDispatcher
    ): FirebaseAuthenticationDataSource {
        return FirebaseAuthenticationDataSourceImpl(firebaseAuth, firebaseWriteDataDataSourceImpl, coroutineDispatcher)
    }

    @Provides
    @Singleton
    @Named("menuReference")
    fun provideDatabaseMenuReference(): DatabaseReference {
        // Return the reference to your specific path or the default database reference
        return FirebaseDatabase.getInstance().getReference("menu")
    }

    @Provides
    @Singleton
    fun provideFirebaseReadDataDataSource(
        @Named("usersReference") databaseReference: DatabaseReference,
        @ApplicationContext context: Context,
        @Named("DispatchersIO") coroutineDispatcher: CoroutineDispatcher
    ): FirebaseReadDataDataSource {
        return FirebaseReadDataDataSourceImpl(databaseReference, context, coroutineDispatcher)
    }

    @Provides
    @Singleton
    @Named("usersReference")
    fun provideDatabaseUsersReference(): DatabaseReference {
        // Return the reference to your specific path or the default database reference
        return FirebaseDatabase.getInstance().getReference("users")
    }

    @Provides
    @Singleton
    fun provideFirebaseWriteDataDataSource(
        @Named("usersReference") databaseReference: DatabaseReference,
        @Named("DispatchersIO") coroutineDispatcher: CoroutineDispatcher
    ): FirebaseWriteDataDataSource {
        return FirebaseWriteDataDataSourceImpl(databaseReference,coroutineDispatcher)
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