package com.tp.recyclertree

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tp.recyclertree.databinding.FragmentFirstBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


private const val TAG = "FirstFragment"
/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private val animDelay = 2000L
    private lateinit var binding: FragmentFirstBinding
    /** Manager class for auto scroll of recycler **/
    private var tpRecyclerAutoScrollManager: TpRecyclerAutoScrollManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setListener()
        setJourneyAdapter()
    }

    private val itemChangeDelay = 470L
    private val itemSpeedPerPixel = 2400F

    private fun initView() {
        /** Init TP Recycler auto scroll manager
         * , provide recycler view and use rest values as default
         *  Make sure to call init this manager object before recycler adapter is set
         * **/
        tpRecyclerAutoScrollManager = TpRecyclerAutoScrollManager(
            recyclerView = binding.rvJourney,
            itemChangeDelay = itemChangeDelay,
            speedPerPixelScrollGeneric = itemSpeedPerPixel,
            speedPerPixelLastItem = TpRecyclerAutoScrollManager.DEFAULT_SPEED_PIXEL_SCROLL_LAST_ITEM,
            isSetTouchListenerToPausePlay = true
        )
    }

    private fun checkAndStartAutoScroll() {
        AppLog.d(TAG, "checkAndStartAutoScroll()")
        tpRecyclerAutoScrollManager?.startAutoScroll()
    }

    private var journeyItemList = ArrayList<ArrayList<String>>()
    private fun setJourneyAdapter() {
        val listTemp1 = arrayListOf("1", "2", "3", "4", "5")
        val listTemp2 = arrayListOf("1", "2", "3", "4")
        val listTemp3 = arrayListOf("1", "2", "3")
        val listTemp4 = arrayListOf("1", "2")
        val listTemp5 = arrayListOf("1")
        val listTemp6 = arrayListOf("1", "2", "3", "4")
        val listTemp7 = arrayListOf("1", "2", "3")
        journeyItemList = arrayListOf(listTemp1, listTemp2, listTemp3, listTemp4, listTemp5, listTemp6, listTemp7)
        val adapter = JourneyItemAdapter(journeyItemList)
        binding.rvJourney.adapter = adapter

        lifecycleScope.launch {
            delay(1000)
            checkAndStartAutoScroll()
        }
    }

    private fun setListener() {
        binding.etSize.visibility = View.GONE
        binding.tvUpdate.setOnClickListener {
            if(isViewRight) {
                animateViewRightToLeft()
            } else {
                animateViewLeftToRight()
            }

//            activity?.let {
//                hideKeyboard(it, binding.etSize)
//            }
//            val strEdit = binding.etSize.text.toString().trim()
//            val listItems = ArrayList<String>()
//            if(strEdit.isNotEmpty()) {
//                var size = strEdit.toInt()
//                if(size > 8 ) {
//                    size = 8
//                }
//                for(i in 1..size) {
//                    listItems.add("$i")
//                }
//            }
//            setAdapter(listItems)
        }


        binding.rvJourney.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if(binding.rvJourney.layoutManager is LinearLayoutManager) {
                    val lastVisibleItem = (binding.rvJourney.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                    if(lastVisibleItem == (journeyItemList.size - 1)) {
                        lifecycleScope.launch {
                            tpRecyclerAutoScrollManager?.removeScrollHandlerCallback()
                            animateViewLeftToRight()
                            delay(animDelay)
                            checkAndStartAutoScroll()
                            animateViewRightToLeft()
                        }

                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

    private fun setAdapterLinear() {
//        val listTemp = arrayListOf("1", "2", "3", "4", "5")
//        val adapter = TreeItemAdapter(listTemp)
//        val glm = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
//        binding.rvItems.layoutManager = glm
//        binding.rvItems.adapter = adapter
    }

    private fun hideKeyboard(activity: Activity, field: View?) {
        try {
            val imm = activity.getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            if (field != null) {
                Log.d(TAG,"hideKeyboard imm $imm")
                Log.d(TAG,"hideKeyboard field getWindowToken " + field.windowToken)
                field.clearFocus()
                imm.hideSoftInputFromWindow(field.windowToken, 0)
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            Log.d("", "hideKeyboard() Exception " + e.message)
        }
    }

    private var isViewRight = false

    private fun animateViewLeftToRight() {
        activity?.let {
            val floatLength =  (Resources.getSystem().displayMetrics.widthPixels - (resources.getDimension(R.dimen.margin_3).toInt() + resources.getDimension(R.dimen.lottie_size).toInt())).toFloat()
            ObjectAnimator.ofFloat(binding.lottiePersona, "translationX", floatLength).apply {
                duration = animDelay
                start()
            }
        }
        isViewRight = true
    }

    private fun animateViewRightToLeft() {
        val displayMetrics = DisplayMetrics()
        activity?.let {
            val floatLength =  resources.getDimension(R.dimen.margin_3)
            ObjectAnimator.ofFloat(binding.lottiePersona, "translationX", floatLength).apply {
                duration = animDelay
                start()
            }
        }
        isViewRight = false
    }


    override fun onDestroy() {
        super.onDestroy()
        tpRecyclerAutoScrollManager?.removeScrollHandlerCallback()
        tpRecyclerAutoScrollManager = null
    }

    override fun onStop() {
        super.onStop()
        tpRecyclerAutoScrollManager?.removeScrollHandlerCallback()
    }

    override fun onPause() {
        super.onPause()
        AppLog.d(TAG, "onPause()")
        tpRecyclerAutoScrollManager?.removeScrollHandlerCallback()
    }

    private var isFirstTimeResume = true
    override fun onResume() {
        super.onResume()
        AppLog.d(
            TAG,
            "onResume() responseData : "
        )

        if(!isFirstTimeResume) {
            checkAndStartAutoScroll()
            isFirstTimeResume = false
        }
    }
}