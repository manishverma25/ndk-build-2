package com.tp.recyclertree

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tp.recyclertree.databinding.LayoutTreeItemExtraMarginBinding
import com.tp.recyclertree.databinding.LayoutTreeItemLeftBinding
import com.tp.recyclertree.databinding.LayoutTreeItemRightBinding

private const val TAG = "TreeItemAdapter"

class TreeItemAdapter(
    private val listItems: ArrayList<String>,
) : RecyclerView.Adapter<TreeItemAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        Log.d(TAG, "onCreateViewHolder  viewType >> ${viewType}  ")
        return when (viewType) {
            TreeItemViewType.EXTRA_MARGIN_RIGHT.viewTypeId -> {
                val binding = LayoutTreeItemExtraMarginBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                ItemViewHolderExtraMargin(binding = binding)
            }

            TreeItemViewType.NORMAL_RIGHT.viewTypeId -> {
                val binding = LayoutTreeItemRightBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                ItemViewHolderNormalRight(binding = binding)
            }

            else -> {
                val binding = LayoutTreeItemLeftBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                ItemViewHolderNormalLeft(binding = binding)
            }
        }

    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder() position : $position")
        val filterType = listItems[position]
        holder.onBind(filterType)
    }


    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount  >> ${listItems.size}  ")
        return listItems.size
    }

    enum class TreeItemViewType(val viewTypeId: Int) {
        NORMAL_LEFT(1),
        NORMAL_RIGHT(2),
        EXTRA_MARGIN_RIGHT(3)
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 1 -> TreeItemViewType.EXTRA_MARGIN_RIGHT.viewTypeId
            (position + 1) % 2 == 0 -> TreeItemViewType.NORMAL_RIGHT.viewTypeId
            else -> TreeItemViewType.NORMAL_LEFT.viewTypeId
        }

    }

    abstract inner class ItemViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {

        abstract fun onBind(label: String)

    }

    inner class ItemViewHolderExtraMargin(
        private val binding: LayoutTreeItemExtraMarginBinding
    ) : ItemViewHolder(binding.root) {

        override fun onBind(label: String) {
            Log.d(TAG, "ItemViewHolderExtraMargin onBind() label : $label }")
        }

    }

    inner class ItemViewHolderNormalLeft(
        private val binding: LayoutTreeItemLeftBinding
    ) : ItemViewHolder(binding.root) {

        override fun onBind(label: String) {
            Log.d(TAG, "ItemViewHolderNormalLeft onBind() label : $label }")
            if(adapterPosition == 2) {
                binding.tvLabel.setImageResource(R.drawable.ic_myglam)
            }
        }
    }

    inner class ItemViewHolderNormalRight(
        private val binding: LayoutTreeItemRightBinding
    ) : ItemViewHolder(binding.root) {

        override fun onBind(label: String) {
            Log.d(TAG, "ItemViewHolderNormalRight onBind() label : $label }")
        }
    }

}