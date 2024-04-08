package ernest.zamelczyk.koleotask.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ernest.zamelczyk.koleotask.api.KoleoHeaderInterceptor
import ernest.zamelczyk.koleotask.api.KoleoService
import ernest.zamelczyk.koleotask.di.qualifiers.BaseUrl
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Converter.Factory
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    @BaseUrl
    fun provideBaseUrl() = "https://koleo.pl/api/v2/main/"

    @Provides
    @Singleton
    fun provideJson() = Json { ignoreUnknownKeys = true }

    @Provides
    @Singleton
    @Named("json")
    fun provideConverterFactory(
        json: Json
    ): Converter.Factory = json.asConverterFactory("application/json".toMediaType())

    @Provides
    @Singleton
    fun provideKoleoHeaderInterceptor() = KoleoHeaderInterceptor()

    @Provides
    @Singleton
    fun provideOkHttpClient(
        headerInterceptor: KoleoHeaderInterceptor
    ): OkHttpClient = OkHttpClient.Builder().addNetworkInterceptor(headerInterceptor).build()

    @Provides
    @Singleton
    fun provideRetrofitClient(
        okHttpClient: OkHttpClient,
        @BaseUrl baseUrl: String,
        @Named("json") converterFactory: Factory
    ) = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(baseUrl)
        .addConverterFactory(converterFactory)
        .build()

    @Provides
    @Singleton
    fun provideKoleoService(
        retrofit: Retrofit
    ) = retrofit.create(KoleoService::class.java)

}