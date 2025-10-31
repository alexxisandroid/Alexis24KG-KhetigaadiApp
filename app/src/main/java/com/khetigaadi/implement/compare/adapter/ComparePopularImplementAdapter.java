package com.khetigaadi.implement.compare.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.khetigaadi.R;
import com.khetigaadi.base.util.BaseRecyclerViewAdapter;
import com.khetigaadi.commons.SharedPreferenceUtils;
import com.khetigaadi.commons.utils.EmptyViewHolder;
import com.khetigaadi.commons.utils.MyClickListener;
import com.khetigaadi.commons.utils.OnLoadMoreListener;
import com.khetigaadi.commons.utils.ProgressViewHolder;
import com.khetigaadi.databinding.CompareTrListItemBinding;
import com.khetigaadi.databinding.EmptyViewBinding;
import com.khetigaadi.implement.compare.util.PopularImpComp;
import com.khetigaadi.implement.newImplement.model.Others;
import com.khetigaadi.user.myfavorites.model.MyFavRentTractors;

import java.util.List;
import java.util.Objects;
public class ComparePopularImplementAdapter extends BaseRecyclerViewAdapter<PopularImpComp,Others> {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private final Context mContext;
    private static MyClickListener myClickListener;
    private final int EMPTY_VIEW = 10;
    public ComparePopularImplementAdapter(Context mContext, RecyclerView recyclerView) {
        super(mContext, recyclerView);
        this.mContext = mContext;
    }
    public void setOnItemClickListener(MyClickListener myClickListener) {
        ComparePopularImplementAdapter.myClickListener = myClickListener;
    }
    @Override
    public int getItemViewType(int position) {
        if (items.size() == 0) {
            return EMPTY_VIEW;
        }
        return items.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == EMPTY_VIEW) {
            return new EmptyViewHolder(EmptyViewBinding.inflate(
                    LayoutInflater.from(parent.getContext()),
                    parent,
                    false
            ));
        } else if (viewType == VIEW_ITEM) {
            vh = new ViewHolder(CompareTrListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);
            vh = new ProgressViewHolder(mContext, v);
        }
        return vh;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            PopularImpComp popularImpComp = getList().get(position);
            try {
                ((ViewHolder) holder).binding.tvTractorName.setText(popularImpComp.getFirstImplement().getImplName());
                ((ViewHolder) holder).binding.tvTractorName1.setText(popularImpComp.getSecondImplement().getImplName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Glide.with(mContext).load(others.getImplementImagePath() + popularImpComp.getFirstImplement().getImplImage().getImage())
                    .placeholder(R.mipmap.no_image)
                    .error(R.mipmap.no_image)
                    .into(((ViewHolder) holder).binding.ivTractor);
            // }
            Glide.with(mContext).load(others.getImplementImagePath() + popularImpComp.getSecondImplement().getImplImage().getImage()/*Constants.decodeURL(currentnews.getImage())*/)
                    .placeholder(R.mipmap.no_image)
                    .error(R.mipmap.no_image)
                    .into(((ViewHolder) holder).binding.ivTractor1);
            //}
        }
    }
    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size() > 0 ? items.size() : 1;
        }
        return 0;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final com.khetigaadi.databinding.CompareTrListItemBinding binding;
        public ViewHolder(@NonNull CompareTrListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(view -> myClickListener.onItemClick(getAbsoluteAdapterPosition(), view));

        }
    }
}
