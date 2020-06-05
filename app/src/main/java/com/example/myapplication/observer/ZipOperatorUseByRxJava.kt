package com.example.myapplication.observer

import android.annotation.SuppressLint
import com.example.myapplication.retrofit.ApiInterfaceRxJava
import com.example.myapplication.retrofit.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import okhttp3.ResponseBody
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

/*
* This class responsible to execute two or more api's sequencely
* */
class ZipOperatorUseByRxJava {

  init {
    loadData()
  }

  @SuppressLint("CheckResult")
  private fun loadData() {
    //Build a Retrofit object//
    /*Create handle for the RetrofitInstance interface*/
    val api = RetrofitClient.retrofitInstance?.create(ApiInterfaceRxJava::class.java)

    Observable.zip(
        api!!.getAllHerosData("https://simplifiedcoding.net/demos/marvel/"),
//        api.getUpComingEvents("https://api.coingecko.com/api/v3/events"),
        api.getAllAirlineData("https://api.androidhive.info/json/airline-tickets.php"),
        BiFunction<ResponseBody, ResponseBody,ResponseBody> { response1,response2->
          // here we get both the results at a time.
          return@BiFunction handleResponse3(responseBody1 = response1,responseBody2 = response2)
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe()

  }

  private fun handleResponse3(responseBody1: ResponseBody,responseBody2: ResponseBody): ResponseBody {
    println("darshna Api response is3: ${responseBody1.string()} \n \${responseBod2.string()")
    return responseBody1
  }

  private fun handleResponse(responseBody: ResponseBody) {
    println("darshna Api response is: ${responseBody.string()}")

  }

  private fun handleResponse1(responseBody: ResponseBody) {
    println("darshna Api response is1: ${responseBody.string()}")

  }
}