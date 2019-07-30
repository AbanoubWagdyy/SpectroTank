package com.spectrotank.ui.sync.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.spectrotank.DataLayer.db.Item
import com.com.spectrotankapplication.R

class SyncAdapter(
    private var items: List<Item>
) : RecyclerView.Adapter<SyncAdapter.ViewHolder>() {

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.sync_list_view_item, parent, false)
        context = view.context
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = items[position]
        holder.tvName.text = item.name
        holder.tvImeNumber.text = item.IMEI
        holder.cbSync.isChecked = item.isSynced

        holder.cbSync.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                if (p1) {
                    for (i in 0 until items.size) {
                        items[i].isSynced = i == position
                    }

                    notifyDataSetChanged()
                }
            }
        })
    }

    fun getItems(): List<Item> {
        return items
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

        val tvName: TextView = mView.findViewById(R.id.tvName)
        val tvImeNumber: TextView = mView.findViewById(R.id.tvImeNumber)
        val cbSync: RadioButton = mView.findViewById(R.id.cbSync)
    }
}