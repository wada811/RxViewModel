package com.wada811.rxviewmodel.properties

import android.databinding.ObservableField
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.util.*

open class ReadOnlyRxProperty<T>(source: Observable<T>, initialValue: T, mode: EnumSet<RxPropertyMode> = RxPropertyMode.DEFAULT) : ObservableField<T>(initialValue), Disposable {
    open var value: T = initialValue
        get() = super.get()
        protected set(value) {
            if (field != value) {
                field = value
                super.set(value)
                subject.onNext(value)
            } else if (!isDistinctUntilChanged) {
                subject.onNext(value)
            }
        }
    
    @Deprecated("For Data-Binding", ReplaceWith("value"), DeprecationLevel.HIDDEN)
    override fun get(): T = value
    
    @Deprecated("For Data-Binding", ReplaceWith("this.value = value"), DeprecationLevel.HIDDEN)
    override fun set(value: T) {
        throw UnsupportedOperationException("DO NOT USE ReadOnlyRxProperty for two-way binding, Please use RxProperty instead.")
    }
    
    protected val isDistinctUntilChanged: Boolean = mode.contains(RxPropertyMode.DISTINCT_UNTIL_CHANGED)
    protected val subject: Subject<T> =
        if (mode.contains(RxPropertyMode.RAISE_LATEST_VALUE_ON_SUBSCRIBE)) {
            BehaviorSubject.createDefault(initialValue).toSerialized()
        } else {
            PublishSubject.create<T>().toSerialized()
        }
    private val sourceDisposable: Disposable = source.subscribe({ value = it }, { subject.onError(it) }, { subject.onComplete() })
    
    fun toObservable(): Observable<T> = subject
    
    override fun isDisposed(): Boolean = sourceDisposable.isDisposed
    override fun dispose(): Unit = sourceDisposable.dispose()
}

fun <T> Observable<T>.toReadOnlyRxProperty(initialValue: T, mode: EnumSet<RxPropertyMode> = RxPropertyMode.DEFAULT) = ReadOnlyRxProperty(this, initialValue, mode)