package com.khetigaadi.implement
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
//import com.khetigaadi.base.databinding.EmptyViewLayoutBinding
//import com.khetigaadi.base.databinding.SearchListItemBinding
//import com.khetigaadi.commons.model.search.Others
import com.khetigaadi.shop.model.search.Others;
import com.khetigaadi.commons.utils.EmptyViewHolder
import com.khetigaadi.databinding.EmptyViewLayoutBinding
import com.khetigaadi.databinding.SearchListItemBinding
import com.khetigaadi.implement.newImplement.util.NewImp
class SearchImplementAdapter(val context:Context,private val mDataset:List<NewImp>,val others:Others):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var myClickListener:Listener
    fun setOnItemClickListener(myClickListener:Listener) {
        this.myClickListener = myClickListener
    }
    override fun getItemViewType(position:Int):Int {
        return if (mDataset.size == 0) EMPTY_VIEW else VIEW_ITEM
    }
    override fun onCreateViewHolder(parent:ViewGroup,viewType:Int):RecyclerView.ViewHolder {
        return if(viewType == EMPTY_VIEW) {
            EmptyViewHolder(EmptyViewLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }
        else {
            DataObjectHolder(SearchListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }
    }
    override fun onBindViewHolder(holder:RecyclerView.ViewHolder,position:Int) {
        if(holder is DataObjectHolder) {
            val product:NewImp = mDataset[position]
            holder.binding.serchRes.text = product.implementName
            Glide.with(context)
                .load(others.implementImagePath + if(product.implementImage != null) product.implementImage.image else "")
                .into(holder.binding.ivImage)
            holder.binding.root.setOnClickListener {
                myClickListener.onItemClick(product)
            }
            println("cvbcbcbcbcv====="+product.brandId+"=="+product.implementTypeId+"=="+product.implementModelId+"=="+product)
        }
    }
    override fun getItemCount():Int {
        return if (mDataset.size > 0) mDataset.size else 1
    }
    internal inner class DataObjectHolder(binding:SearchListItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        val binding:SearchListItemBinding
        init {
            this.binding = binding
        }
    }
    internal inner class EmptyViewHolder(binding:EmptyViewLayoutBinding):
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
        fun onItemClick(implement:NewImp)
    }
}