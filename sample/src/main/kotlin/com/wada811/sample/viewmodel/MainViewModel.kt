package com.wada811.sample.viewmodel

import android.util.Log
import com.wada811.rxviewmodel.RxViewModel
import com.wada811.rxviewmodel.commands.toRxCommand
import com.wada811.rxviewmodel.messages.RxMessenger
import com.wada811.rxviewmodel.properties.RxProperty
import com.wada811.rxviewmodel.properties.toReadOnlyRxProperty
import com.wada811.sample.view.activity.MainActivity
import io.reactivex.subjects.BehaviorSubject

class MainViewModel : RxViewModel() {
    val observable: BehaviorSubject<String> = BehaviorSubject.createDefault("world")
    val name = RxProperty<String>(observable, observable.value).asManaged()
    val helloCommand = name.toObservable().map { it != "" }.toRxCommand<Unit>().asManaged()
    val helloName = name.toObservable().map { "Hello, $it!" }.toReadOnlyRxProperty("Hello, ${name.value}!").asManaged()
    
    init {
        helloCommand.toObservable()
            .subscribe({
                RxMessenger.send(MainActivity.ToastAction(helloName.value))
            }, {
                Log.e("RxViewModel", "onError")
            }, {
                Log.i("RxViewModel", "onComplete")
            }).asManaged()
    }
}