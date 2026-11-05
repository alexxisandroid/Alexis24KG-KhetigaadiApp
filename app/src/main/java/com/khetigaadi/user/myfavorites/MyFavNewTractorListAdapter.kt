package com.khetigaadi.user.myfavorites
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.khetigaadi.AppConfig
import com.khetigaadi.R
import com.khetigaadi.base.util.BaseRecyclerViewAdapter
import com.khetigaadi.commons.utils.EmptyViewHolder
import com.khetigaadi.commons.utils.ProgressViewHolder
import com.khetigaadi.databinding.EmptyViewBinding
import com.khetigaadi.databinding.NewTractorListItemBinding
import com.khetigaadi.tractor.newTractor.NewTractorListAdapter
import com.khetigaadi.user.myfavorites.model.MyFavNewTractor
import com.khetigaadi.user.myfavorites.model.Others
import com.khetigaadi.util.NumberFormatting
class MyFavNewTractorListAdapter(private val mContext:Context,recyclerView:RecyclerView?):BaseRecyclerViewAdapter<MyFavNewTractor?,Others?>(mContext,recyclerView) {
    private val VIEW_ITEM = 1
    private val VIEW_PROG = 0
    private lateinit var listener:NewTractorListAdapter.Listener
    fun setOnItemClickListener(myClickListener:NewTractorListAdapter.Listener) {
        this.listener = myClickListener
    }
    override fun getItemViewType(position:Int):Int {
        if (items.size == 0) {
            return EMPTY_VIEW
        }
        return if (items[position] != null) VIEW_ITEM else VIEW_PROG
    }
    override fun onCreateViewHolder(parent:ViewGroup,viewType:Int):RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        if (viewType == EMPTY_VIEW) {
            return EmptyViewHolder(EmptyViewBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        } else if (viewType == VIEW_ITEM) {
            vh = ViewHolder(NewTractorListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.progressbar_item, parent, false)
            vh = ProgressViewHolder(mContext, v)
        }
        return vh
    }
    override fun onBindViewHolder(holder:RecyclerView.ViewHolder,position:Int) {
        if(holder is ViewHolder) {
            val currentTractor = items[position]!!.newTractor
            holder.binding.tvNewTractorName.text = currentTractor.name
            if (currentTractor.exactHp != null) {
                holder.binding.tvHP.visibility = View.VISIBLE
                holder.binding.tvHP.text = String.format("%s %s", currentTractor.exactHp, mContext.getString(R.string.hp))
            }
            else {
                holder.binding.tvHP.visibility = View.GONE
            }
            holder.binding.tvViews.text = NumberFormatting.format(currentTractor.userView)
            if (currentTractor.isSponsor) {
                holder.binding.container.setBackgroundResource(R.drawable.btn_rectangle_outline_bg_red_and_details_background)
            }
            else {
                holder.binding.container.setBackgroundColor(mContext.resources.getColor(R.color.detailsBackground))
            }
            for(shortSpec in currentTractor.getShortSpecData) {
                if(!TextUtils.isEmpty(shortSpec.specData)) {
                    if(shortSpec.tractorSpecId == 5) {
                        holder.binding.tvNoOfCylinder.text = shortSpec.specData
                        holder.binding.tvNoOfCylinder.visibility = View.VISIBLE
                    } else {
                        holder.binding.tvNoOfCylinder.visibility = View.GONE
                    }
                    if(shortSpec.tractorSpecId == 4) {
                        holder.binding.tvDisplacementCC.text = shortSpec.specData
                        holder.binding.tvDisplacementCC.visibility = View.VISIBLE
                    }
                    else {
                        holder.binding.tvDisplacementCC.visibility = View.GONE
                    }
                }
            }
            if(currentTractor.drive != null) {
                holder.binding.ivDrive.visibility = View.VISIBLE
                if (currentTractor.drive.id == 1) {
                    holder.binding.ivDrive.setImageResource(R.drawable.ic_2wd)
                } else {
                    holder.binding.ivDrive.setImageResource(R.drawable.ic_4wd)
                }
            }
            else {
                holder.binding.ivDrive.visibility = View.GONE
            }
          //  println("fdgdfgdfgd===="+ others?.tractorImagePath+currentTractor.image.tractor_img_name)
           val bUrl = AppConfig.BASE_ASSET_URL+"new-tractor/"  //others!!.tractorImagePath
            if(currentTractor.image != null && currentTractor.image.tractor_img_name != null) Glide.with(mContext)
                .load(bUrl+ currentTractor.image.tractor_img_name)
                .error(R.mipmap.no_image)
                //.listener()
                .placeholder(R.mipmap.no_image).into(holder.binding.ivNewTractorImage)
            if(currentTractor.isBookmark) {
                holder.binding.ivFavorites.setImageResource(R.drawable.ic_favorites)
            }
            else {
                holder.binding.ivFavorites.setImageResource(R.drawable.ic_favorites_border)
            }
        }
    }
    fun setLoaded() {
        loading = false
    }
    override fun getItemCount():Int {
        return if (items != null) {
            if (items.size > 0) items.size else 1
        } else 0
    }
    private inner class ViewHolder(val binding:NewTractorListItemBinding):RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener { v: View -> listener.onClick(absoluteAdapterPosition,v) }
            binding.ivFavorites.setOnClickListener { v: View -> listener.onFavoriteClick(absoluteAdapterPosition,v) }
            binding.btnOnRoadPrice.setOnClickListener { v: View -> listener.onGetRoadPriceClick(absoluteAdapterPosition,v) }
            binding.btnRequestForDemo.setOnClickListener { v: View -> listener.onGetDemoClick(absoluteAdapterPosition,v) }
        }
    }
    companion object {
        private const val EMPTY_VIEW = 10
    }
}