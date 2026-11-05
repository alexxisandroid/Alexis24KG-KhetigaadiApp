package com.khetigaadi.news.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.khetigaadi.R;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.commons.utils.EmptyViewHolder;
import com.khetigaadi.commons.utils.OnLoadMoreListener;
import com.khetigaadi.commons.utils.ProgressViewHolder;
import com.khetigaadi.databinding.NewsCommentBinding;
import com.khetigaadi.news.model.Comment;
import com.khetigaadi.user.SessionManager;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
public class CommentListAdapter extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private int lastVisibleItem, totalItemCount;
    private final Context context;
    private final SessionManager sessionManager;
    private final ArrayList<Comment> posts;
    private final RecyclerView recyclerView;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean loading;
    private final int visibleThreshold = 5;
    private static final int EMPTY_VIEW = 10;
    private CommentListClickListener commentListClickListener;
    public CommentListAdapter(Context context,RecyclerView recyclerView,SessionManager sessionManager) {
        this.context = context;
        this.sessionManager = sessionManager;
        posts = new ArrayList<>();
        this.recyclerView = recyclerView;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView,dx,dy);
                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                            if(!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                                // End has been reached
                                // Do something
                                if(onLoadMoreListener != null) {
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
        if (posts.size() == 0) {
            return EMPTY_VIEW;
        }
        return posts.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }
    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent,int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == EMPTY_VIEW) {
            return new EmptyViewHolder(new View(parent.getContext()));
        } else if (viewType == VIEW_ITEM) {
            vh = new MyViewHolder(NewsCommentBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_item,parent,false);
            vh = new ProgressViewHolder(context,v);
        }
        return vh;
    }
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder,int position) {
        if(holder instanceof MyViewHolder) {
            Comment currentPost = posts.get(position);
            if(currentPost.getUser() != null) {
                Glide.with(context)
                        .load(sessionManager.getUserOthers().getUserPath()+currentPost.getUser().getImage())
                        .error(R.drawable.ic_user_profile)
                        .placeholder(R.drawable.ic_user_profile)
                        .into(((MyViewHolder) holder).binding.civUser);
            }
            ((MyViewHolder) holder).binding.tvUserName.setText(currentPost.getUser().getFname().concat(" " + currentPost.getUser().getLname()));
            ((MyViewHolder) holder).binding.tvCommment.setText(currentPost.getComment());
            if (currentPost.isLike()) {
                ((MyViewHolder) holder).binding.tvCommentLike.setTextColor(ContextCompat.getColor(context, R.color.darkGrey));
                ((MyViewHolder) holder).binding.tvLikeCount.setTextColor(ContextCompat.getColor(context, R.color.darkGrey));
            } else {
                ((MyViewHolder) holder).binding.tvCommentLike.setTextColor(ContextCompat.getColor(context, R.color.ValueGrey));
                ((MyViewHolder) holder).binding.tvLikeCount.setTextColor(ContextCompat.getColor(context, R.color.ValueGrey));
            }
            if (currentPost.getPostedDate() != null)
                ((MyViewHolder) holder).binding.tvCommentTime.setText(Constants.convertTimeAgo(currentPost.getPostedDate()));
            if (currentPost.getLikes() != null)
                ((MyViewHolder) holder).binding.tvLikeCount.setText(String.valueOf(currentPost.getLikes()));
            if (currentPost.getReplies() != null)
                ((MyViewHolder) holder).binding.tvReplyCount.setText(String.valueOf(currentPost.getReplies()));
        }
    }
    public void setLoaded() {
        loading = false;
    }
    @Override
    public int getItemCount() {
        return posts.size() > 0 ? posts.size() : 1;
    }
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }
    public void setClickListener(CommentListClickListener commentListClickListener) {
        this.commentListClickListener = commentListClickListener;
    }
    public void addItems(List<Comment> data) {
        posts.addAll(data);
        notifyDataSetChanged();
    }
    public Comment getItem(int position) {
        return posts.get(position);
    }
    public void updateItem(int position) {
        notifyItemChanged(position);
    }
    public void addItem(int position,Comment data) {
        posts.add(position, data);
        if(posts.size() == 1) {
            notifyDataSetChanged();
        }
        else {
            notifyItemInserted(position);
        }
    }
    public void removeAll() {
        posts.clear();
        notifyDataSetChanged();
    }
    public ArrayList<Comment> getAllItems() {
        return posts;
    }
    public void removeItem(int adapterPosition) {
        posts.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
    }
    public void updateItem(int position,Comment comment) {
        posts.set(position, comment);
        notifyItemChanged(position);
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        private final NewsCommentBinding binding;
        private MyViewHolder(@NonNull NewsCommentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.civUser.setOnClickListener(v -> {
                if (commentListClickListener != null)
                    commentListClickListener.onProfileClik(posts.get(getAbsoluteAdapterPosition()), getAbsoluteAdapterPosition(), v);
            });
            binding.tvLikeCount.setOnClickListener(v -> {
                if (commentListClickListener != null)
                    commentListClickListener.onLikeClick(posts.get(getAbsoluteAdapterPosition()), getAbsoluteAdapterPosition(), v);
            });

            binding.tvCommentLike.setOnClickListener(v -> {
                if (commentListClickListener != null)
                    commentListClickListener.onLikeClick(posts.get(getAbsoluteAdapterPosition()), getAbsoluteAdapterPosition(), v);
            });
            binding.tvReplyCount.setOnClickListener(v -> {
                if (commentListClickListener != null)
                    commentListClickListener.onReplyClick(posts.get(getAbsoluteAdapterPosition()), getAbsoluteAdapterPosition(), v);
            });
            binding.tvReply.setOnClickListener(v -> {
                if (commentListClickListener != null)
                    commentListClickListener.onReplyClick(posts.get(getAbsoluteAdapterPosition()), getAbsoluteAdapterPosition(), v);
            });


        }
    }
    public interface CommentListClickListener {
        void onItemClick(Comment post, int adapterPosition, View v);
        void onLikeClick(Comment post, int adapterPosition, View v);
        void onProfileClik(Comment post, int adapterPosition, View v);
        void onReplyClick(Comment comment, int adapterPosition, View v);
    }
}
