package com.wada811.rxviewmodel.binding.view

import android.databinding.BindingAdapter
import android.view.View
import com.jakewharton.rxbinding2.view.clicks
import com.wada811.rxviewmodel.commands.RxCommand
import com.wada811.rxviewmodel.commands.bind

@BindingAdapter("click")
fun View.setOnClick(command: RxCommand<Unit>) = this.clicks().map { Unit }.bind(command)
