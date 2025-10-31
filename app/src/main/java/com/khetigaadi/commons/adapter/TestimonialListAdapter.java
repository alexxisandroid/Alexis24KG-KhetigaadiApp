package com.khetigaadi.commons.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.khetigaadi.R;
import com.khetigaadi.base.util.BaseRecyclerViewAdapter;
import com.khetigaadi.commons.model.Testimonial;
import com.khetigaadi.commons.utils.EmptyViewHolder;
import com.khetigaadi.commons.utils.MyClickListener;
import com.khetigaadi.commons.utils.ProgressViewHolder;
import com.khetigaadi.databinding.EmptyViewBinding;
import com.khetigaadi.databinding.TestimonialListItemBinding;
import com.khetigaadi.implement.newImplement.model.Others;
public class TestimonialListAdapter extends BaseRecyclerViewAdapter<Testimonial, Others> {
    private static final int EMPTY_VIEW = 10;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private final Context mContext;
    private MyClickListener myClickListener;

    public TestimonialListAdapter(Context mContext, RecyclerView recyclerView) {
        super(mContext, recyclerView);
        this.mContext = mContext;
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (items.size() == 0) {
            return EMPTY_VIEW;
        }
        return items.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == EMPTY_VIEW) {
            return new EmptyViewHolder(EmptyViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else if (viewType == VIEW_ITEM) {
            return new ViewHolder(TestimonialListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else {
            return new ProgressViewHolder(mContext, LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_for_horizontal_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            Testimonial item = getList().get(position);
            ((ViewHolder) holder).binding.tvTitle.setText(item.getDescription());
            Glide.with(mContext)
                    .load(others != null && item.getImage() != null ? others.getTestimonialsImagePath() + item.getImage() : null)
                    .placeholder(R.mipmap.no_image)
                    .error(R.mipmap.no_image)
                    .into(((ViewHolder) holder).binding.ivImage);
            ((ViewHolder) holder).binding.tvUserName.setText(String.format("- %s", item.getCustomerName()));
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TestimonialListItemBinding binding;

        public ViewHolder(@NonNull TestimonialListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (myClickListener != null)
                myClickListener.onItemClick(getAbsoluteAdapterPosition(), v);
        }
    }


}
