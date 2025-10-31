package com.khetigaadi.tractor.dealer
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
import com.khetigaadi.commons.utils.MyClickListener
import com.khetigaadi.commons.utils.MyTrashClickListener
import com.khetigaadi.commons.utils.ProgressViewHolder
import com.khetigaadi.databinding.DealerListItemBinding
import com.khetigaadi.databinding.EmptyViewBinding
import com.khetigaadi.model.Others
import com.khetigaadi.tractor.dealer.model.Dealer
class DealersListAdapter(private val mContext:Context,recyclerView:RecyclerView?) : BaseRecyclerViewAdapter<Dealer?,Others?>(mContext,recyclerView) {
    private val VIEW_ITEM = 1
    private val VIEW_PROG = 0
    private var myClickListener:MyClickListener? = null
    private var myTrashClickListener:MyTrashClickListener? = null
    fun setOnGetDetailsClickListener(myClickListener:MyClickListener?) {
        this.myClickListener = myClickListener
    }
    fun setOnCallClickListener(myTrashClickListener:MyTrashClickListener?) {
        this.myTrashClickListener = myTrashClickListener
    }
    override fun getItemViewType(position:Int):Int {
        if (items.size == 0) {
            return EMPTY_VIEW
        }
        return if (items[position] != null) VIEW_ITEM else VIEW_PROG
    }
    override fun onCreateViewHolder(parent:ViewGroup,viewType:Int):RecyclerView.ViewHolder {
        return when (viewType) {
            EMPTY_VIEW -> EmptyViewHolder(EmptyViewBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            VIEW_ITEM -> ViewHolder(DealerListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            else -> ProgressViewHolder(mContext,LayoutInflater.from(parent.context).inflate(R.layout.progressbar_item,parent,false))
        }
    }
    override fun onBindViewHolder(holder:RecyclerView.ViewHolder,position:Int) {
        if (holder is ViewHolder) {
            val currentAppli = items[position]!!
            holder.binding.tvDealerName.text = currentAppli.name
            if (currentAppli.address == null || currentAppli.address.equals("", ignoreCase = true)) {
                holder.binding.tvDealerAddress.visibility = View.GONE
            } else {
                holder.binding.tvDealerAddress.visibility = View.VISIBLE
                if (currentAppli.user != null && currentAppli.address != null && currentAppli.user.city != null) holder.binding.tvDealerAddress.text =
                    String.format("%s, %s",currentAppli.address,currentAppli.user.city.name)
                else holder.binding.tvDealerAddress.text = currentAppli.address
            }
            if (currentAppli.user == null || currentAppli.user.email == null || TextUtils.isEmpty(currentAppli.user.email)) {
                holder.binding.tvDealerEmail.visibility = View.GONE
            } else {
                holder.binding.tvDealerEmail.visibility = View.VISIBLE
                holder.binding.tvDealerEmail.text = currentAppli.user.email
            }
            holder.binding.tvDealerCont.visibility = View.GONE
            holder.binding.btnCall.visibility = View.GONE
            Glide.with(mContext)
                .load(if (currentAppli.brandImage != null && others != null) others!!.brandLogoPath + currentAppli.brandImage.image else null)
                .placeholder(R.mipmap.no_image)
                .error(R.mipmap.no_image)
                    .into(holder.binding.brandImage)
        }
    }
    override fun getItemCount(): Int {
        return if (items.size > 0) items.size else 1
    }
    private inner class ViewHolder(val binding:DealerListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.btnGetDealerInfo.setOnClickListener { view1: View? -> myClickListener!!.onItemClick(adapterPosition, view1) }
            binding.tvDealerCont.setOnClickListener { v: View? -> myTrashClickListener!!.onTrashClick(adapterPosition, v) }
            binding.btnCall.setOnClickListener { v: View? -> myTrashClickListener!!.onTrashClick(adapterPosition, v) }
        }
    }
    companion object {
        private const val EMPTY_VIEW = 10
    }
}