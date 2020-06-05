package com.example.rxjavaproject.observer

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class WayThreeCompositeDisposable() {
  var log: String = "darshna "
  private var mCompositeDisposable = CompositeDisposable()
  init {
    way1()
  }

  @SuppressLint("CheckResult")
  private fun way1() {
    val foodsObservable = getFoodsObservable()
    val foodsObserver = getFoodsObserver()
    val foodsCapsObservable = getFoodsCapsObserver()

    mCompositeDisposable.add(
        foodsObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .filter { s -> s.toLowerCase().startsWith("c") }
            .subscribeWith(foodsObserver))

    mCompositeDisposable.add(
        foodsObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .filter { s -> s.toLowerCase().startsWith("b") }
            .map { s -> s.toUpperCase() }
            .subscribeWith(foodsCapsObservable)
    )
  }

  private fun getFoodsObservable(): Observable<String> {
    val foodArray = arrayOf("Apple", "Arugula",
        "Bacon", "Banana", "Beef", "Bread", "Butter",
        "Cacao", "Cherry", "Chocolate", "Curry",
        "Danish", "Donut", "Dumpling",
        "Fennel", "Fish", "Fudge")
    return Observable.fromArray(*foodArray)
  }
  private fun getFoodsCapsObserver(): DisposableObserver<String> {
    return object : DisposableObserver<String>() {
      override fun onComplete() {
        Log.i("${log}Caps onComplete", "Done")
      }

      override fun onNext(t: String) {
        Log.i("${log}Caps onNext", t as String)
      }

      override fun onError(e: Throwable) {
        Log.i("${log}Caps onError", e.toString())

      }

    }
  }

  private fun getFoodsObserver(): DisposableObserver<String> {
    return object : DisposableObserver<String>() {
      override fun onComplete() {
        Log.i("${log}onComplete", "Done")
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