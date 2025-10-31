package com.khetigaadi.harvester
import android.content.Context
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
import com.khetigaadi.databinding.NewImplementHomeListItemBinding
import com.khetigaadi.harvester.model.Harvester
import com.khetigaadi.harvester.model.Others
import com.khetigaadi.util.NumberFormatting
class SimilarHarvesterListAdapter(private val mContext:Context,recyclerView:RecyclerView?) :
    BaseRecyclerViewAdapter<Harvester,Others>(mContext,recyclerView) {
    private val VIEW_ITEM = 1
    private val VIEW_PROG = 0
    private var myClickListener: Listener? = null
    private val EMPTY_VIEW = 10
    fun setOnItemClickListener(myClickListener: Listener?) {
        this.myClickListener = myClickListener
    }
    override fun getItemViewType(position: Int): Int {
        if (items.size == 0) {
            return EMPTY_VIEW
        }
        return if (list[position] != null) VIEW_ITEM else VIEW_PROG
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            EMPTY_VIEW -> {
                return EmptyViewHolder(
                    EmptyViewBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            VIEW_ITEM -> {
                ViewHolder(
                    NewImplementHomeListItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                val v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.progressbar_for_horizontal_item, parent, false)
                ProgressViewHolder(mContext, v)
            }
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val currentTractor = list[position]
            if (currentTractor != null) {
                holder.binding.popularTractorTitle.text = currentTractor.name

                Glide.with(mContext)
                    .load(
                        if (others != null && currentTractor.harvesterImage != null) Constants.decodeURL(
                            others!!.harvesterImagePath + currentTractor.harvesterImage.name
                        ) else null
                    )
                    .error(R.mipmap.no_image)
                    .placeholder(R.mipmap.no_image)
                    .into(holder.binding.popularTractorImage)
                holder.binding.btnGetPrice.setOnClickListener { v: View? ->
                    if (myClickListener != null) myClickListener!!.onGetPriceClick(
                        position,
                        v
                    )
                }
                if (currentTractor.views != null) {
                    holder.binding.tvViews.text =
                        NumberFormatting.format(currentTractor.views.toString())
                }
                /* if (currentTractor.hp != null) {
                     holder.binding.tvHP.text = String.format("%s %s", currentTractor.hp.hpRange, mContext.getString(R.string.hp))
                 }*/
                if (currentTractor.harvesterType != null) {
                    holder.binding.tvType.text = currentTractor.harvesterType.name
                }
            }
        } else {
            (holder as ProgressViewHolder).progressBar.isIndeterminate = true
        }
    }
    private inner class ViewHolder(val binding: NewImplementHomeListItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        override fun onClick(v: View) {
            myClickListener!!.onItemClick(absoluteAdapterPosition, v)
        }
        init {
            itemView.rootView.setOnClickListener(this)
        }
    }
    interface Listener {
        fun onItemClick(position: Int, v: View?)
        fun onGetPriceClick(position: Int, v: View?)
    }
}