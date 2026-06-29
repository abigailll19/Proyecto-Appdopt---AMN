package com.example.appdopt.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan_tennert.supabase.SupabaseClient
import io.github.jan_tennert.supabase.createSupabaseClient
import io.github.jan_tennert.supabase.gotrue.Auth
import io.github.jan_tennert.supabase.postgrest.Postgrest
import io.github.jan_tennert.supabase.realtime.Realtime
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SupabaseModule {

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = "https://TU_PROYECTO.supabase.co",
            supabaseKey = "TU_ANON_KEY"
        ) {
            install(Postgrest)
            install(Auth)
            install(Realtime)
        }
    }
}
