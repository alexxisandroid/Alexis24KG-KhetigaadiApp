package com.khetigaadi.tractor.compare
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.khetigaadi.databinding.CompareTractorSpecListItemBinding
import com.khetigaadi.databinding.TractorSpecContentBinding
import com.khetigaadi.tractor.compare.model.CompTracSpecific
class CompareTractorDetailSpecAdapter(
    private val context: Context,
    private val items: List<CompTracSpecific>
) : RecyclerView.Adapter<CompareTractorDetailSpecAdapter.SpecViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecViewHolder {
        return SpecViewHolder(TractorSpecContentBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }
    override fun onBindViewHolder(holder:SpecViewHolder,position:Int) {
        holder.binding.tvSpecTitle.text = items[position].name
        holder.binding.llSpecData.removeAllViews()
        for (i in items[position].tractorSpec.indices) {
            val item = items[position].tractorSpec[i]
            val linearLayout = LinearLayout(context)
            if(item.compareTractorSpecData == null || item.compareTractorSpecData.size == 0 || (TextUtils.isEmpty(item.compareTractorSpecData[0].specData) && TextUtils.isEmpty(item.compareTractorSpecData[1].specData))) {
                continue
            } else {
                linearLayout.weightSum = 2f
                linearLayout.orientation = LinearLayout.VERTICAL
                val rowLayout = CompareTractorSpecListItemBinding.inflate(LayoutInflater.from(context),linearLayout,false)
                rowLayout.tvLabel.text = item.tractorSpecName
                if (item.compareTractorSpecData != null) {
                    if (item.compareTractorSpecData.size > 0 && !TextUtils.isEmpty(item.compareTractorSpecData[0].specData))
                        rowLayout.tvValue.text = item.compareTractorSpecData[0].specData

                    if (item.compareTractorSpecData.size > 1 && !TextUtils.isEmpty(item.compareTractorSpecData[1].specData))
                        rowLayout.tvValue1.text = item.compareTractorSpecData[1].specData
                }
                if (rowLayout.tvValue.text.equals("-") && rowLayout.tvValue1.text.equals("-"))
                    continue
                if (holder.binding.llSpecData.childCount == 0)
                    rowLayout.vLine.visibility = View.GONE
                else
                    rowLayout.vLine.visibility = View.VISIBLE
                linearLayout.addView(rowLayout.root)
            }
            holder.binding.llSpecData.addView(linearLayout)
        }
        if (holder.binding.llSpecData.childCount == 0)
            holder.binding.root.visibility = View.GONE
        else
            holder.binding.root.visibility = View.VISIBLE
    }
    override fun getItemCount():Int {
        return items.size
    }
    class SpecViewHolder(val binding:TractorSpecContentBinding):RecyclerView.ViewHolder(binding.root)
}