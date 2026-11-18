package com.khetigaadi.tractor.usedTractor
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.khetigaadi.AppConfig
/////import com.khetigaadi.BuildConfig
import com.khetigaadi.R
//import com.khetigaadi.base.databinding.EmptyViewLayoutBinding
//import com.khetigaadi.base.databinding.SearchListItemBinding
//import com.khetigaadi.commons.model.search.Others
//import com.khetigaadi.shop.model.search.Others;
//import com.khetigaadi.commons.model.search.Others

import com.khetigaadi.shop.model.search.Others
//import com.khetigaadi.commons.model.search.Others
//import com.khetigaadi.commons.utils.EmptyViewHolder
import com.khetigaadi.databinding.EmptyViewLayoutBinding
import com.khetigaadi.databinding.SearchListItemBinding
//import com.khetigaadi.shop.search.EmptyViewHolder
import com.khetigaadi.tractor.usedTractor.util.UsedTractor
class SearchUsedTractorAdapter(val context:Context,private val mDataset:List<UsedTractor>,val others:Others):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var myClickListener:Listener
    fun setOnItemClickListener(myClickListener:Listener) {
        this.myClickListener = myClickListener
    }
    override fun getItemViewType(position:Int):Int {
        return if(mDataset.size == 0) EMPTY_VIEW else VIEW_ITEM
    }
    override fun onCreateViewHolder(parent:ViewGroup,viewType:Int):RecyclerView.ViewHolder
    {
        return if(viewType == EMPTY_VIEW) {
            EmptyViewHolder(EmptyViewLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }
        else {
            DataObjectHolder(SearchListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder:RecyclerView.ViewHolder, position:Int)
    {
        if(holder is DataObjectHolder) {
            val product: UsedTractor = mDataset[position]
            val bUrl = AppConfig.BASE_ASSET_URL  //others.usedTractorImagePath
            holder.binding.serchRes.text = (if(product.brand != null)
                product.brand?.name else "") + " "+if (product.model != null) product.model?.name else ""
            Glide.with(context)
                //.load(others.usedTractorImagePath+product.image?.usedTractorImage)
                .load(bUrl+"used_tractor/"+product.image?.usedTractorImage)
                .placeholder(R.mipmap.no_image)
                .error(R.mipmap.no_image)
                .into(holder.binding.ivImage)

            //println("cvbcvbcbcb===="+others.usedTractorImagePath+product.image?.usedTractorImage)
            holder.binding.root.setOnClickListener {
                myClickListener.onItemClick(product)
            }
        }
    }
    override fun getItemCount(): Int {
        return if(mDataset.size > 0) mDataset.size else 1
    }
    internal inner class DataObjectHolder(binding:SearchListItemBinding):RecyclerView.ViewHolder(binding.root) {
        val binding:SearchListItemBinding
        init {
            this.binding = binding
        }
    }
    internal inner class EmptyViewHolder(binding:EmptyViewLayoutBinding):RecyclerView.ViewHolder(binding.root) {
        val binding:EmptyViewLayoutBinding
        init {
            this.binding = binding
        }
    }
    companion object {
        private const val EMPTY_VIEW = 10
        private const val VIEW_ITEM = 1
    }
    interface Listener {
        fun onItemClick(usedTractor: UsedTractor)
    }
}