package com.example.myapplication.observer

import com.example.myapplication.retrofit.ApiInterfaceRxJava
import com.example.myapplication.retrofit.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody

class ApiHitByRxJava {
  var myCompositeDisposable = CompositeDisposable()
  init {
    loadData()
  }

  private fun loadData() {
    //Build a Retrofit object//

    /*Create handle for the RetrofitInstance interface*/
    val api = RetrofitClient.retrofitInstance?.create(ApiInterfaceRxJava::class.java)

    myCompositeDisposable.add(
        api!!.getAllHerosData("https://simplifiedcoding.net/demos/marvel/")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(this::handleResponse)
    )

  }

  private fun handleResponse(responseBody: ResponseBody) {
    println("darshna Api response is: ${responseBody.string()}")

  }
}