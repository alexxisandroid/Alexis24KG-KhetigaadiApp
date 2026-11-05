package com.khetigaadi.tractor.newTractor
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.khetigaadi.R
import com.khetigaadi.base.util.BaseRecyclerViewAdapter
import com.khetigaadi.commons.utils.Constants
import com.khetigaadi.commons.utils.EmptyViewHolder
import com.khetigaadi.commons.utils.ProgressViewHolder
import com.khetigaadi.databinding.EmptyViewBinding
import com.khetigaadi.databinding.PopularTractorItemBinding
import com.khetigaadi.tractor.PopularTractorAdapter
import com.khetigaadi.tractor.newTractor.model.NewTractor
import com.khetigaadi.tractor.newTractor.model.NewTractorOthers
import com.khetigaadi.util.NumberFormatting

class SimilarTractorAdapter(private val mContext:Context,recyclerView:RecyclerView?) :
    BaseRecyclerViewAdapter<NewTractor?,NewTractorOthers?>(mContext,recyclerView) {
    private lateinit var listener: PopularTractorAdapter.Listener
    private val VIEW_ITEM = 1
    private val VIEW_PROG = 0
    private val EMPTY_VIEW = 10
    fun setOnItemClickListener(myClickListener: PopularTractorAdapter.Listener) {
        listener = myClickListener
    }
    override fun getItemViewType(position:Int):Int {
        if(items.size == 0) {
            return EMPTY_VIEW
        }
        return if (items[position] != null) VIEW_ITEM else VIEW_PROG
    }
    override fun onCreateViewHolder(parent:ViewGroup,viewType:Int):RecyclerView.ViewHolder {
        return when(viewType) {
            EMPTY_VIEW -> {
                EmptyViewHolder(EmptyViewBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            }
            VIEW_ITEM -> {
                ViewHolder(PopularTractorItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            }
            else -> {
                ProgressViewHolder(mContext,LayoutInflater.from(parent.context).inflate(R.layout.progressbar_for_horizontal_item, parent, false))
            }
        }
    }
    override fun onBindViewHolder(holder:RecyclerView.ViewHolder,position:Int) {
        if(holder is ViewHolder) {
            val currentTractor = list[position]!!
            holder.binding.popularTractorTitle.text = currentTractor.name
            holder.binding.tvHP.text = String.format("%s %s",currentTractor.exactHp, mContext.getString(R.string.hp))
            if(currentTractor.userView != null && !TextUtils.isEmpty(currentTractor.userView)) {
                holder.binding.tvViews.text = NumberFormatting.format(currentTractor.userView.toInt().toLong())
            }
            Glide.with(mContext)
                .load(if (others != null && currentTractor.image != null) Constants.decodeURL(others!!.tractorImagePath + currentTractor.image.tractor_img_name) else "")
                .placeholder(R.mipmap.no_image)
                .error(R.mipmap.no_image)
                .into(holder.binding.popularTractorImage)
            holder.binding.root.setOnClickListener { v:View? -> listener.onItemClick(position,v) }
            holder.binding.btnGetOnRoadPrice.setOnClickListener { v:View ->
                listener.onGetRoadPriceClick(position,v)
            }
        }
    }
    private class ViewHolder(val binding:PopularTractorItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}