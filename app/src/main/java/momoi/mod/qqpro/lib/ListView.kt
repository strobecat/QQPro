package momoi.mod.qqpro.lib

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import momoi.mod.qqpro.asGroup

fun <T : ListView, E> T.content(
    state: MutableState<List<E>>,
    factory: Context.() -> View,
    update: ViewGroup.(E) -> Unit
) = apply {
    adapter = SimpleListAdapter(state, factory, update)
}

@SuppressLint("NotifyDataSetChanged")
class SimpleListAdapter<E>(
    val state: MutableState<List<E>>,
    val factory: Context.() -> View,
    val update: ViewGroup.(E) -> Unit
) : BaseAdapter() {
    init {
        state.observe {
            notifyDataSetChanged()
        }
    }

    override fun getCount(): Int {
        return state.value.size
    }

    override fun getItem(position: Int): E {
        return state.value[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: factory(parent.context).also {
            it.tag = SimpleViewHolder(it)
        }

        val holder = view.tag as SimpleViewHolder
        update(holder.itemView.asGroup(), getItem(position))
        return view
    }
}

class SimpleViewHolder(val itemView: View)