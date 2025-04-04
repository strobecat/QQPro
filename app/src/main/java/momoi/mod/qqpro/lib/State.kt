package momoi.mod.qqpro.lib

import java.lang.ref.WeakReference

open class State<T>(initialValue: T) {
    var value: T = initialValue
        protected set
    protected val observers = mutableListOf<WeakReference<(T)->Unit>>()
    fun observe(block: (T)->Unit) {
        block(value)
        observers.add(WeakReference(block))
    }
}