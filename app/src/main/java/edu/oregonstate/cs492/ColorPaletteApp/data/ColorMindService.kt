package edu.oregonstate.cs492.ColorPaletteApp.data

import com.squareup.moshi.Moshi
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.GET

interface ColorMindService {
    @POST("api/")
    suspend fun loadColorPalette(
        @Body request: ColorMindRequest
    ) : Response<ColorSet>

    @GET("list/")
    suspend fun loadModelList()
    : Response<ColorMindModel>

    companion object {
        private const val BASE_URL = "http://colormind.io/"

        fun create() : ColorMindService {
            val moshi = Moshi.Builder()
                .add(ColorMindColorJsonAdapter())
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(ColorMindService::class.java)
        }
    }
}