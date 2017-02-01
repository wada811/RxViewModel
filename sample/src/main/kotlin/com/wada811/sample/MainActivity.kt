package com.wada811.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable

class MainActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val disposables = CompositeDisposable()
        disposables.add(MainActivityBindingAdapter(this, R.layout.activity_main, MainViewModel()))
    }
}
