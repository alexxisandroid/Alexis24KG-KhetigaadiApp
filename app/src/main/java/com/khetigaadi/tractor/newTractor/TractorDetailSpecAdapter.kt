package com.khetigaadi.tractor.newTractor

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.khetigaadi.databinding.TractorSpecContentBinding
import com.khetigaadi.databinding.UsedTractorDetailsListItemBinding
import com.khetigaadi.tractor.newTractor.TractorDetailSpecAdapter.SpecViewHolder
import com.khetigaadi.tractor.newTractor.model.NewTrSpecList

class TractorDetailSpecAdapter(
    private val context: Context,
    private val items: List<NewTrSpecList>
) : RecyclerView.Adapter<SpecViewHolder>() {
    override fun onCreateViewHolder(parent:ViewGroup,viewType:Int):SpecViewHolder {
        return SpecViewHolder(TractorSpecContentBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }
    override fun onBindViewHolder(holder:SpecViewHolder,position:Int) {
        holder.binding.tvSpecTitle.text = items[position].name
        holder.binding.llSpecData.removeAllViews()
        for (i in items[position].tractorSpec.indices) {
            val item = items[position].tractorSpec[i]
            val linearLayout = LinearLayout(context)
            if (item.tractorSpecData == null || item.tractorSpecData.specData == null || TextUtils.isEmpty(item.tractorSpecData.specData)) {
                continue
            } else {
                linearLayout.weightSum = 2f
                linearLayout.orientation = LinearLayout.VERTICAL
                val rowLayout = UsedTractorDetailsListItemBinding.inflate(LayoutInflater.from(context),linearLayout,false)
                rowLayout.tvLabel.text = item.tractorSpecName
                rowLayout.tvValue.text = item.tractorSpecData.specData
                if (holder.binding.llSpecData.childCount == 0)
                    rowLayout.vLine.visibility = View.GONE
                else
                    rowLayout.vLine.visibility = View.VISIBLE
                linearLayout.addView(rowLayout.root)
            }
            holder.binding.llSpecData.addView(linearLayout)
        }
    }
    override fun getItemCount(): Int {
        return items.size
    }
    class SpecViewHolder(val binding: TractorSpecContentBinding) :
        RecyclerView.ViewHolder(binding.root)
}