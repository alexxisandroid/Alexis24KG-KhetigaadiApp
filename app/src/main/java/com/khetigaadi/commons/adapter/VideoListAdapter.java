package com.khetigaadi.commons.adapter;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.khetigaadi.R;
import com.khetigaadi.base.model.youtube.ListItem;
import com.khetigaadi.base.util.BaseRecyclerViewAdapter;
import com.khetigaadi.commons.utils.EmptyViewHolder;
import com.khetigaadi.commons.utils.MyClickListener;
import com.khetigaadi.commons.utils.ProgressViewHolder;
import com.khetigaadi.databinding.EmptyViewBinding;
import com.khetigaadi.databinding.VideoListItemBinding;
import com.khetigaadi.model.Others;
public class VideoListAdapter extends BaseRecyclerViewAdapter<ListItem,Others> {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private final Context context;
    private MyClickListener myClickListener;
    private static final int EMPTY_VIEW = 10;
    public VideoListAdapter(Context context,RecyclerView myRecyclerView) {
        super(context,myRecyclerView);
        this.context = context;
    }
    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }
    @Override
    public int getItemViewType(int position) {
        if(items.size() == 0) {
            return EMPTY_VIEW;
        }
        return items.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }
    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType) {
        RecyclerView.ViewHolder vh;
        if(viewType == EMPTY_VIEW) {
            return new EmptyViewHolder(EmptyViewBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        }
        else if(viewType == VIEW_ITEM) {
            vh = new ViewHolder(VideoListItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        }
        else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_item,parent,false);
            vh = new ProgressViewHolder(context,v);
        }
        return vh;
    }
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder,int position) {
        if(holder instanceof ViewHolder) {
            ListItem currentTractor = items.get(position);
            ((ViewHolder) holder).binding.ccvvv.setBackgroundColor(Color.WHITE);

            ((ViewHolder) holder).binding.tvName.setText(Html.fromHtml(currentTractor.getSnippet().getTitle()));
            Glide.with(context).load(currentTractor.getSnippet().getThumbnails().getMedium().getUrl()).into(((ViewHolder)holder).binding.ivVideo);
        }
    }
    @Override
    public int getItemCount() {
        return items.size() > 0 ? items.size() : 1;
    }
    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final com.khetigaadi.databinding.VideoListItemBinding binding;
        public ViewHolder(@NonNull VideoListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAbsoluteAdapterPosition(),v);
        }
    }
}
