package com.wada811.rxviewmodel.commands

import android.databinding.ObservableBoolean
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor

class RxCommand<T>(canExecuteSource: Observable<Boolean> = Observable.just(true), canExecuteInitially: Boolean = true) : Disposable {
    private val trigger: FlowableProcessor<T> = PublishProcessor.create<T>().toSerialized()
    var canExecute: ObservableBoolean
        get
        private set
    private var canExecuteSourceDisposable: Disposable
    private val disposables = CompositeDisposable()
    
    init {
        canExecute = ObservableBoolean(canExecuteInitially)
        canExecuteSourceDisposable = canExecuteSource.distinctUntilChanged().subscribe({ canExecute.set(it) })
    }
    
    @Suppress("unused") fun toFlowable(): Flowable<T> = trigger
    
    internal fun execute(parameter: T) = trigger.onNext(parameter)
    
    override fun isDisposed(): Boolean = disposables.isDisposed
    override fun dispose() {
        if (isDisposed) {
            trigger.onComplete()
            disposables.dispose()
            canExecute.set(false)
            canExecuteSourceDisposable.dispose()
        }
    }
    
    internal fun bind(disposable: Disposable) {
        disposables.add(disposable)
    }
}

fun <T> Observable<Boolean>.toRxCommand(canExecuteInitially: Boolean = true) = RxCommand<T>(this, canExecuteInitially)
fun <T> Observable<T>.bind(command: RxCommand<T>) {
    command.bind(this.filter { command.canExecute.get() }.subscribe { command.execute(it) })
}