package com.khetigaadi.tractor.usedTractor

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.khetigaadi.R
import com.khetigaadi.base.util.BaseRecyclerViewAdapter
import com.khetigaadi.commons.utils.*
import com.khetigaadi.databinding.EmptyViewBinding
import com.khetigaadi.databinding.UsedTractorHomeListItemBinding
import com.khetigaadi.databinding.UsedTractorListItemBinding
import com.khetigaadi.model.Others
import com.khetigaadi.tractor.usedTractor.util.UsedTractor
import com.khetigaadi.util.NumberFormatting
import com.khetigaadi.util.Utils

class UsedTractorListAdapter(
    private val mContext: Context,
    private val userId: String?,
    recyclerView: RecyclerView?,
    private val isFromHome: Boolean
) : BaseRecyclerViewAdapter<UsedTractor?, Others?>(mContext, recyclerView) {
    private var myClickListener: MyClickListener? = null
    private var myTrashClickListener: MyTrashClickListener? = null
    private var myFavoritesClickListener: MyFavoritesClickListener? = null
    private var onChatClickListener: MyClickListener? = null
    fun setOnItemClickListener(myClickListener: MyClickListener?) {
        this.myClickListener = myClickListener
    }

    fun setOnSellerDetailsClickListener(myTrashClickListener: MyTrashClickListener?) {
        this.myTrashClickListener = myTrashClickListener
    }

    fun setOnFavoritesClickListener(myFavoritesClickListener: MyFavoritesClickListener?) {
        this.myFavoritesClickListener = myFavoritesClickListener
    }

    override fun getItemViewType(position: Int): Int {
        if (items.size == 0) {
            return EMPTY_VIEW
        }
        return if (items[position] != null) VIEW_ITEM else VIEW_PROG
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == EMPTY_VIEW) {
            return EmptyViewHolder(
                EmptyViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else if (viewType == VIEW_ITEM) {
            if (isFromHome)
                HomeViewHolder(
                    UsedTractorHomeListItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            else
                ViewHolder(
                    UsedTractorListItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
        } else {
            val v = LayoutInflater.from(parent.context).inflate(
                R.layout.progressbar_item, parent, false
            )
            ProgressViewHolder(mContext, v)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val currentTractor = items[position]!!
            holder.binding.tvTractorName.text = String.format(
                "%s %s",
                if (currentTractor.brand != null) currentTractor.brand.name else "",
                if (currentTractor.model != null) currentTractor.model.name else ""
            )
            if (currentTractor.user != null) {
                if (!TextUtils.isEmpty(currentTractor.user.fname) && currentTractor.user.fname != null && !TextUtils.isEmpty(
                        currentTractor.user.lname
                    ) && currentTractor.user.lname != null
                ) {
                    holder.binding.tvSeller.text =
                        String.format("%s %s", currentTractor.user.fname, currentTractor.user.lname)
                } else {
                    holder.binding.tvSeller.visibility = View.GONE
                }
                if (currentTractor.user.city != null) {
                    holder.binding.tvCity.visibility = View.VISIBLE
                    holder.binding.tvCity.text = currentTractor.user.city.name
                } else {
                    holder.binding.tvCity.visibility = View.GONE
                }
            } else {
                holder.binding.tvSeller.visibility = View.GONE
                holder.binding.tvCity.visibility = View.GONE
            }
            if (TextUtils.isEmpty(currentTractor.modelMonth) && currentTractor.modelMonth == null && TextUtils.isEmpty(
                    currentTractor.modelYear
                ) && currentTractor.modelYear == null
            ) {
                holder.binding.tvYear.visibility = View.GONE
            } else {
                holder.binding.tvYear.visibility = View.VISIBLE
                holder.binding.tvYear.text =
                    String.format("%s %s", currentTractor.modelMonth, currentTractor.modelYear)
            }
            if (currentTractor.hoursDriven.isEmpty()) {
                holder.binding.tvDriven.visibility = View.GONE
            } else {
                holder.binding.tvDriven.text = currentTractor.hoursDriven
            }
            if (currentTractor.price.isEmpty()) {
                holder.binding.tvPrice.visibility = View.GONE
            } else {
                holder.binding.tvPrice.text = String.format(
                    "%s%s",
                    mContext.getString(R.string.rs),
                    Utils.priceConvert(currentTractor.price)
                )
            }
            if (currentTractor.userViews != null) {
                try {
                    holder.binding.tvViews.text =
                        NumberFormatting.format(currentTractor.userViews.toInt().toLong())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            /* Commented by pranav
            if (currentTractor.isSponsor()) {
                ((ViewHolder) holder).tvSponser.setVisibility(View.VISIBLE);
            } else {
                ((ViewHolder) holder).tvSponser.setVisibility(View.GONE);
            }*/if (currentTractor.image != null) {
                /* if (!TextUtils.isEmpty(currentTractor.getImage().getUsedTractorImage()) && currentTractor.getImage().getUsedTractorImage() != null &&
                        !TextUtils.equals(currentTractor.getImage().getUsedTractorImage(), "null")) {*/
                Glide.with(mContext)
                    .load(Constants.decodeURL(others!!.tractorImagePath + currentTractor.image.usedTractorImage))
                    .error(R.mipmap.no_image)
                    .placeholder(R.mipmap.no_image)
                    .into(holder.binding.ivImage)
                //}
            }
            if (userId != null && currentTractor.user != null) {
                if (!currentTractor.user.id.isEmpty() && userId == currentTractor.user.id) {
                    holder.binding.btnSellerDetails.visibility = View.GONE
                } else if (currentTractor.soldUnsoldStatus != null && currentTractor.soldUnsoldStatus != "0") {
                    holder.binding.btnSellerDetails.visibility = View.GONE
                } else {
                    holder.binding.btnSellerDetails.visibility = View.VISIBLE
                }
            } else {
                holder.binding.btnSellerDetails.visibility = View.GONE
            }
            if (currentTractor.soldUnsoldStatus != null && currentTractor.soldUnsoldStatus != "0") {
                holder.binding.ivSold.visibility = View.VISIBLE
            } else {
                holder.binding.ivSold.visibility = View.GONE
            }
            if (currentTractor.favoritesStatus) {
                holder.binding.ivFavorites.setImageResource(R.drawable.ic_favorites)
            } else {
                holder.binding.ivFavorites.setImageResource(R.drawable.ic_favorites_border)
            }
        } else if (holder is HomeViewHolder) {
            val currentTractor = items[position]!!
            holder.binding.tvTractorName.text =
                String.format("%s %s", currentTractor.brand.name, currentTractor.model.getName())
            if (currentTractor.price.isEmpty()) {
                holder.binding.tvPrice.visibility = View.GONE
            } else {
                holder.binding.tvPrice.text = String.format(
                    "%s%s",
                    mContext.getString(R.string.rs),
                    Utils.priceConvert(currentTractor.price + "")
                )
            }
            if (currentTractor.userViews != null) {
                holder.binding.tvViews.text = NumberFormatting.format(currentTractor.userViews)
            }
            Glide.with(mContext)
                .load(if (currentTractor.image != null) Constants.decodeURL(others!!.tractorImagePath + currentTractor.image.usedTractorImage) else "")
                .error(R.mipmap.no_image)
                .placeholder(R.mipmap.no_image)
                .into(holder.binding.ivImage)
            if (userId != null && currentTractor.user != null) {
                if (currentTractor.user.id.isNotEmpty() && userId == currentTractor.user.id) {
                    holder.binding.btnSellerDetails.visibility = View.GONE
                } else if (currentTractor.soldUnsoldStatus != null && currentTractor.soldUnsoldStatus != "0") {
                    holder.binding.btnSellerDetails.visibility = View.GONE
                } else {
                    holder.binding.btnSellerDetails.visibility = View.VISIBLE
                }
                if (currentTractor.user.city != null) {
                    holder.binding.tvLocation.text = currentTractor.user.city.name
                    holder.binding.tvLocation.visibility = View.VISIBLE
                } else
                    holder.binding.tvLocation.visibility = View.GONE
            } else {
                holder.binding.btnSellerDetails.visibility = View.GONE
            }
            if (currentTractor.soldUnsoldStatus != null && currentTractor.soldUnsoldStatus != "0") {
                holder.binding.ivSold.visibility = View.VISIBLE
            } else {
                holder.binding.ivSold.visibility = View.GONE
            }
            if (currentTractor.hoursDriven != null && !TextUtils.isEmpty(currentTractor.hoursDriven)) {
                holder.binding.tvHrsDriven.text = currentTractor.hoursDriven
                holder.binding.tvHrsDriven.visibility = View.VISIBLE
            } else
                holder.binding.tvHrsDriven.visibility = View.GONE

            if (currentTractor.modelYear != null && !TextUtils.isEmpty(currentTractor.modelYear)) {
                holder.binding.tvYear.text = currentTractor.modelYear
                holder.binding.tvYear.visibility = View.VISIBLE
            } else
                holder.binding.tvYear.visibility = View.GONE

        }
    }

    fun setOnChatClickListener(onChatClickListener: MyClickListener?) {
        this.onChatClickListener = onChatClickListener
    }

    private inner class ViewHolder(val binding: UsedTractorListItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        override fun onClick(v: View) {
            myClickListener!!.onItemClick(adapterPosition, v)
        }

        init {
            binding.root.setOnClickListener(this)
            binding.btnSellerDetails.setOnClickListener { v: View? ->
                myTrashClickListener!!.onTrashClick(
                    adapterPosition,
                    v
                )
            }
            binding.ivFavorites.setOnClickListener { v: View? ->
                myFavoritesClickListener!!.onClick(
                    adapterPosition,
                    v
                )
            }
        }
    }

    private inner class HomeViewHolder(val binding: UsedTractorHomeListItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        override fun onClick(v: View) {
            if (myClickListener != null)
                myClickListener?.onItemClick(adapterPosition, v)
        }

        init {
            binding.root.setOnClickListener(this)
            binding.btnSellerDetails.setOnClickListener { v: View? ->
                if (myTrashClickListener != null)
                    myTrashClickListener?.onTrashClick(adapterPosition, v)
            }
        }
    }

    companion object {
        private const val EMPTY_VIEW = 10
        private const val VIEW_ITEM = 1
        private const val VIEW_PROG = 0
    }
}