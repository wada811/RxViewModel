package com.wada811.sample

import android.util.Log
import com.wada811.rxviewmodel.RxCommand
import com.wada811.rxviewmodel.RxProperty
import com.wada811.rxviewmodel.RxViewModel
import io.reactivex.Observable

class MainViewModel : RxViewModel() {
    val name = RxProperty<String>(Observable.fromArray("world"), "").asManaged()
    val helloCommand = RxCommand<Unit>(name.toObservable().map { it != "" }).asManaged()
    val helloName = RxProperty<String>(Observable.fromArray(""), "").asManaged()
    
    init {
        helloCommand.toFlowable()
            .subscribe({
                helloName.value = "Hello, ${name.value}!"
                Log.d("RxViewModel", "onNext")
            }, {
                Log.e("RxViewModel", "onError")
            }, {
                Log.i("RxViewModel", "onComplete")
            })
    }
}