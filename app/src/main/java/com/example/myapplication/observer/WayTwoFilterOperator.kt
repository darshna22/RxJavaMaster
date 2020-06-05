package com.example.rxjavaproject.observer

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class WayTwoFilterOperator() {
  var log: String = "darshna "

  init {
    way1()
  }

  @SuppressLint("CheckResult")
  private fun way1() {
    val foodsObservable = getFoodsObservable()
    val foodsObserver = getFoodsObserver()
    foodsObservable
        .subscribeOn(Schedulers.io())
//        .filter { s -> s == true }
        .filter { s -> s.toString().toLowerCase().startsWith("c") }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(foodsObserver)
  }

  private fun getFoodsObservable(): Observable<Any> {
    return Observable.just(
        true,
        1.1,
        "Bacon",
        1,
        'A',
        "Fish",
        "Cacao",
        "Cherry",
        "Chocolate",
        "Curry"
    )
  }

  private fun getFoodsObserver(): Observer<Any> {
    return object : Observer<Any> {
      override fun onComplete() {
        Log.i("${log}onComplete", "Done")
      }

      override fun onSubscribe(d: Disposable) {
        Log.i("${log}onSubscribe", d.toString())
      }

      override fun onNext(t: Any) {
        Log.i("${log}onNext", t as String)
      }

      override fun onError(e: Throwable) {
        Log.i("${log}onError", e.toString())

      }

    }
  }
}