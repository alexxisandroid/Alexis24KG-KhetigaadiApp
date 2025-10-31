package com.khetigaadi.tractor.newTractor.search
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.khetigaadi.R
//import com.khetigaadi.base.databinding.EmptyViewLayoutBinding
//import com.khetigaadi.base.databinding.SearchListItemBinding
//import com.khetigaadi.commons.model.search.Others
import com.khetigaadi.shop.model.search.Others;
import com.khetigaadi.commons.utils.EmptyViewHolder
import com.khetigaadi.databinding.EmptyViewLayoutBinding
import com.khetigaadi.databinding.SearchListItemBinding
//import com.khetigaadi.shop.search.EmptyViewHolder
import com.khetigaadi.tractor.newTractor.model.NewTractor
class SearchNewTractorAdapter(
    val context:Context,
    private val mDataset:List<NewTractor>,val others:Others):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var myClickListener:Listener
    fun setOnItemClickListener(myClickListener:Listener) {
        this.myClickListener = myClickListener
    }
    override fun getItemViewType(position: Int): Int {
        return if (mDataset.isEmpty()) EMPTY_VIEW else VIEW_ITEM
    }
    override fun onCreateViewHolder(parent:ViewGroup,viewType:Int):RecyclerView.ViewHolder {
        return if (viewType == EMPTY_VIEW) {
            EmptyViewHolder(EmptyViewLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        } else {
            DataObjectHolder(SearchListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }
    }
    override fun onBindViewHolder(holder:RecyclerView.ViewHolder,position:Int) {
        if (holder is DataObjectHolder) {
            val product: NewTractor = mDataset[position]
            holder.binding.serchRes.text = product.name
            Glide.with(context)
                .load(others.tractorImagePath + product.image?.tractor_img_name)
                .placeholder(R.mipmap.no_image)
                .error(R.mipmap.no_image)
                .into(holder.binding.ivImage)
            holder.binding.root.setOnClickListener {
                myClickListener.onItemClick(product)
            }
        }
    }
    override fun getItemCount(): Int {
        return if (mDataset.isNotEmpty()) mDataset.size else 1
    }
    internal inner class DataObjectHolder(binding:SearchListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val binding: SearchListItemBinding
        init {
            this.binding = binding
        }
    }
    internal inner class EmptyViewHolder(binding:EmptyViewLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val binding: EmptyViewLayoutBinding
        init {
            this.binding = binding
        }
    }
    companion object {
        private const val EMPTY_VIEW = 10
        private const val VIEW_ITEM = 1
    }
    interface Listener {
        fun onItemClick(tractor:NewTractor)
    }
}