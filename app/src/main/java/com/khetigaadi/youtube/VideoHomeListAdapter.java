package com.khetigaadi.youtube;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.khetigaadi.R;
import com.khetigaadi.base.model.youtube.ListItem;
import com.khetigaadi.base.util.BaseRecyclerViewAdapter;
import com.khetigaadi.commons.utils.EmptyViewHolder;
import com.khetigaadi.commons.utils.ProgressViewHolder;
import com.khetigaadi.databinding.EmptyViewBinding;
import com.khetigaadi.databinding.VideoHomeLargeListItemBinding;
import com.khetigaadi.databinding.VideoHomeListItemBinding;
import com.khetigaadi.model.Others;
public class VideoHomeListAdapter extends BaseRecyclerViewAdapter<ListItem,Others> {
    private static final int VIEW_LARGE_ITEM = 2;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private final FragmentActivity context;
    private Listener myClickListener;
    private static final int EMPTY_VIEW = 10;
    public VideoHomeListAdapter(FragmentActivity context, RecyclerView myRecyclerView) {
        super(context, myRecyclerView);
        this.context = context;
    }
    public void setOnItemClickListener(Listener myClickListener) {
        this.myClickListener = myClickListener;
    }
    @Override
    public int getItemViewType(int position) {
        if (items.size() == 0) {
            return EMPTY_VIEW;
        }
       /* } else if (position == 0)
            return VIEW_LARGE_ITEM;*/
        return items.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }
    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == EMPTY_VIEW) {
            return new EmptyViewHolder(EmptyViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else if (viewType == VIEW_ITEM) {
            vh = new ViewHolder(VideoHomeListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else if (viewType == VIEW_LARGE_ITEM) {
            vh = new LargeViewHolder(VideoHomeLargeListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else {
            vh = new ProgressViewHolder(context, LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_item, parent, false));
        }
        return vh;
    }
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ListItem currentTractor = items.get(position);
            ((ViewHolder) holder).binding.tvName.setText(currentTractor.getSnippet().getTitle());
            Glide.with(context).load(currentTractor.getSnippet().getThumbnails().getMedium().getUrl()).into(((ViewHolder) holder).binding.ivVideo);
        } else if (holder instanceof LargeViewHolder) {
           // YouTubePlayerFragmentX gff =
            /*ListItem currentTractor = items.get(position);
            YouTubePlayerFragmentX youTubePlayerFragment = YouTubePlayerFragmentX.newInstance();
            FragmentTransaction transaction = context.getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.youtubeFragment, youTubePlayerFragment).commit();
            youTubePlayerFragment.initialize(context.getString(R.string.google_app_id), new YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
                    if (null == youTubePlayer) return;
                    try {
                        // Start buffering
                        if (!wasRestored) {
                            youTubePlayer.cueVideo(currentTractor.getId().getVideoId());
                            youTubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);
                            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
                            youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                                @Override
                                public void onLoading() {

                                }

                                @Override
                                public void onLoaded(String s) {
                                    youTubePlayer.pause();
                                }

                                @Override
                                public void onAdStarted() {

                                }

                                @Override
                                public void onVideoStarted() {
                                }

                                @Override
                                public void onVideoEnded() {

                                }

                                @Override
                                public void onError(YouTubePlayer.ErrorReason errorReason) {

                                }

                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                }
            });*/
        }
    }
    @Override
    public int getItemCount() {
        return !items.isEmpty() ? items.size() : 1;
    }
    private class ViewHolder extends RecyclerView.ViewHolder {
        private final VideoHomeListItemBinding binding;
        public ViewHolder(@NonNull VideoHomeListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(v -> myClickListener.onItemClick(items.get(getAbsoluteAdapterPosition()), v));
        }
    }
    private class LargeViewHolder extends RecyclerView.ViewHolder {
        private final VideoHomeLargeListItemBinding binding;
        public LargeViewHolder(@NonNull VideoHomeLargeListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(v -> myClickListener.onItemClick(items.get(getAbsoluteAdapterPosition()), v));
        }
    }
    public interface Listener {
        void onItemClick(ListItem item, View v);
    }
}
