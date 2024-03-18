package com.tp.recyclertree

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

/** Created by Shubham Pundir 8 Nov 2023
 ** @Note  Make sure to call init this manager object before recycler adapter is set
 */
class TpRecyclerAutoScrollManager(
    private val recyclerView: RecyclerView,
    private val itemChangeDelay: Long = ITEM_CHANGE_DELAY_DEFAULT,
    private val speedPerPixelScrollGeneric: Float = DEFAULT_SPEED_PIXEL_SCROLL,
    private val speedPerPixelLastItem: Float = DEFAULT_SPEED_PIXEL_SCROLL_LAST_ITEM,
    private val isSetTouchListenerToPausePlay: Boolean = true
) {


    private val handler = Handler(Looper.getMainLooper())
    private var speedPerPixelScroll = speedPerPixelScrollGeneric
    private var isLastItemScrolled = false
    private var smoothScrollRequestedPosition = -1

    companion object {
        private const val TAG = "TpRecyclerAutoScrollManager"
        const val DEFAULT_SPEED_PIXEL_SCROLL = 8000f
        const val DEFAULT_SPEED_PIXEL_SCROLL_LAST_ITEM = 300f
        const val ITEM_CHANGE_DELAY_DEFAULT = 1500L
    }

    private val llManager: LinearLayoutManager =
        object : LinearLayoutManager(recyclerView.context, HORIZONTAL, false) {
            override fun smoothScrollToPosition(
                recyclerView: RecyclerView,
                state: RecyclerView.State,
                position: Int
            ) {
                val smoothScroller: LinearSmoothScroller =
                    object : LinearSmoothScroller(recyclerView.context) {
                        override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                            /** Default value for speedPerPixelScroll in recycler is =25f **/
                            return speedPerPixelScroll / displayMetrics.densityDpi
                        }
                    }
                smoothScroller.targetPosition = position
                startSmoothScroll(smoothScroller)
            }
        }

    init {
        /** Set Linear layout manager where we have controlled smooth scroll speed **/
        recyclerView.layoutManager = llManager

        /** Set touch listener for recycler to stop and resume auto scroll **/
        if (isSetTouchListenerToPausePlay) {
            setTouchListenerForRecycler()
        }
    }


    /** Start Autoscroll for recycler,
     * @Note make sure to call this only after adapter is set on recycler **/
    fun startAutoScroll() {
        postHandlerForSmoothScroll(runnable, itemChangeDelay)
    }

    private fun setTouchListenerForRecycler() {
        recyclerView.addOnItemTouchListener(
            RecyclerTouchListener(
                recyclerView.context,
                object : RecyclerTouchListener.RecyclerGestureCallback {
                    @SuppressLint("Recycle")
                    override fun onClick(view: View?, position: Int) {
                        AppLog.d(TAG, "RecyclerTouchListener onClick() : position : $position")
                    }

                    override fun onTouchUp() {
                        super.onTouchUp()
                        AppLog.d(TAG, "RecyclerTouchListener onTouchUp() ")
                        postHandlerForSmoothScroll(runnable, 100)
                    }

                    override fun onTouchDown() {
                        super.onTouchDown()
                        AppLog.d(TAG, "RecyclerTouchListener onTouchDown() ")
                        removeScrollHandlerCallback()
                    }
                })
        )
    }

    private val runnable = object : Runnable {
        override fun run() {

            AppLog.d(TAG, " isLastItemScrolled     .... $isLastItemScrolled ")

            /** Check if last item scrolled,
             * then post handler for next item if view has scrolled back to first item and it is visible
             * else post handle to check this scenario again
             * **/
            if (isLastItemScrolled) {
                val firstVisibleItem = llManager.findFirstCompletelyVisibleItemPosition()
                AppLog.d(TAG, " firstVisibleItem     .... $firstVisibleItem ")

                if (firstVisibleItem != 0) {
                    postHandlerForSmoothScroll(this, itemChangeDelay)
                    return
                }
            }


            /** Find last visible item position of recycler view and start count from there **/
            var nextItemPositionToScroll = llManager.findLastCompletelyVisibleItemPosition()
            nextItemPositionToScroll++

            /** If next position is out of list size , then start list scroll again from position 0 **/
            if (nextItemPositionToScroll == (recyclerView.adapter?.itemCount ?: -1)) {
                nextItemPositionToScroll = 0
                isLastItemScrolled = true
                speedPerPixelScroll = speedPerPixelLastItem

            } else {
                speedPerPixelScroll = speedPerPixelScrollGeneric
                isLastItemScrolled = false
            }


            /** check if nextItemPositionToScroll is equal to previous request scroll item,
             * if yes then don't scroll again for same item
             * simply post runnable to check again
             * **/
            if (nextItemPositionToScroll == smoothScrollRequestedPosition) {
                nextItemPositionToScroll++
            }

            AppLog.d(
                TAG,
                "smoothScrollRequestedPosition : $smoothScrollRequestedPosition nextItemPositionToScroll = $nextItemPositionToScroll "
            )

            if (nextItemPositionToScroll < (recyclerView.adapter?.itemCount ?: -1)) {
                AppLog.d(
                    TAG,
                    "runnable smoothScrollToPosition() nextItemPositionToScroll = $nextItemPositionToScroll "
                )
                smoothScrollRequestedPosition = nextItemPositionToScroll
                recyclerView.smoothScrollToPosition(nextItemPositionToScroll)
                postHandlerForSmoothScroll(this, itemChangeDelay)
            } else {
                postHandlerForSmoothScroll(this, itemChangeDelay)
            }
        }
    }

    private fun postHandlerForSmoothScroll(runnable: Runnable, scrollSpeedInMilliseconds: Long) {
        handler.postDelayed(runnable, scrollSpeedInMilliseconds)
    }

    fun removeScrollHandlerCallback() {
        handler.removeCallbacks(runnable)
        smoothScrollRequestedPosition = -1
    }
}