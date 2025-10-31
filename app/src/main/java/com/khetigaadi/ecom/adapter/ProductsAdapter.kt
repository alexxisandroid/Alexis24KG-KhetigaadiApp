package com.khetigaadi.ecom.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.khetigaadi.R
import com.khetigaadi.base.util.BaseRecyclerViewAdapter
import com.khetigaadi.commons.utils.EmptyViewHolder
import com.khetigaadi.commons.utils.ProgressViewHolder
import com.khetigaadi.databinding.CategoriesListItemBinding
import com.khetigaadi.databinding.CategoriesNewHomeListItemBinding
import com.khetigaadi.databinding.EmptyViewBinding
import com.khetigaadi.ecom.ProductDetailsActivity
import com.khetigaadi.shop.model.Others
import com.khetigaadi.shop.model.Product

class ProductsAdapter(
    private val context: Context,
    recyclerView: RecyclerView?,
    private val isFromHome: Boolean
) :
    BaseRecyclerViewAdapter<Product?, Others?>(
        context, recyclerView
    ) {
    private val VIEW_ITEM = 1
    private val VIEW_PROG = 0
    private var listener: Listener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == EMPTY_VIEW) {
            EmptyViewHolder(
                EmptyViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else if (viewType == VIEW_ITEM) {
            if (isFromHome) HomeViewHolder(
                CategoriesNewHomeListItemBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            ) else ViewHolder(
                CategoriesListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            ProgressViewHolder(
                context,
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.progressbar_for_horizontal_item, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val item = items[position]!!
            Glide.with(context)
                .load(others!!.shopCategoryImagePath + item.image)
                .error(R.mipmap.no_image)
                .placeholder(R.mipmap.no_image)
                .into(holder.binding.image)
//            if (item. != null) holder.binding.image.setBackgroundColor(
//                Color.parseColor(item.colorCode))
            holder.binding.title.text = item.name
            holder.binding.getRoot().setOnClickListener { v ->
                if (listener != null) listener!!.onItemClick(
                    v,
                    position,
                    item
                )
            }
        } else if (holder is HomeViewHolder) {
            val item = items[position]!!
            Glide.with(context)
                .load(others!!.shopCategoryImagePath + item.image)
                .error(R.mipmap.no_image)
                .placeholder(R.mipmap.no_image)
                .into(holder.binding.image)
//            if (item.colorCode != null) holder.binding.image.setBackgroundColor(
//                Color.parseColor(
//                    item.colorCode
//                )
//            )
            holder.binding.title.text = item.name
            holder.binding.getRoot().setOnClickListener { v ->
                if (listener != null) listener!!.onItemClick(
                    v,
                    position,
                    item
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (items.size == 0) {
            return EMPTY_VIEW
        }
        return if (items[position] != null) VIEW_ITEM else VIEW_PROG
    }

    override fun getItemCount(): Int {
        return if (items.size > 0) items.size else 1
    }

    fun setListener(listener: Listener?) {
        this.listener = listener
    }

     class ViewHolder(val binding: CategoriesListItemBinding) :
        RecyclerView.ViewHolder(
            binding.getRoot()
        )

     class HomeViewHolder(val binding: CategoriesNewHomeListItemBinding) :
        RecyclerView.ViewHolder(
            binding.getRoot()
        )

    interface Listener {
    }

    companion object {
        private const val EMPTY_VIEW = 10
    }
}

fun ProductsAdapter.Listener.onItemClick(v: View?, position: Int, item: Product?) {

    val intent = Intent(v!!.context, ProductDetailsActivity::class.java)
    intent.putExtra("product", item)
    intent.putExtra("imagePath", item!!.name)
    v!!.context.startActivity(intent)
}

