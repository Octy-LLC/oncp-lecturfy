package com.lecturfy.di

import javax.inject.Qualifier


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RetrofitLogin

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RetrofitUserProfile

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RetrofitUserSettings

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RetrofitTranscriptions

