package momoi.mod.qqpro.lib

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import momoi.mod.qqpro.asGroup

fun <T : RecyclerView> T.linearLayout() = apply {
    layoutManager = LinearLayoutManager(context)
}

fun <T : RecyclerView, E> T.content(
    state: MutableState<List<E>>,
    factory: Context.() -> View,
    update: ViewGroup.(E) -> Unit
) = apply {
    adapter = SimpleAdapter(state, factory, update)
}

class SimpleHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

@SuppressLint("NotifyDataSetChanged")
class SimpleAdapter<E>(
    val state: MutableState<List<E>>,
    val factory: Context.() -> View,
    val update: ViewGroup.(E) -> Unit
) : RecyclerView.Adapter<SimpleHolder>() {
    init {
        state.observe {
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SimpleHolder(factory(parent.context))

    override fun getItemCount(): Int {
        return state.value.size
    }

    override fun onBindViewHolder(holder: SimpleHolder, position: Int) {
        update(holder.itemView.asGroup(), state.value[position])
    }

}