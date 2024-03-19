package com.tp.recyclertree

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tp.recyclertree.databinding.LayoutTreeItemLeftBinding
import com.tp.recyclertree.databinding.LayoutTreeItemRightBinding

private const val TAG = "TreeItemAdapterCustom"

class TreeItemAdapterCustom(
    private val parent: ViewGroup,
    private val listItems: ArrayList<String>,
) {

    init {
        createTree()
    }

    private fun createTree() {
        for (i in 0 until listItems.size) {
            val viewHolder = createViewHolder(getItemViewType(i))
            bindViewHolder(viewHolder, i)

            /** Set margin for view to **/
            val marginLayoutParams =
                viewHolder.itemView.layoutParams as ViewGroup.MarginLayoutParams

            val heightSingleItem =
                parent.context.resources.getDimension(R.dimen.height_recycler_single_item)
                    .toInt() + parent.context.resources.getDimension(R.dimen.bottom_margin_item)
                    .toInt()
            var marginBottom = if (i == 0) 0 else (i / 2) * heightSingleItem
            Log.d(
                TAG,
                "createTree position : $i marginBottom : $marginBottom  heightSingleItem $heightSingleItem height "
            )
            if ((i + 1) % 2 == 0) {
                val extraMarginEvenItem =
                    parent.context.resources.getDimension(R.dimen.extra_bottom_margin_item_custom)
                        .toInt()
                marginBottom += extraMarginEvenItem
                Log.d(TAG, "createTree extraMarginEvenItem : $extraMarginEvenItem ")
            }

            Log.d(TAG, "createTree marginBottom : $marginBottom")

            marginLayoutParams.bottomMargin = marginBottom

//            parent.addView(viewHolder.itemView)
        }
    }

    private fun createViewHolder(viewType: Int): ItemViewHolder {
        Log.d(TAG, "onCreateViewHolder  viewType >> $viewType")
        return when (viewType) {
            TreeItemViewType.NORMAL_RIGHT.viewTypeId -> {
                val binding = LayoutTreeItemRightBinding.inflate(
                    LayoutInflater.from(parent.context), parent, true
                )
                ItemViewHolderNormalRight(binding = binding)
            }

            else -> {
                val binding = LayoutTreeItemLeftBinding.inflate(
                    LayoutInflater.from(parent.context), parent, true
                )
                ItemViewHolderNormalLeft(binding = binding)
            }
        }

    }

    private fun bindViewHolder(holder: ItemViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder() position : $position")
        val treeItem = listItems[position]
        holder.onBind(treeItem, position = position)
    }

    enum class TreeItemViewType(val viewTypeId: Int) {
        NORMAL_LEFT(1),
        NORMAL_RIGHT(2)
    }

    private fun getItemViewType(position: Int): Int {
        return when {
            (position + 1) % 2 == 0 -> TreeItemViewType.NORMAL_RIGHT.viewTypeId
            else -> TreeItemViewType.NORMAL_LEFT.viewTypeId
        }

    }

    abstract inner class ItemViewHolder(val itemView: View) {
        abstract fun onBind(label: String, position: Int)
    }

    inner class ItemViewHolderNormalLeft(
        private val binding: LayoutTreeItemLeftBinding
    ) : ItemViewHolder(binding.root) {

        override fun onBind(label: String, position: Int) {
            Log.d(TAG, "ItemViewHolder Left onBind() position : $position }")
            if (position == 2) {
                binding.tvLabel.setImageResource(R.drawable.ic_myglam)
            }
        }
    }

    inner class ItemViewHolderNormalRight(
        private val binding: LayoutTreeItemRightBinding
    ) : ItemViewHolder(binding.root) {

        override fun onBind(label: String, position: Int) {
            Log.d(TAG, "ItemViewHolder Right onBind() position : $position }")
        }
    }

}