package com.wada811.sample.view.binding

import android.databinding.BindingAdapter
import android.view.View
import com.jakewharton.rxbinding2.view.clicks
import com.wada811.rxviewmodel.commands.RxCommand

@BindingAdapter("click")
fun View.setRxCommandOnClick(command: RxCommand<Unit>) = command.bindTrigger(this.clicks().map { Unit })

