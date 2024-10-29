package com.lecturfy.di

import android.app.Application
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.lecturfy.api.*
import com.lecturfy.api.LoginService
import com.lecturfy.api.UserprofileService
import com.lecturfy.api.UsersettingsService
import com.lecturfy.api.TranscriptionsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import android.content.Context
import android.content.SharedPreferences
import javax.inject.Singleton
import com.lecturfy.components.google.GoogleSignInHelper

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    



    @Provides
    @Singleton
    @RetrofitLogin
    fun provideRetrofitLogin(gson: Gson): Retrofit {
        return Retrofit.Builder()
        .baseUrl("https://api.lecturfy.com/auth/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    }

    @Provides
    @Singleton
    fun provideLoginService(@RetrofitLogin retrofit: Retrofit): LoginService {
        return retrofit.create(LoginService::class.java)
    }
    @Provides
    @Singleton
    @RetrofitUserProfile
    fun provideRetrofitUserProfile(gson: Gson): Retrofit {
        return Retrofit.Builder()
        .baseUrl("https://api.lecturfy.com/users/me/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    }

    @Provides
    @Singleton
    fun provideUserprofileService(@RetrofitUserProfile retrofit: Retrofit): UserprofileService {
        return retrofit.create(UserprofileService::class.java)
    }
    @Provides
    @Singleton
    @RetrofitUserSettings
    fun provideRetrofitUserSettings(gson: Gson): Retrofit {
        return Retrofit.Builder()
        .baseUrl("https://api.lecturfy.com/users/me/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    }

    @Provides
    @Singleton
    fun provideUsersettingsService(@RetrofitUserSettings retrofit: Retrofit): UsersettingsService {
        return retrofit.create(UsersettingsService::class.java)
    }
    @Provides
    @Singleton
    @RetrofitTranscriptions
    fun provideRetrofitTranscriptions(gson: Gson): Retrofit {
        return Retrofit.Builder()
        .baseUrl("https://api.lecturfy.com/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    }

    @Provides
    @Singleton
    fun provideTranscriptionsService(@RetrofitTranscriptions retrofit: Retrofit): TranscriptionsService {
        return retrofit.create(TranscriptionsService::class.java)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideGoogleSignInHelper(@ApplicationContext context: Context): GoogleSignInHelper {
        return GoogleSignInHelper(context)
    }
}
