package com.khetigaadi.tractor.compare
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.khetigaadi.R
import com.khetigaadi.base.util.BaseRecyclerViewAdapter
import com.khetigaadi.commons.utils.EmptyViewHolder
import com.khetigaadi.commons.utils.ProgressViewHolder
import com.khetigaadi.databinding.CompareTrListItemBinding
import com.khetigaadi.databinding.EmptyViewBinding
import com.khetigaadi.model.Others
import com.khetigaadi.tractor.compare.util.Tractors
class ComparePopularTractorAdapter(private val mContext:Context,recyclerView:RecyclerView?):BaseRecyclerViewAdapter<Tractors?,Others?>(mContext,recyclerView) {
    private var myClickListener: Listener? = null
    private val VIEW_ITEM = 1
    private val VIEW_PROG = 0
    private val EMPTY_VIEW = 10
    fun setOnItemClickListener(myClickListener:Listener) {
        this.myClickListener = myClickListener
    }
    override fun getItemViewType(position: Int):Int {
        if (list.isEmpty()) {
            return EMPTY_VIEW
        }
        return if (list[position] != null) VIEW_ITEM else VIEW_PROG
    }
    override fun onCreateViewHolder(parent:ViewGroup,viewType:Int):RecyclerView.ViewHolder {
        return if (viewType == EMPTY_VIEW) {
            return EmptyViewHolder(EmptyViewBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }
        else if(viewType == VIEW_ITEM) {
            ViewHolder(CompareTrListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }
        else {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.progressbar_item,parent,false)
            ProgressViewHolder(mContext,v)
        }
    }
    override fun onBindViewHolder(holder:RecyclerView.ViewHolder,position:Int) {
        if (holder is ViewHolder) {
            println("bcvbcbcvbcvbbcvb====="+position)
            val currentTractor = list[position]
            if (currentTractor != null) {
                holder.binding.tvHP.text =  String.format("%s %s",currentTractor.firstTractor.exactHp,mContext.getString(R.string.hp))
                holder.binding.tvHP1.text = String.format("%s %s",currentTractor.secondTractor.exactHp,mContext.getString(R.string.hp))
                try {
                    holder.binding.tvTractorName.text = currentTractor.firstTractor.name
                    holder.binding.tvTractorName1.text = currentTractor.secondTractor.name
                }
                catch (e: Exception) {
                    e.printStackTrace()
                }
                Glide.with(mContext)
                    .load(if (currentTractor.firstTractor.image.tractor_img_name != "") others!!.tractorImagePath+currentTractor.firstTractor.image.tractor_img_name else "")
                    .placeholder(R.mipmap.no_image)
                    .error(R.mipmap.no_image)
                    .into(holder.binding.ivTractor)
                Glide.with(mContext)
                    .load(if (currentTractor.secondTractor.image.tractor_img_name != "") others!!.tractorImagePath+currentTractor.secondTractor.image.tractor_img_name else "")
                    .placeholder(R.mipmap.no_image)
                    .error(R.mipmap.no_image)
                    .into(holder.binding.ivTractor1)
            }
        }
    }
    override fun getItemCount(): Int {
        return if (list.isNotEmpty()) list.size else 1
    }
    inner class ViewHolder(val binding: CompareTrListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener { view: View? ->
                myClickListener?.onItemClick(absoluteAdapterPosition,view,list[absoluteAdapterPosition])
            }
        }
    }
    interface Listener {
        fun onItemClick(position:Int,v:View?,get:Tractors?)
    }
}