package com.khetigaadi.tractor.dealer
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.khetigaadi.R
import com.khetigaadi.base.util.BaseRecyclerViewAdapter
import com.khetigaadi.commons.utils.EmptyViewHolder
import com.khetigaadi.commons.utils.MyClickListener
import com.khetigaadi.commons.utils.MyTrashClickListener
import com.khetigaadi.commons.utils.ProgressViewHolder
import com.khetigaadi.databinding.DealerListItemBinding
import com.khetigaadi.databinding.DealerNameBinding
import com.khetigaadi.databinding.EmptyViewBinding
import com.khetigaadi.model.Others
import com.khetigaadi.tractor.dealer.model.Dealer
class DealersListAdapter(private val mContext:Context,recyclerView:RecyclerView?):BaseRecyclerViewAdapter<Dealer?,Others?>(mContext,recyclerView) {
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
            if (currentAppli.address == null || currentAppli.address.equals("",ignoreCase = true)) {
                holder.binding.tvDealerAddress.visibility = View.GONE
            } else {
                holder.binding.tvDealerAddress.visibility = View.VISIBLE
                if (currentAppli.user != null && currentAppli.address != null && currentAppli.user.city != null) holder.binding.tvDealerAddress.text =
                    String.format("%s, %s", currentAppli.address, currentAppli.user.city.name)
                else holder.binding.tvDealerAddress.text = currentAppli.address
            }
            if (currentAppli.user == null || currentAppli.user.email == null || TextUtils.isEmpty(
                    currentAppli.user.email
                )
            ) {
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
//            holder.binding.tvDealerName.setOnClickListener {
//                Snackbar.make(
//                    holder.binding.tvDealerEmail,
//                    "" + holder.binding.tvDealerName.text,
//                    Snackbar.LENGTH_INDEFINITE).apply {
//                    setAction("OK") {
//                        // appUpdateManager.completeUpdate()
//                        setAnchorView(R.id.anchorCenter) }
//                    show()
//                }
//            }
            holder.binding.readMoreContainer.setOnClickListener {
               val addressVal = holder.binding.tvDealerAddress.text.toString()
                val mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(addressVal))
                val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)
                mapIntent.setPackage("com.google.android.apps.maps")

                if(mapIntent.resolveActivity(mContext.packageManager) != null) {
                    mContext.startActivity(mapIntent)
                }
            }
            holder.binding.tvDealerName.setOnClickListener {
                // get prompts.xml view
                val d = Dialog(mContext)
                d.requestWindowFeature(Window.FEATURE_NO_TITLE)
                d.setCancelable(true)
                d.setCanceledOnTouchOutside(true)
                val dialogBinding: DealerNameBinding = DealerNameBinding.inflate((mContext as Activity).layoutInflater)
                d.setContentView(dialogBinding.getRoot())
                val textView = d.findViewById<TextView?>(R.id.tv_dealerName)
                if(textView != null) {
                    textView.text = holder.binding.tvDealerName.text
                }
                d.show()
            }
            holder.binding.tvDealerEmail.setOnClickListener {
                try {
                    val email =  holder.binding.tvDealerEmail.text
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:$email") // Only email apps should handle this
                    }
                    // Verify that there is an email app to handle the intent
                    if(intent.resolveActivity(mContext.packageManager) != null) {
                        mContext.startActivity(intent)
                    }
                    else {
                        Toast.makeText(mContext,"No email app found",Toast.LENGTH_SHORT).show()
                    }
                }
                catch(e:Exception) {
                    Toast.makeText(mContext, "Error opening email app", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    override fun getItemCount():Int {
        return if (items.size > 0) items.size else 1
    }
    private inner class ViewHolder(val binding:DealerListItemBinding):RecyclerView.ViewHolder(binding.root) {
        init {
            binding.btnGetDealerInfo.setOnClickListener { view1: View? -> myClickListener!!.onItemClick(adapterPosition, view1) }
            binding.tvDealerCont.setOnClickListener { v: View? -> myTrashClickListener!!.onTrashClick(adapterPosition, v) }
            binding.btnCall.setOnClickListener { v: View? -> myTrashClickListener!!.onTrashClick(adapterPosition, v) }

           // binding.tvDealerName.setOnClickListener { view1: View? -> myClickListener!!.onItemClick(adapterPosition, view1) }

        }
    }
    companion object {
        private const val EMPTY_VIEW = 10
    }
}