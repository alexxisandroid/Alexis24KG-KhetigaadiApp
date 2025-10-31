package com.khetigaadi.news.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.khetigaadi.R;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.commons.utils.EmptyViewHolder;
import com.khetigaadi.commons.utils.OnLoadMoreListener;
import com.khetigaadi.commons.utils.ProgressViewHolder;
import com.khetigaadi.databinding.EmptyViewBinding;
import com.khetigaadi.databinding.ReplyListItemBinding;
import com.khetigaadi.news.model.Reply;
import com.khetigaadi.user.SessionManager;

import java.util.ArrayList;
import java.util.List;
public class ReplyListAdapter extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private int lastVisibleItem, totalItemCount;
    private final Context context;
    private SessionManager sessionManager;
    private ArrayList<Reply> replies;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean loading;
    private final int visibleThreshold = 5;
    private static final int EMPTY_VIEW = 10;
    private ReplyListClickListener commentListClickListener;
    public ReplyListAdapter(Context context, RecyclerView recyclerView, SessionManager sessionManager) {
        this.context = context;
        this.sessionManager = sessionManager;
        replies = new ArrayList<>();
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();
            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition();
                            if (!loading
                                    && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                                // End has been reached
                                // Do something

                                if (onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore();
                                }
                                loading = true;
                            }
                        }
                    });
        }
    }
    @Override
    public int getItemViewType(int position) {
        if (replies.size() == 0) {
            return EMPTY_VIEW;
        }
        return replies.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }
    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == EMPTY_VIEW) {
            return new EmptyViewHolder(EmptyViewBinding.inflate(
                    LayoutInflater.from(parent.getContext()),
                    parent,
                    false
            ));
        } else if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.reply_list_item, parent, false);

            vh = new MyViewHolder(ReplyListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder(context, v);
        }
        return vh;
    }
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            Reply currentPost = replies.get(position);
            if (currentPost.getUser() != null) {
                Glide.with(context).load(sessionManager.getUserOthers().getUserPath() + currentPost.getUser().getImage()).into(((MyViewHolder) holder).binding.civUser);

                ((MyViewHolder) holder).binding.tvUserName.setText(String.format("%s %s", currentPost.getUser().getFname(), currentPost.getUser().getLname()));
            }
            ((MyViewHolder) holder).binding.tvCommment.setText(currentPost.getContent());
            if (currentPost.getCreatedAt() != null)
                ((MyViewHolder) holder).binding.tvCommentTime.setText(Constants.convertTimeAgo(currentPost.getCreatedAt()));

        }
    }
    public void setLoaded() {
        loading = false;
    }
    @Override
    public int getItemCount() {
        return replies.size() > 0 ? replies.size() : 1;
    }
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }
    public void setClickListener(ReplyListClickListener commentListClickListener) {
        this.commentListClickListener = commentListClickListener;
    }
    public void addItems(List<Reply> data) {
        replies.addAll(data);
        notifyDataSetChanged();
    }
    public Reply getItem(int position) {
        return replies.get(position);
    }
    public void updateItem(int position) {
        notifyItemChanged(position);
    }
    public void addItem(int position, Reply data) {
        replies.add(position, data);
        if (replies.size() == 1) {
            notifyDataSetChanged();
        } else {
            notifyItemInserted(position);
        }
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        private final ReplyListItemBinding binding;
        private MyViewHolder(@NonNull ReplyListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.civUser.setOnClickListener(v -> {
                if (commentListClickListener != null)
                    commentListClickListener.onProfileClik(replies.get(getAbsoluteAdapterPosition()), getAbsoluteAdapterPosition(), v);
            });
        }
    }
    public interface ReplyListClickListener {
        void onProfileClik(Reply reply, int adapterPosition, View v);
    }
}
