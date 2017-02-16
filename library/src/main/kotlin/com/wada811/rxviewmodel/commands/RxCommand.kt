package com.wada811.rxviewmodel.commands

import android.databinding.ObservableBoolean
import com.wada811.rxviewmodel.extensions.addTo
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

class RxCommand<T>(canExecuteSource: Observable<Boolean> = Observable.just(true), canExecuteInitially: Boolean = true) : Disposable {
    private val subject: Subject<T> = PublishSubject.create<T>().toSerialized()
    val canExecute: ObservableBoolean = ObservableBoolean(canExecuteInitially)
    private val disposables = CompositeDisposable()
    
    init {
        canExecuteSource
            .distinctUntilChanged()
            .subscribe { canExecute.set(it) }
            .addTo(disposables)
    }
    
    @Suppress("unused") fun toObservable(): Observable<T> = subject
    
    internal fun execute(parameter: T) = subject.onNext(parameter)
    
    override fun isDisposed(): Boolean = disposables.isDisposed
    override fun dispose() {
        if (!isDisposed) {
            subject.onComplete()
            disposables.dispose()
            canExecute.set(false)
        }
    }
    
    fun bindTrigger(observable: Observable<T>) {
        observable
            .filter { canExecute.get() }
            .doOnTerminate { dispose() }
            .subscribe({ execute(it) }, { subject.onError(it) }, { subject.onComplete() })
            .addTo(disposables)
    }
}

fun <T> Observable<Boolean>.toRxCommand(canExecuteInitially: Boolean = true) = RxCommand<T>(this, canExecuteInitially)
