package momoi.mod.qqpro.lib

class MutableState<T>(initialValue: T) : State<T>(initialValue) {
    fun update(new: T) {
        value = new
        observers.forEach {
            it.get()?.invoke(value)
        }
    }
}