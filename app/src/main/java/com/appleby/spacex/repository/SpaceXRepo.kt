package com.appleby.spacex.repository

import com.appleby.spacex.networkmodel.Launch
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

object SpaceXRepo {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.spacexdata.com/v4/")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service by lazy {
        retrofit.create(
            SpaceXApiService::class.java)
    }

    interface SpaceXApiService {
        @GET("launches/past")
        fun getPastLaunches() : Observable<Response<List<Launch>>>
    }

}