package com.khetigaadi.tractor
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.khetigaadi.R
import com.khetigaadi.base.util.BaseRecyclerViewAdapter
import com.khetigaadi.commons.utils.EmptyViewHolder
import com.khetigaadi.commons.utils.MyClickListener
import com.khetigaadi.commons.utils.ProgressViewHolder
import com.khetigaadi.databinding.EmptyViewBinding
import com.khetigaadi.databinding.PopularTractorNewListItemBinding
import com.khetigaadi.implement.newImplement.model.Others
import com.khetigaadi.tractor.newTractor.model.NewTractor
import com.khetigaadi.util.NumberFormatting
class PopularTractorAdapter(private val mContext:Context,recyclerView:RecyclerView?):BaseRecyclerViewAdapter<NewTractor?,Others?>(mContext,recyclerView) {
    fun setOnItemClickListener(listener: Listener) {
        myClickListener = listener
    }
    override fun getItemViewType(position:Int):Int {
        if (items.size == 0) {
            return EMPTY_VIEW
        }
        return if (items[position] != null) VIEW_ITEM else VIEW_PROG
    }
    override fun onCreateViewHolder(parent:ViewGroup,viewType:Int):RecyclerView.ViewHolder {
        return when (viewType) {
            EMPTY_VIEW -> {
                EmptyViewHolder(EmptyViewBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            }
            VIEW_ITEM -> {
                ViewHolder(PopularTractorNewListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            }
            else -> {
                ProgressViewHolder(mContext, LayoutInflater.from(parent.context).inflate(R.layout.progressbar_for_horizontal_item, parent, false))
            }
        }
    }
    override fun onBindViewHolder(holder:RecyclerView.ViewHolder,position:Int) {
        if (holder is ViewHolder) {
            val currentTractor = list[position]!!
            holder.binding.popularTractorTitle.text = currentTractor.name
            holder.binding.tvHP.text = String.format("%s %s", currentTractor.exactHp, mContext.getString(R.string.hp))
            if (currentTractor.userView != null && !TextUtils.isEmpty(currentTractor.userView)) {
                holder.binding.tvViews.text = NumberFormatting.format(currentTractor.userView.toInt().toLong())
            }
            Glide.with(mContext).load(if (others != null && currentTractor.image.tractor_img_name != null) others!!.newTractorImage + currentTractor.image.tractor_img_name else "").placeholder(R.mipmap.no_image).error(R.mipmap.no_image).into(holder.binding.popularTractorImage)
        }
    }
    private class ViewHolder(val binding:PopularTractorNewListItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener { v: View? ->
                myClickListener?.onItemClick(
                    absoluteAdapterPosition,
                    v
                )
            }
            binding.btnGetOnRoadPrice.setOnClickListener { v: View? ->
                run { myClickListener?.onGetRoadPriceClick(absoluteAdapterPosition, v) }
            }
        }
    }
    companion object {
        private var myClickListener: Listener? = null
        private const val VIEW_ITEM = 1
        private const val VIEW_PROG = 0
        private const val EMPTY_VIEW = 10

    }
    interface Listener:MyClickListener {
        fun onGetRoadPriceClick(position:Int,v:View?)
    }
}