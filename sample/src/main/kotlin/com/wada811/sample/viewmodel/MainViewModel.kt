package com.wada811.sample.viewmodel

import android.util.Log
import com.wada811.rxviewmodel.RxViewModel
import com.wada811.rxviewmodel.commands.RxCommand
import com.wada811.rxviewmodel.messages.RxMessenger
import com.wada811.rxviewmodel.properties.RxProperty
import com.wada811.sample.view.activity.MainActivity
import io.reactivex.Observable

class MainViewModel : RxViewModel() {
    val name = RxProperty<String>(Observable.just("world"), "world").asManaged()
    val helloCommand = RxCommand<Unit>(name.toObservable().map { it != "" }).asManaged()
    val helloName = RxProperty<String>(Observable.just(""), "").asManaged()
    
    init {
        helloCommand.toFlowable()
            .subscribe({
                Log.d("RxViewModel", "onNext")
                helloName.value = "Hello, ${name.value}!"
                RxMessenger.send(MainActivity.ToastAction(helloName.value))
            }, {
                Log.e("RxViewModel", "onError")
            }, {
                Log.i("RxViewModel", "onComplete")
            }).asManaged()
    }
}