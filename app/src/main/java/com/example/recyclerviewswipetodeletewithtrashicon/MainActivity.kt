package com.example.recyclerviewswipetodeletewithtrashicon

import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var arrayList = ArrayList<ModelClass>()
    private var data = arrayOf("a", "b", "c", "d", "e", "f", "g", "h", "i")
    private var myAdapter: MyAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myAdapter = MyAdapter(this, getData())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )
        recyclerView.adapter = myAdapter

        setSwipeToDelete()
    }

    private fun setSwipeToDelete() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val modelClass = arrayList[viewHolder.adapterPosition]
                val position = viewHolder.adapterPosition
                arrayList.removeAt(viewHolder.adapterPosition)
                myAdapter!!.notifyItemRemoved(viewHolder.adapterPosition)
                Snackbar.make(recyclerView!!, modelClass.name, Snackbar.LENGTH_LONG).setAction(
                    "Undo"
                ) {
                    arrayList.add(position, modelClass)
                    myAdapter!!.notifyItemInserted(position)
                    recyclerView!!.scrollToPosition(position)
                }.show()
            }

            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                return 1f
            }

            override fun onChildDraw(
                c: Canvas, recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
            ) {
                setDeleteIcon(c, viewHolder, dX, dY, isCurrentlyActive)
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }).attachToRecyclerView(recyclerView)
    }

    private fun setDeleteIcon(
        c: Canvas,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        isCurrentlyActive: Boolean
    ) {
        val mClearPaint = Paint()
        mClearPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        val mBackground = ColorDrawable()
        val backgroundColor = Color.parseColor("#b80f0a")
        val deleteDrawable = ContextCompat.getDrawable(this, R.drawable.ic_baseline_delete)
        val intrinsicWidth = deleteDrawable!!.intrinsicWidth
        val intrinsicHeight = deleteDrawable.intrinsicHeight
        val itemView = viewHolder.itemView
        val itemHeight = itemView.height
        val isCancelled = dX == 0f && !isCurrentlyActive
        if (isCancelled) {
            c.drawRect(
                itemView.right + dX, itemView.top.toFloat(),
                itemView.right.toFloat(), itemView.bottom.toFloat(), mClearPaint
            )
            return
        }
        mBackground.color = backgroundColor
        mBackground.setBounds(
            itemView.right + dX.toInt(),
            itemView.top,
            itemView.right,
            itemView.bottom
        )
        mBackground.draw(c)
        val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
        val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
        val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth
        val deleteIconRight = itemView.right - deleteIconMargin
        val deleteIconBottom = deleteIconTop + intrinsicHeight
        deleteDrawable.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
        deleteDrawable.draw(c)
    }

    private fun getData(): ArrayList<ModelClass> {
        for (i in data.indices) {
            arrayList.add(ModelClass(data[i]))
        }
        return arrayList
    }
}