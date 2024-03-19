package com.tp.recyclertree

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tp.recyclertree.databinding.LayoutJourneyItemBinding

private const val TAG = "JourneyItemAdapter"

class JourneyItemAdapter(
    private val listItems: ArrayList<ArrayList<String>>
) : RecyclerView.Adapter<JourneyItemAdapter.ItemViewHolder>() {

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
            setAdapterCustom(journeyItem)
        }

        private fun setAdapterCustom(listItems: ArrayList<String>) {
            TreeItemAdapterCustom(binding.rvItems, listItems = listItems)
        }
    }

}