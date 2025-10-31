package com.khetigaadi.harvester
import android.content.Context
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
import com.khetigaadi.databinding.ImplementHomeNewListItemBinding
import com.khetigaadi.databinding.NewImplementListItemBinding
import com.khetigaadi.harvester.model.Harvester
import com.khetigaadi.harvester.model.Others
import com.khetigaadi.util.NumberFormatting
class HarvesterListAdapter(
    private val mContext: Context,
    recyclerView: RecyclerView?,
    private val isFromHome: Boolean
) :
    BaseRecyclerViewAdapter<Harvester, Others>(mContext, recyclerView) {
    private val VIEW_ITEM = 1
    private val VIEW_PROG = 0
    private var myGetPriceClickListener: MyClickListener? = null
    private lateinit var myClickListener: Listener
    fun setOnItemClickListener(listener: Listener) {
        this.myClickListener = listener
    }
    override fun getItemViewType(position: Int): Int {
        if (items.size == 0) {
            return EMPTY_VIEW
        }
        return if (items[position] != null) VIEW_ITEM else VIEW_PROG
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            EMPTY_VIEW -> {
                EmptyViewHolder(
                    EmptyViewBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            VIEW_ITEM -> {
                if (isFromHome) {
                    HomeViewHolder(
                        ImplementHomeNewListItemBinding.inflate(
                            LayoutInflater.from(parent.context),
                            parent,
                            false
                        )
                    )
                } else {
                    ViewHolder(
                        NewImplementListItemBinding.inflate(
                            LayoutInflater.from(parent.context),
                            parent,
                            false
                        )
                    )
                }
            }
            else -> {
                ProgressViewHolder(
                    mContext, LayoutInflater.from(parent.context).inflate(
                        R.layout.progressbar_item, parent, false
                    )
                )
            }
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val currentTractor = items[position]
            if (currentTractor != null) {
                if (currentTractor.name.isNotEmpty()) {
                    holder.binding.tvNewTractorName.text = currentTractor.name
                }
                if (currentTractor.harvesterType != null) {
                    if (!currentTractor.harvesterType.name.isEmpty()) {
                        holder.binding.tvType.text = currentTractor.harvesterType.name
                    }
                }
                holder.binding.tvViews.text =
                    NumberFormatting.format(currentTractor.views.toString())
                /*if (currentTractor.hp != null) {
                    holder.binding.tvHP.text = currentTractor.hp.hpRange
                } else {*/
                holder.binding.tvHP.visibility = View.GONE
//                }
                Glide.with(mContext)
                    .load(if (currentTractor.harvesterImage != null && others != null) others.harvesterImagePath + currentTractor.harvesterImage.name else "")
                    .placeholder(R.mipmap.no_image)
                    .error(R.mipmap.no_image)
                    .into(holder.binding.ivImage)
            }
        } else if (holder is HomeViewHolder) {
            val currentTractor = items[position]
            if (currentTractor != null) {
                if (currentTractor.name.isNotEmpty()) {
                    holder.binding.popularTractorTitle.text = currentTractor.name
                }
                if (currentTractor.harvesterType != null) {
                    if (!currentTractor.harvesterType.name.isEmpty()) {
                        holder.binding.tvType.text = currentTractor.harvesterType.name
                    }
                }
                holder.binding.tvViews.text =
                    NumberFormatting.format(currentTractor.views.toString())
                holder.binding.tvHP.visibility = View.GONE
                Glide.with(mContext)
                    .load(if (currentTractor.harvesterImage != null && others != null) others.harvesterImagePath + currentTractor.harvesterImage.name else "")
                    .placeholder(R.mipmap.no_image)
                    .error(R.mipmap.no_image)
                    .into(holder.binding.popularTractorImage)
            }
        }
    }
    override fun getItemCount(): Int {
        return if (items.size > 0) items.size else 1
    }
    fun setGetPriceClick(myGetPriceClickListener: MyClickListener?) {
        this.myGetPriceClickListener = myGetPriceClickListener
    }
    private inner class ViewHolder(val binding: NewImplementListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener { v: View? ->
                myClickListener.onItemClick(
                    absoluteAdapterPosition,
                    v,
                    items[absoluteAdapterPosition]
                )
            }
            binding.btnGetPrice.setOnClickListener { rippleView: View? ->
                if (myGetPriceClickListener != null) myGetPriceClickListener!!.onItemClick(
                    absoluteAdapterPosition,
                    rippleView
                )
            }
        }
    }
    private inner class HomeViewHolder(val binding: ImplementHomeNewListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener { v: View? ->
                myClickListener.onItemClick(
                    absoluteAdapterPosition,
                    v, items[absoluteAdapterPosition]
                )
            }
            binding.btnGetPrice.setOnClickListener { rippleView: View? ->
                if (myGetPriceClickListener != null) myGetPriceClickListener!!.onItemClick(
                    absoluteAdapterPosition,
                    rippleView
                )
            }
        }
    }
    companion object {
        private const val EMPTY_VIEW = 10
    }
    interface Listener {
        fun onItemClick(position: Int, view: View?, harvester: Harvester)
    }
}