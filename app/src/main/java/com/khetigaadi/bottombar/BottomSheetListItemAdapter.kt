package com.khetigaadi.bottombar
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khetigaadi.R
import com.khetigaadi.databinding.FragmentBottomMenuListDialogListDialogItemBinding
import com.khetigaadi.model.NameAndImage
class BottomSheetListItemAdapter(private val mItemCount:List<NameAndImage>,val contextt:Context,var listener:ClickListener):RecyclerView.Adapter<BottomSheetListItemAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent:ViewGroup,viewType:Int):MyViewHolder {
        return MyViewHolder(FragmentBottomMenuListDialogListDialogItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }
    override fun onBindViewHolder(holder:MyViewHolder,position:Int) {
        val item = mItemCount[position]
        holder.binding.tvTitle.text = item.name
        holder.binding.ivImage.setImageResource(item.drawable)
        println("vcbvbcvbcv===="+item.name)
       // if(item.name.equals("Shop"))
            //holder.binding.text.background.setTint(contextt.getColor(R.color.babyBlueEyes))
        holder.binding.root.setOnClickListener { listener.onItemClick(position,it) }
    }
    override fun getItemCount():Int {
        return mItemCount.size
    }
    inner class MyViewHolder(val binding:FragmentBottomMenuListDialogListDialogItemBinding):RecyclerView.ViewHolder(binding.root)
    interface ClickListener {
        fun onItemClick(position:Int,view:View)
    }
}