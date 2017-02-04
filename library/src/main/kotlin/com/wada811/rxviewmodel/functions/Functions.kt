package com.wada811.rxviewmodel.functions

interface Function
interface Func0<out R> : Function {
    @Throws(Exception::class)
    fun invoke(): R
}

interface Func1<in T1, out R> : Function {
    @Throws(Exception::class)
    fun invoke(activity: T1): R
}

interface Func2<in T1, in T2, out R> : Function {
    @Throws(Exception::class)
    fun invoke(t1: T1, t2: T2): R
}

interface Func3<in T1, in T2, in T3, out R> : Function {
    @Throws(Exception::class)
    fun invoke(t1: T1, t2: T2, t3: T3): R
}

interface Func4<in T1, in T2, in T3, in T4, out R> : Function {
    @Throws(Exception::class)
    fun invoke(t1: T1, t2: T2, t3: T3, t4: T4): R
}

interface Action : Function
interface Action0 : Action, Func0<Unit>
interface Action1<in T1> : Action, Func1<T1, Unit>
interface Action2<in T1, in T2> : Action, Func2<T1, T2, Unit>
interface Action3<in T1, in T2, in T3> : Action, Func3<T1, T2, T3, Unit>
interface Action4<in T1, in T2, in T3, in T4> : Action, Func4<T1, T2, T3, T4, Unit>