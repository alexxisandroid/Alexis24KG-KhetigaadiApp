package com.khetigaadi.tractor.rent
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.khetigaadi.AppConfig
import com.khetigaadi.AppController
import com.khetigaadi.BuildConfig
import com.khetigaadi.R
import com.khetigaadi.base.util.BaseRecyclerViewAdapter
import com.khetigaadi.commons.utils.Constants
import com.khetigaadi.commons.utils.EmptyViewHolder
import com.khetigaadi.commons.utils.ProgressViewHolder
import com.khetigaadi.databinding.EmptyViewBinding
import com.khetigaadi.databinding.SimilarRentTractorListItemBinding
import com.khetigaadi.tractor.PopularTractorAdapter
import com.khetigaadi.tractor.newTractor.model.NewTractorOthers
import com.khetigaadi.tractor.rent.model.RentTractor
import com.khetigaadi.util.NumberFormatting
class SimilarRentTractorAdapter(private val mContext:Context,recyclerView:RecyclerView?):BaseRecyclerViewAdapter<RentTractor?, NewTractorOthers?>(mContext, recyclerView) {
    private val VIEW_ITEM = 1
    private val VIEW_PROG = 0
    private val EMPTY_VIEW = 10
    fun setOnItemClickListener(myClickListener:PopularTractorAdapter.Listener) {
        listener = myClickListener
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
                EmptyViewHolder(
                    EmptyViewBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            VIEW_ITEM -> {
                ViewHolder(SimilarRentTractorListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            else -> {
                ProgressViewHolder(mContext, LayoutInflater.from(parent.context).inflate(R.layout.progressbar_for_horizontal_item, parent, false))
            }
        }
    }
    override fun onBindViewHolder(holder:RecyclerView.ViewHolder,position:Int) {
        if (holder is ViewHolder) {
            val currentTractor = list[position]!!
            holder.binding.popularTractorTitle.text = String.format("%s %s",currentTractor.rentTractorBrand.name,currentTractor.tractorModel.getName())
            holder.binding.tvHP.text = String.format("%s %s",currentTractor.exactHp, mContext.getString(R.string.hp))
            holder.binding.tvViews.text = NumberFormatting.format(String.format("%d",currentTractor.userViews))

            var imgPath = AppConfig.BASE_ASSET_URL +"new-tractor/"
            if(others!=null && others!!.tractorImagePath!=null)
                imgPath = others!!.tractorImagePath
            println("cbcvcbcbvcbcbvb===="+imgPath+currentTractor.rentTractorTable.tractorImage.tractor_img_name)
            Glide.with(mContext)
//                .load(
//                    if(others != null && currentTractor.rentTractorTable.tractorImage != null)
//                        imgPath+currentTractor.rentTractorTable.tractorImage.tractor_img_name
//                    else ""
//                )
                .load(imgPath+currentTractor.rentTractorTable.tractorImage.tractor_img_name)
                .placeholder(R.mipmap.no_image).error(R.mipmap.no_image)
                .into(holder.binding.popularTractorImage)
        }
    }
    private class ViewHolder(val binding:SimilarRentTractorListItemBinding):RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener { v:View? -> listener?.onItemClick(absoluteAdapterPosition, v) }
        }
    }
    companion object {
        private var listener: PopularTractorAdapter.Listener? = null
    }
}