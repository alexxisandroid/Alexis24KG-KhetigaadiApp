package com.khetigaadi.tractor.rent.rentTractorNewUi.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.khetigaadi.databinding.ImplContentBinding;
import com.khetigaadi.tractor.rent.rentTractorNewUi.RentTractorImplementActivity;
import com.khetigaadi.tractor.rent.rentTractorNewUi.model.RentImplement;

import java.util.ArrayList;
import java.util.List;
public class AddImplementAdapter extends RecyclerView.Adapter<AddImplementAdapter.ImplementViewHolder> {
    private final Context mContext;
    private final Listener listener;
    private List<RentImplement> alAddImpl;
    public AddImplementAdapter(Context mContext,Listener listener) {
        this.mContext = mContext;
        this.listener = listener;
        this.alAddImpl = new ArrayList<>();
    }
    @NonNull
    @Override
    public ImplementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImplementViewHolder(ImplContentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull ImplementViewHolder holder, int position) {
        final RentImplement addImplementPojo = alAddImpl.get(position);
        Glide.with(mContext).load( addImplementPojo.getImage()).into(holder.binding.ivImpl);
        holder.binding.tvImplType.setText(addImplementPojo.getType().getName());
        holder.binding.tvImplRent.setText(addImplementPojo.getRentAmt());
        holder.binding.tvImplPer.setText(addImplementPojo.getUnit().getUnitName());
        holder.binding.ibImplRemove.setOnClickListener(v -> {
            listener.onDelete(addImplementPojo);
            alAddImpl.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, alAddImpl.size());
            if (mContext instanceof RentTractorImplementActivity) {
                ((RentTractorImplementActivity) mContext).arraySize(alAddImpl.size());
            }
        });
    }
    @Override
    public int getItemCount() {
        return alAddImpl.size();
    }
    public void setList(List<RentImplement> impList) {
        this.alAddImpl = impList;
        notifyDataSetChanged();
    }
    public static class ImplementViewHolder extends RecyclerView.ViewHolder {
        private ImplContentBinding binding;
        private ImplementViewHolder(@NonNull ImplContentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public interface Listener {
        void onDelete(RentImplement item);
    }
}
