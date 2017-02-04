package com.wada811.rxviewmodel.properties

import android.databinding.ObservableField
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor
import java.util.*

class RxProperty<T>(source: Observable<T>, initialValue: T, mode: EnumSet<Mode> = RxProperty.Mode.DEFAULT_MODE) : ObservableField<T>(initialValue), Disposable {
    
    var value: T = initialValue
        get() = super.get()
        set(value) {
            if (field != value) {
                field = value
                super.set(value)
                processor.onNext(value)
            } else if (!isDistinctUntilChanged) {
                processor.onNext(value)
            }
        }
    
    @Deprecated("For Data-Binding", ReplaceWith("value"), DeprecationLevel.HIDDEN)
    override fun get(): T? = value
    
    @Deprecated("For Data-Binding", ReplaceWith("this.value = value"), DeprecationLevel.HIDDEN)
    override fun set(value: T) {
        this.value = value
    }
    
    private var isDistinctUntilChanged: Boolean
    private var processor: FlowableProcessor<T>
    private var sourceDisposable: Disposable?
    
    init {
        isDistinctUntilChanged = mode.contains(Mode.DISTINCT_UNTIL_CHANGED)
        val isRaiseLatestValueOnSubscribe = mode.contains(Mode.RAISE_LATEST_VALUE_ON_SUBSCRIBE)
        if (isRaiseLatestValueOnSubscribe) {
            processor = BehaviorProcessor.createDefault(initialValue).toSerialized()
        } else {
            processor = PublishProcessor.create<T>().toSerialized()
        }
        value = source.lastElement().blockingGet()
        sourceDisposable = source.subscribe({ value = it }, { processor.onError(it) }, { processor.onComplete() })
    }
    
    fun toObservable(): Observable<T> = processor.toObservable()
    
    override fun isDisposed(): Boolean = sourceDisposable?.isDisposed ?: true
    override fun dispose(): Unit = sourceDisposable?.dispose() ?: Unit
    
    enum class Mode {
        /**
         * If next value is same as current, not set and not notify.
         */
        DISTINCT_UNTIL_CHANGED,
        /**
         * Sends notification on the instance created and subscribed.
         */
        RAISE_LATEST_VALUE_ON_SUBSCRIBE;
        
        internal companion object {
            internal val DEFAULT_MODE = EnumSet.of(DISTINCT_UNTIL_CHANGED, RAISE_LATEST_VALUE_ON_SUBSCRIBE)
        }
    }
    
}