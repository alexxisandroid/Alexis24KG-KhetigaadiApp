package com.khetigaadi.implement.newImplement
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.khetigaadi.BuildConfig
import com.khetigaadi.R
import com.khetigaadi.base.util.BaseRecyclerViewAdapter
import com.khetigaadi.commons.utils.EmptyViewHolder
import com.khetigaadi.commons.utils.MyClickListener
import com.khetigaadi.commons.utils.ProgressViewHolder
import com.khetigaadi.databinding.EmptyViewBinding
import com.khetigaadi.databinding.NewImplementListItemBinding
import com.khetigaadi.implement.newImplement.model.Others
import com.khetigaadi.implement.newImplement.util.NewImp
import com.khetigaadi.util.NumberFormatting
class NewImplementListAdapter(private val mContext:Context,recyclerView:RecyclerView?):BaseRecyclerViewAdapter<NewImp?,Others?>(mContext,recyclerView) {
    private val VIEW_ITEM = 1
    private val VIEW_PROG = 0
    private var myGetPriceClickListener:MyClickListener? = null
    private lateinit var myClickListener:MyClickListener
    fun setOnItemClickListener(listener:MyClickListener) {
        this.myClickListener = listener
    }
    override fun getItemViewType(position:Int):Int {
        if (items.size == 0) {
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
                ViewHolder(NewImplementListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            }
            else -> {
                ProgressViewHolder(mContext, LayoutInflater.from(parent.context).inflate(R.layout.progressbar_item,parent,false))
            }
        }
    }
    override fun onBindViewHolder(holder:RecyclerView.ViewHolder,position:Int) {
        if (holder is ViewHolder) {
            val currentTractor = items[position]
            if (currentTractor != null) {
                if (!currentTractor.implementName.isEmpty()) {
                    holder.binding.tvNewTractorName.text = currentTractor.implementName
                }
                if (currentTractor.type != null) {
                    if (!currentTractor.type.name.isEmpty()) {
                        holder.binding.tvType.text = currentTractor.type.name
                    }
                }
                holder.binding.tvViews.text = NumberFormatting.format(currentTractor.views)
                if (currentTractor.hp != null) {
                    holder.binding.tvHP.text = currentTractor.hp.hpRange
                } else {
                    holder.binding.tvHP.visibility = View.GONE
                }
              //  println("cxvcvxcvcxv==="+currentTractor.implementImage.image)
                var bpath = others?.implementImagePath
                if(others==null || others!!.implementImagePath==null) {
                    bpath = BuildConfig.BASE_URL+"images/new-implement/";
                }
                var pathh = "";
                if(currentTractor.implementImage!=null)
                    pathh =currentTractor.implementImage.image
                Glide.with(mContext)
                    //.load(if (currentTractor.implementImage != null && others != null) bpath + currentTractor.implementImage.image else "")
                    .load(bpath+pathh)
                    .placeholder(R.mipmap.no_image)
                    .error(R.mipmap.no_image)
                     .into(holder.binding.ivImage)
            }

//        } else if (holder instanceof ProgressViewHolder)  {
//            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }
    override fun getItemCount():Int {
        return if (items.size > 0) items.size else 1
    }
    fun setGetPriceClick(myGetPriceClickListener:MyClickListener?) {
        this.myGetPriceClickListener = myGetPriceClickListener
    }
    private inner class ViewHolder(val binding:NewImplementListItemBinding):RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener { v:View? ->
                myClickListener.onItemClick(absoluteAdapterPosition,v)
            }
            binding.btnGetPrice.setOnClickListener { rippleView:View? ->
                if (myGetPriceClickListener != null) myGetPriceClickListener!!.onItemClick(absoluteAdapterPosition,rippleView)
            }
        }
    }
    companion object {
        private const val EMPTY_VIEW = 10
    }
}