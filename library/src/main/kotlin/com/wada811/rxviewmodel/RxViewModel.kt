package com.wada811.rxviewmodel

import com.wada811.rxviewmodel.commands.RxCommand
import com.wada811.rxviewmodel.properties.ReadOnlyRxProperty
import com.wada811.rxviewmodel.properties.RxProperty
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class RxViewModel : Disposable {
    private val disposables = CompositeDisposable()
    override fun isDisposed(): Boolean = disposables.isDisposed
    override fun dispose(): Unit = disposables.dispose()
    
    protected fun <T> RxProperty<T>.asManaged(): RxProperty<T> {
        disposables.add(this)
        return this
    }
    
    protected fun <T> ReadOnlyRxProperty<T>.asManaged(): ReadOnlyRxProperty<T> {
        disposables.add(this)
        return this
    }
    
    protected fun <T> RxCommand<T>.asManaged(): RxCommand<T> {
        disposables.add(this)
        return this
    }
    
    protected fun <T> T.asManaged() where T : Disposable {
        disposables.add(this)
    }
}