package com.tp.recyclertree

import android.content.Context
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener

class RecyclerTouchListener(
    context: Context?,
    private val recyclerGestureCallback: RecyclerGestureCallback?
) : OnItemTouchListener {
    private val gestureDetector: GestureDetector =
        GestureDetector(context, object : SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return true
            }

            override fun onLongPress(e: MotionEvent) {
            }

        })

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                recyclerGestureCallback?.onTouchDown()
            }
            MotionEvent.ACTION_UP -> {
                recyclerGestureCallback?.onTouchUp()
            } else -> {
                val child: View? = rv.findChildViewUnder(e.x, e.y)
                if (child != null && recyclerGestureCallback != null && gestureDetector.onTouchEvent(e)) {
                    recyclerGestureCallback.onClick(child, rv.getChildLayoutPosition(child))
                }
            }
        }
        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {

    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
    interface RecyclerGestureCallback {
        fun onClick(view: View?, position: Int) {}
        fun onTouchDown() {}
        fun onTouchUp() {}
    }

}