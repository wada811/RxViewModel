package com.wada811.rxviewmodel

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class RxViewModel : Disposable {
    private val disposables = CompositeDisposable()
    override fun isDisposed(): Boolean = disposables.isDisposed
    override fun dispose() {
        if (!isDisposed) {
            disposables.dispose()
        }
    }

    protected fun <T> RxProperty<T>.asManaged(): RxProperty<T> {
        disposables.add(this)
        return this
    }
    protected fun <T> RxCommand<T>.asManaged(): RxCommand<T> {
        disposables.add(this)
        return this
    }
}