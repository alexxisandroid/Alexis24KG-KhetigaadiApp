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
import com.khetigaadi.commons.utils.EmptyViewHolder
import com.khetigaadi.commons.utils.ProgressViewHolder
import com.khetigaadi.databinding.EmptyViewBinding
import com.khetigaadi.databinding.NewTractorListItemBinding
import com.khetigaadi.model.Others
import com.khetigaadi.tractor.newTractor.model.NewTractor
import com.khetigaadi.util.NumberFormatting
class NewTractorListAdapter internal constructor(private val mContext:Context,recyclerView: RecyclerView?):BaseRecyclerViewAdapter<NewTractor,Others>(mContext,recyclerView) {
    private val VIEW_ITEM = 1
    private val VIEW_PROG = 0
    private lateinit var listener: Listener
    private val EMPTY_VIEW = 10
    fun setOnItemClickListener(myClickListener:Listener) {
        this.listener = myClickListener
    }
    override fun getItemViewType(position:Int):Int {
        if(items.size == 0) {
            return EMPTY_VIEW
        }
        return if(items[position] != null) VIEW_ITEM else VIEW_PROG
    }
    override fun onCreateViewHolder(parent:ViewGroup,viewType:Int):RecyclerView.ViewHolder {
        return when (viewType) {
            EMPTY_VIEW -> EmptyViewHolder(EmptyViewBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            VIEW_ITEM -> ViewHolder(NewTractorListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            else -> ProgressViewHolder(mContext,LayoutInflater.from(parent.context).inflate(R.layout.progressbar_item,parent,false))
        }
    }
    override fun onBindViewHolder(holder:RecyclerView.ViewHolder,position:Int) {
        if (holder is ViewHolder) {
            val currentTractor = items[position]!!
            holder.binding.tvNewTractorName.text = currentTractor.name
            if (currentTractor.exactHp != null) {
                holder.binding.tvHP.visibility = View.VISIBLE
                holder.binding.tvHP.text = String.format("%s %s", currentTractor.exactHp, mContext.getString(R.string.hp))
            } else {
                holder.binding.tvHP.visibility = View.GONE
            }
            holder.binding.tvViews.text = NumberFormatting.format(currentTractor.userView)
            if (currentTractor.isSponsor) {
                holder.binding.container.setBackgroundResource(R.drawable.btn_rectangle_outline_bg_red_and_details_background)
                holder.binding.tvSponsor.visibility = View.VISIBLE
            }
            else {
               // holder.binding.container.setBackgroundColor(mContext.resources.getColor(R.color.detailsBackground))
                holder.binding.container.setBackgroundColor(mContext.resources.getColor(R.color.white))

                holder.binding.tvSponsor.visibility = View.GONE
            }
            for(shortSpec in currentTractor.getShortSpecData) {
                if(!TextUtils.isEmpty(shortSpec.specData)) {
                    if(shortSpec.tractorSpecId == 5) {
                        holder.binding.tvNoOfCylinder.text = shortSpec.specData
                        holder.binding.tvNoOfCylinder.visibility = View.VISIBLE
                    }
                    else {
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
            if (currentTractor.drive != null) {
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
            if(currentTractor.image != null && currentTractor.image.tractor_img_name != null) {
              // val imgg = AppConfig.BASE_ASSET_URL+"tr:h-350,w-650/new-tractor/"
                val imgg = others!!.tractorImagePath
                //others!!.tractorImagePath
                println("cvxvcxvxvcxv====="+imgg+currentTractor.image.tractor_img_name)
//                if(position==0) {
//                   Glide.with(mContext)
//                       .load(R.drawable.tract_650)
//                       .error(R.mipmap.no_image)
//                       .placeholder(R.mipmap.no_image)
//                       .into(holder.binding.ivNewTractorImage)
//               }
//                else if (position==1) {
//                   Glide.with(mContext)
//                       .load(R.drawable.tract_700)
//                       .error(R.mipmap.no_image)
//                       .placeholder(R.mipmap.no_image)
//                       .into(holder.binding.ivNewTractorImage)
//               }
//                else
                  Glide.with(mContext)
                    .load(imgg+currentTractor.image.tractor_img_name)
                    .error(R.mipmap.no_image)
                    .placeholder(R.mipmap.no_image)
                    .into(holder.binding.ivNewTractorImage)
            }
            if(currentTractor.isBookmark) {
                holder.binding.ivFavorites.setImageResource(R.drawable.ic_favorites)
            }
            else {
                holder.binding.ivFavorites.setImageResource(R.drawable.ic_favorites_border)
            }
            //        } else if (holder instanceof ProgressViewHolder) {
//            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }
    override fun getItemCount():Int {
        return if (items.size > 0) items.size else 1
    }
    private inner class ViewHolder(val binding:NewTractorListItemBinding):RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener { v:View -> listener.onClick(absoluteAdapterPosition,v) }
            binding.ivFavorites.setOnClickListener { v:View -> listener.onFavoriteClick(absoluteAdapterPosition,v) }
            binding.btnOnRoadPrice.setOnClickListener { v:View -> listener.onGetRoadPriceClick(absoluteAdapterPosition,v) }
            binding.btnRequestForDemo.setOnClickListener { v:View -> listener.onGetDemoClick(absoluteAdapterPosition,v) }
        }
    }
    interface Listener {
        fun onClick(position:Int,view:View)
        fun onFavoriteClick(position:Int,view:View)
        fun onGetRoadPriceClick(position:Int,view:View)
        fun onGetDemoClick(position:Int,view:View)
    }
}