package com.wada811.sample

import android.app.Activity
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import com.wada811.rxviewmodel.RxViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class ActivityBindingAdapter<TBinding, TViewModel>(activity: Activity, layoutId: Int, viewModel: TViewModel) : Disposable
where TBinding : ViewDataBinding, TViewModel : RxViewModel {
    private val binding: TBinding = DataBindingUtil.setContentView<TBinding>(activity, layoutId)
    private val disposables = CompositeDisposable()

    init {
        disposables.add(viewModel)
        binding.setVariable(BR.viewModel, viewModel)
    }

    override fun isDisposed(): Boolean = disposables.isDisposed
    override fun dispose() {
        if (!isDisposed) {
            disposables.dispose()
            binding.unbind()
        }
    }
}
