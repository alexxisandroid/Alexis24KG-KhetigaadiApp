package com.khetigaadi.commons.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.khetigaadi.R
import com.khetigaadi.implement.newImplement.util.Brand;
import com.khetigaadi.base.util.BaseRecyclerViewAdapter
import com.khetigaadi.commons.utils.EmptyViewHolder
import com.khetigaadi.commons.utils.ProgressViewHolder
import com.khetigaadi.databinding.BrandListItemBinding
import com.khetigaadi.databinding.EmptyViewBinding
import com.khetigaadi.model.Others
class BrandAdapter(private val context:Context,recyclerView:RecyclerView?) :
    BaseRecyclerViewAdapter<Brand?,Others?>(context,recyclerView) {
    private val VIEW_ITEM = 1
    private val VIEW_PROG = 0
    private var clickListener: ClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            EMPTY_VIEW -> {
                EmptyViewHolder(EmptyViewBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            }
            VIEW_ITEM -> {
                ViewHolder(
                    BrandListItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                ProgressViewHolder(
                    context,
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.progressbar_item, parent, false)
                )
            }
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val item: Brand = items[position]!!
//            holder.binding.tvName.text = item.name
            val pathV= "https://khetigaadi.com/images/brandlogos/";
            Glide.with(context)
               // .load(if(item.image != null) SharedPreferenceUtils.getInstance(context).getStringValue(Constants.BRAND_BASE_URL, "") + item.image else "")
                .load(pathV+item.image)
                .error(R.mipmap.no_image)
                .placeholder(R.mipmap.no_image)
                .into(holder.binding.ivImage)
            holder.binding.root.setOnClickListener { v ->
                if (clickListener != null) clickListener!!.onItemClick(position,holder.binding.root)
            }
        }
    }
    override fun getItemViewType(position:Int):Int {
        if (items.size == 0) {
            return EMPTY_VIEW
        }
        return if (items[position] != null) VIEW_ITEM else VIEW_PROG
    }
    override fun getItemCount():Int {
        return if (items.size > 0) items.size else 1
    }
    fun setOnItemClickListener(clickListener:ClickListener?) {
        this.clickListener = clickListener
    }
    class ViewHolder(val binding:BrandListItemBinding):RecyclerView.ViewHolder(binding.root)
    interface ClickListener {
        fun onItemClick(position:Int,v:View?)
    }
    companion object {
        private const val EMPTY_VIEW = 10
    }
}