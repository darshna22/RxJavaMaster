package com.example.rxjavaproject.observer

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class WayOne() {
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
        .observeOn(AndroidSchedulers.mainThread()).subscribeBy(onNext = {
          Log.i("${log}onNext", it.toString())
        },
            onError = {
              Log.i("${log}onError", it.toString())
            },
            onComplete = {
              Log.i("${log}onNext", "DONE!!")
            }
        )
//        .subscribe(foodsObserver)
  }

  private fun getFoodsObservable(): Observable<String> {
    return Observable.just("Apple", "Bacon", "Cacao", "Dumpling", "Fish")
  }

  private fun getFoodsObserver(): Observer<String> {
    return object : Observer<String> {
      override fun onComplete() {
        Log.i("${log}onComplete", "Done")
      }

      override fun onSubscribe(d: Disposable) {
        Log.i("${log}onSubscribe", d.toString())
      }

      override fun onNext(t: String) {
        Log.i("${log}onNext", t)
      }

      override fun onError(e: Throwable) {
        Log.i("${log}onError", e.toString())

      }

    }
  }
}