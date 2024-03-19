package com.puneetkaur.movies.network.model.api

import com.puneetkaur.movies.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Helper class for creating and obtaining a Retrofit instance for making API calls.
 */
class RetrofitHelper {

    companion object{

        fun getInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        /**
         * Lazily initialized instance of [MoviesApiService] obtained from the Retrofit instance.
         */
        val api by lazy {
            getInstance().create(MoviesApiService::class.java)
        }

    }
}