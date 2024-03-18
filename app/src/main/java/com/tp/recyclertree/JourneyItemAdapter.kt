package com.tp.recyclertree

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tp.recyclertree.databinding.LayoutJourneyItemBinding

private const val TAG = "JourneyItemAdapter"

class JourneyItemAdapter(
    private val listItems: ArrayList<ArrayList<String>>
) : RecyclerView.Adapter<JourneyItemAdapter.ItemViewHolder>() {

    private var selectedPosition: Int = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        Log.d(TAG, "onCreateViewHolder  viewType >> ${viewType}  ")
        val binding = LayoutJourneyItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder() position : $position")
        val itemJourney = listItems[position]
        holder.onBind(itemJourney)
    }


    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount  >> ${listItems.size}  ")
        return listItems.size
    }

    inner class ItemViewHolder(
        private val binding: LayoutJourneyItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(journeyItem: ArrayList<String>) {
            Log.d(TAG, "onBind() ")
            setAdapter(journeyItem)
        }

        private fun setAdapter(listItems: ArrayList<String>) {
            val adapter = TreeItemAdapter(listItems)
            val glm = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            glm.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
            glm.reverseLayout = true

            binding.rvItems.layoutManager = glm
            val marginBottomItem =
                binding.root.context.resources.getDimension(R.dimen.bottom_margin_item).toInt()
            var marginLine =
                binding.root.context.resources.getDimension(R.dimen.margin_top_dotted_line_odd)
                    .toInt()
            val heightSingleItem =
                binding.root.context.resources.getDimension(R.dimen.height_recycler_single_item)
                    .toInt() + marginBottomItem
            var height = (((listItems.size / 2) + 1) * heightSingleItem)

            Log.d(
                TAG,
                "setAdapter() marginBottomItem : $marginBottomItem  heightSingleItem $heightSingleItem height : $height"
            )
            if (listItems.size % 2 == 0) {
                val extraMarginEvenItem =
                    binding.root.context.resources.getDimension(R.dimen.extra_bottom_margin_item)
                        .toInt()

                Log.d(TAG, "setAdapter() extraMarginEvenItem : $extraMarginEvenItem ")

                height -= heightSingleItem

                height += extraMarginEvenItem

                marginLine =
                    binding.root.context.resources.getDimension(R.dimen.margin_top_dotted_even)
                        .toInt()
            } else {

            }
            Log.d(TAG, "setAdapter() final height : $height ")

            binding.rvItems.layoutParams.height = height

            val lineParams = binding.line.layoutParams as ViewGroup.MarginLayoutParams
            lineParams.topMargin = marginLine


            binding.rvItems.adapter = adapter

        }
    }

}