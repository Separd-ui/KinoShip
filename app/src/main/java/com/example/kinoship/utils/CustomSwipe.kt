package com.example.kinoship.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.kinoship.R
import com.google.android.material.snackbar.Snackbar

class CustomSwipe(
    val context: Context,
    val onSwiped: OnSwiped
) : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT){


    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position=viewHolder.adapterPosition
        onSwiped.swipedToDelete(position)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )

        val background= ColorDrawable(Color.RED)
        val icon= ContextCompat.getDrawable(context, R.drawable.ic_delete_v2)
        val itemView = viewHolder.itemView
        val cornerOffset = 20
        val iconMargin = (itemView.height - icon!!.intrinsicHeight) / 2
        val iconTop= itemView.top + (itemView.height - icon.intrinsicHeight) / 2
        val iconBot = iconTop + icon.intrinsicHeight


        if (dX < 0) {
            val iconLeft: Int = itemView.right - iconMargin - icon.intrinsicWidth
            val iconRight = itemView.right - iconMargin
            icon.setBounds(iconLeft, iconTop, iconRight, iconBot)
            background.setBounds(
                itemView.right + dX.toInt() - cornerOffset,
                itemView.top,
                itemView.right,
                itemView.bottom
            )
        }
        background.draw(c)
        icon.draw(c)
    }


    interface OnSwiped{
        fun swipedToDelete(pos:Int)
    }
}