package com.khetigaadi.user.myfavorites;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.khetigaadi.R;
import com.khetigaadi.base.util.BaseRecyclerViewAdapter;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.commons.utils.EmptyViewHolder;
import com.khetigaadi.commons.utils.MyClickListener;
import com.khetigaadi.commons.utils.MyFavoritesClickListener;
import com.khetigaadi.commons.utils.MySoldClickListener;
import com.khetigaadi.commons.utils.ProgressViewHolder;
import com.khetigaadi.databinding.EmptyViewBinding;
import com.khetigaadi.databinding.MyFavRentTrListBinding;
import com.khetigaadi.databinding.RentTrListItemBinding;
import com.khetigaadi.tractor.rent.model.RentTractor;
import com.khetigaadi.user.model.User;
import com.khetigaadi.user.myfavorites.model.MyFavRentTractors;
import com.khetigaadi.user.myfavorites.model.Others;
import com.khetigaadi.util.NumberFormatting;
import com.khetigaadi.util.Utils;

import org.jetbrains.annotations.NotNull;
public class MyFavRentTrListAdapter extends BaseRecyclerViewAdapter<MyFavRentTractors,Others> {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private final Context mContext;
    private final User user;
    private static MyClickListener myClickListener;
    private static final int EMPTY_VIEW = 10;
    private MySoldClickListener mySoldClickListener;
    private MyFavoritesClickListener myFavoritesClickListener;
    public MyFavRentTrListAdapter(Context mContext, User user, RecyclerView recyclerView) {
        super(mContext, recyclerView);
        this.mContext = mContext;
        this.user = user;

    }
    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }
    public void setOnSendEnqClickListener(MySoldClickListener mySoldClickListener) {
        this.mySoldClickListener = mySoldClickListener;
    }
    public void setOnFavoritesClick(MyFavoritesClickListener myFavoritesClickListener) {
        this.myFavoritesClickListener = myFavoritesClickListener;
    }
    @Override
    public int getItemViewType(int position) {
        if (items.size() == 0) {
            return EMPTY_VIEW;
        }
        return items.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }
    @Override
    public RecyclerView.@NotNull ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == EMPTY_VIEW) {
            return new EmptyViewHolder(EmptyViewBinding.inflate(
                    LayoutInflater.from(parent.getContext()),
                    parent,
                    false
            ));
        } else if (viewType == VIEW_ITEM) {
            vh = new ViewHolder(MyFavRentTrListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder(mContext, v);
        }
        return vh;
    }
    @Override
    public void onBindViewHolder(final RecyclerView.@NotNull ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            RentTractor item = items.get(position).getRentTractor();
            if (item.getRentTractorTable() != null && item.getRentTractorTable().getTractorName() != null)
                ((ViewHolder) holder).binding.tvNewTractorName.setText(item.getRentTractorTable().getTractorName());
            if (item.getUser() != null) {
                if (item.getUser().getFname().isEmpty() || item.getUser().getLname().trim().equalsIgnoreCase("")) {
                    ((ViewHolder) holder).binding.tvOwnerName.setVisibility(View.GONE);
                } else {
                    ((ViewHolder) holder).binding.tvOwnerName.setText(String.format("%s %s", item.getUser().getFname(), item.getUser().getLname()));
                }
                if (item.getUser().getCity().getName().isEmpty() || item.getUser().getCity().getName().trim().equalsIgnoreCase("")) {
                    ((ViewHolder) holder).binding.tvCity.setVisibility(View.GONE);
                } else {
                    ((ViewHolder) holder).binding.tvCity.setText(item.getUser().getCity().getName());
                }
                if (!item.getUser().getId().isEmpty() && user.getId().equals(item.getUser().getId())) {
                    ((ViewHolder) holder).binding.btnSendEnq.setVisibility(View.GONE);
                } else {
                    ((ViewHolder) holder).binding.btnSendEnq.setVisibility(View.VISIBLE);
                }
            }
            if (item.getDrive() == null) {
                ((ViewHolder) holder).binding.ivDrive.setVisibility(View.GONE);
            } else {
                ((ViewHolder) holder).binding.ivDrive.setVisibility(View.VISIBLE);
                if (item.getDrive().getId() == 1) {
                    ((ViewHolder) holder).binding.ivDrive.setImageResource(R.drawable.ic_2wd);
                } else {
                    ((ViewHolder) holder).binding.ivDrive.setImageResource(R.drawable.ic_4wd);
                }
            }
            if (item.getExactHp() == null || item.getExactHp().isEmpty() || item.getExactHp().trim().equalsIgnoreCase("") || item.getExactHp().equals("0")) {
                ((ViewHolder) holder).binding.tvHP.setVisibility(View.GONE);
            } else {
                ((ViewHolder) holder).binding.tvHP.setText(item.getExactHp());
            }
            if (item.getRentTractorImplement() == null) {
                ((ViewHolder) holder).binding.tvPrice.setVisibility(View.GONE);
            } else {
                ((ViewHolder) holder).binding.tvPrice.setText(Utils.priceConvert(item.getRentTractorImplement().getRate()));
            }
            if (item.getRentTractorTable() != null && item.getRentTractorTable().getTractorImage() != null)
                Glide.with(mContext)
                        .load(others.getNewTractorImagePath() + item.getRentTractorTable().getTractorImage().getTractor_img_name())
                        .error(R.mipmap.no_image)
                        .placeholder(R.mipmap.no_image)
                        .into(((ViewHolder) holder).binding.ivNewTractorImage);
            if (item.isBookmark()) {
                ((ViewHolder) holder).binding.ivFavorites.setImageResource(R.drawable.ic_favorites);
            } else {
                ((ViewHolder) holder).binding.ivFavorites.setImageResource(R.drawable.ic_favorites_border);
            }
            if (item.getUserViews() == 0) {
                ((ViewHolder) holder).binding.tvViews.setVisibility(View.GONE);
            } else {
                ((ViewHolder) holder).binding.tvViews.setText(NumberFormatting.format(item.getUserViews()));
            }
        }
    }
    public void setLoaded() {
        loading = false;
    }
    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size() > 0 ? items.size() : 1;
        }
        return 0;
    }
    private class ViewHolder extends RecyclerView.ViewHolder {
        private final MyFavRentTrListBinding binding;
        public ViewHolder(@NonNull MyFavRentTrListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(v -> myClickListener.onItemClick(getAbsoluteAdapterPosition(), v));
            binding.btnSendEnq.setOnClickListener(view -> mySoldClickListener.onSoldClick(getAbsoluteAdapterPosition(), view));
            binding.ivFavorites.setOnClickListener(v -> myFavoritesClickListener.onClick(getAbsoluteAdapterPosition(), v));
        }
    }
}
