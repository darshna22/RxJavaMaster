package com.example.rxjavaproject.observer

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class CustomData() {
  var log: String = "darshna "

  init {
    way1()
  }

  @SuppressLint("CheckResult")
  private fun way1() {
    val cityStatesObservable = getCityStatesObservable()
    val foodsObserver = getFoodsObserver()
    cityStatesObservable
        .subscribeOn(Schedulers.io())
        .map { t: State ->
          t.name = t.name.toUpperCase()
          t.name1 = t.name1.toLowerCase()
          t
        }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(foodsObserver)
  }

  private fun getCityStatesObservable(): Observable<State> {
    val stateList = ArrayList<State>()
    stateList.add(State(1, "California","California"))
    stateList.add(State(2, "New York","New York"))
    stateList.add(State(3, "Colorado","Colorado"))
    stateList.add(State(4, "Texas","Texas"))
    stateList.add(State(5, "Alaska","Alaska"))
    return Observable.fromIterable(stateList)
  }

  private fun getFoodsObserver(): Observer<State> {
    return object : Observer<State> {
      override fun onComplete() {
        Log.i("${log}onComplete", "Done")
      }

      override fun onSubscribe(d: Disposable) {
        Log.i("${log}onSubscribe", d.toString())
      }

      override fun onNext(t: State) {
        Log.i("${log}onNext", t.name+" "+t.id+" "+t.name1)
      }

      override fun onError(e: Throwable) {
        Log.i("${log}onError", e.toString())

      }

    }
  }

  data class State(var id: Int = 0, var name: String = "",var name1: String = "")
}