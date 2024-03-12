package com.tp.recyclertree

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tp.recyclertree.databinding.FragmentFirstBinding

private const val TAG = "FirstFragment"
/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private lateinit var binding: FragmentFirstBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
        val listTemp = arrayListOf("1", "2", "3", "4", "5")
        setAdapter(listItems = listTemp)
    }

    private fun setListener() {
        binding.tvUpdate.setOnClickListener {
            activity?.let {
                hideKeyboard(it, binding.etSize)
            }
            val strEdit = binding.etSize.text.toString().trim()
            val listItems = ArrayList<String>()
            if(strEdit.isNotEmpty()) {
                var size = strEdit.toInt()
                if(size > 8 ) {
                    size = 8
                }
                for(i in 1..size) {
                    listItems.add("$i")
                }
            }
            setAdapter(listItems)
        }
    }

    private fun setAdapter(listItems : ArrayList<String>) {
        val adapter = TreeItemAdapter(listItems)
        val glm = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        glm.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        glm.reverseLayout = true

        binding.rvItems.layoutManager = glm
        val marginBottomItem = resources.getDimension(R.dimen.bottom_margin_item).toInt()
        var marginLine = resources.getDimension(R.dimen.margin_top_dotted_line_odd).toInt()
        val heightSingleItem = resources.getDimension(R.dimen.height_recycler_single_item).toInt() + marginBottomItem
        var height = (((listItems.size / 2) + 1) * heightSingleItem)

        Log.d(TAG, "setAdapter() marginBottomItem : $marginBottomItem  heightSingleItem $heightSingleItem height : $height")
        if(listItems.size%2 == 0) {
            val extraMarginEvenItem = resources.getDimension(R.dimen.extra_bottom_margin_item).toInt()

            Log.d(TAG, "setAdapter() extraMarginEvenItem : $extraMarginEvenItem ")

            height -= heightSingleItem

            height += extraMarginEvenItem

            marginLine = resources.getDimension(R.dimen.margin_top_dotted_even).toInt()
        } else {

        }
        Log.d(TAG, "setAdapter() final height : $height ")

        binding.rvItems.layoutParams.height = height

        val lineParams = binding.line.layoutParams as MarginLayoutParams
        lineParams.topMargin = marginLine


        binding.rvItems.adapter = adapter

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

}