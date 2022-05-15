package com.example.recyclerviewswipetodeletewithtrashicon

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_item.view.*

class MyAdapter(context: MainActivity, data: ArrayList<ModelClass>) :
    RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    private val context: Context
    private val data: ArrayList<ModelClass>

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.row_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val modelClass = data[position]
        holder.itemView.textView.text = modelClass.name
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.rowitem.setOnClickListener {
                Toast.makeText(
                    context, data[adapterPosition].toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    init {
        this.context = context
        this.data = data
    }
}