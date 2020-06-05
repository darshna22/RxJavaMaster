package com.example.myapplication.retrofit

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterfaceRxJava {
  @GET("{fullUrl}")
  fun getAllHeros(@Path(value = "fullUrl", encoded = true) fullUrl: String): Call<ResponseBody>

  @GET("{fullUrl}")
  fun getUpComingEvents(
    @Path(value = "fullUrl", encoded = true) fullUrl: String
  ): Observable<ResponseBody>

  @GET("{fullUrl}")
  fun getAllHerosData(@Path(value = "fullUrl", encoded = true) fullUrl: String): Observable<ResponseBody>

//  "https://api.androidhive.info/json/airline-tickets.php"
//  https://api.androidhive.info/json/airline-tickets-price.php
  @GET("{fullUrl}")
  fun getAllAirlineData(@Path(value = "fullUrl", encoded = true) fullUrl: String): Observable<ResponseBody>


}