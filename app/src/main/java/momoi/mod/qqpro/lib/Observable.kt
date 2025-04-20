package momoi.mod.qqpro.lib

import java.util.Vector

class Observable<T>(value: T) {
    var value = value
        private set
    private val observerList = Vector<Observer>()
    inner class Observer(var block: (Observer.(T) -> Unit)?) {
        fun cancel() {
            block = null
        }
    }
    fun observe(block: Observer.(T) -> Unit) {
        observerList.removeAll { it.block == null }
        observerList.add(Observer(block))
    }
    inline fun observeOnce(crossinline block: Observer.(T)->Unit) {
        observe {
            block(it)
            cancel()
        }
    }
    fun update(value: T) {
        this.value = value
        observerList.forEach {
            it.block?.invoke(it, value)
        }
    }
}