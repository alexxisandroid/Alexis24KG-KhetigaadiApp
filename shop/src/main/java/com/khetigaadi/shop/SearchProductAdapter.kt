package com.khetigaadi.shop
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.khetigaadi.base.databinding.EmptyViewLayoutBinding
import com.khetigaadi.base.databinding.SearchListItemBinding
import com.khetigaadi.shop.model.Others
import com.khetigaadi.shop.model.Product
open class SearchProductAdapter(val context:Context,private val mDataset:List<Product>,val others:Others) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
private lateinit var myClickListener:Listener
        fun setOnItemClickListener(myClickListener:Listener) {
        this.myClickListener = myClickListener
        }
        override fun getItemViewType(position: Int): Int {
        return if (mDataset.size == 0) EMPTY_VIEW else VIEW_ITEM
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == EMPTY_VIEW) {
             EmptyViewHolder(EmptyViewLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }
        else {
             DataObjectHolder(SearchListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }
        }
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DataObjectHolder) {
        val product: Product = mDataset[position]
        holder.binding.serchRes.text = product.name
        Glide.with(context)
        .load(others.shopProductImagePath + product.image.image)
        .into(holder.binding.ivImage)
        holder.binding.root.setOnClickListener {
        myClickListener.onItemClick(product)
        }
        }
        }
        override fun getItemCount(): Int {
        return if (mDataset.size > 0) mDataset.size else 1
        }
        internal inner class DataObjectHolder(binding:SearchListItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        val binding: SearchListItemBinding
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
    fun onItemClick(product: Product)
}
}


//package com.khetigaadi.shop;
//import android.content.Context;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Filter;
//import android.widget.Filterable;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import com.khetigaadi.shop.model.search.Others;
////import com.khetigaadi.
//import com.khetigaadi.shop.model.Product;
//import org.jetbrains.annotations.NotNull;
//import java.util.ArrayList;
//import java.util.List;
//public class SearchProductAdapter extends RecyclerView.Adapter<SearchProductAdapter.MyViewHolder> implements Filterable {
//    private List<Product> filteredData = null;
//    private List<Product> mModelList;
//    private ItemFilter mFilter = new ItemFilter();
//    private OnClickListener onClickListener;
//    public SearchProductAdapter(Context context,List<Product> product,Others others) {
//
//    }
//    public void setOnClickListener(OnClickListener onClickListener) {
//        this.onClickListener = onClickListener;
//    }
//    public void setOnItemClickListener(@NotNull Object any) {
//
//    }
//    @Override
//    public Filter getFilter() {
//        return null;
//    }
//    @NonNull
//    @Override
//    public SearchProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType) {
//        return null;
//    }
//    @Override
//    public void onBindViewHolder(@NonNull SearchProductAdapter.MyViewHolder holder,int position) {
//        final Product model = filteredData.get(position);
//        holder.view.setText(model.getName());
//        holder.view.setOnClickListener(view -> {
//            if (onClickListener != null)
//                onClickListener.onClick(model);
//        });
//    }
//    @Override
//    public int getItemCount() {
//        return filteredData == null ? 0:filteredData.size();
//    }
//    public static class MyViewHolder extends RecyclerView.ViewHolder {
//        private final TextView view;
//
//        private MyViewHolder(View itemView) {
//            super(itemView);
//            view = (TextView) itemView;
//        }
//    }
//    private class ItemFilter extends Filter {
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            String filterString = constraint.toString().toLowerCase();
//            FilterResults results = new FilterResults();
//            final List<Product> list = mModelList;
//            int count = list.size();
//            final ArrayList<Product> nlist = new ArrayList<>(count);
//            Product filterableString;
//            for (int i = 0; i < count; i++) {
//                filterableString = list.get(i);
//                if (filterableString.getName().toLowerCase().contains(filterString)) {
//                    nlist.add(filterableString);
//                }
//            }
//            results.values = nlist;
//            results.count = nlist.size();
//            return results;
//        }
//        @Override
//        protected void publishResults(CharSequence constraint,FilterResults results) {
//            filteredData = (ArrayList<Product>) results.values;
//            notifyDataSetChanged();
//        }
//    }
//    public interface OnClickListener {
//        void onClick(Product brand);
//    }
//    interface Listener  {
//        public void onItemClick(Product product);
//    }
//}
