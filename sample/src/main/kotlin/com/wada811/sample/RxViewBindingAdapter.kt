package com.wada811.sample

import android.databinding.BindingAdapter
import android.view.View
import com.jakewharton.rxbinding2.view.clicks
import com.wada811.rxviewmodel.RxCommand
import com.wada811.rxviewmodel.bind

@BindingAdapter("android:click")
fun View.setOnClick(command: RxCommand<Unit>) = this.clicks().map { Unit }.bind(command)
