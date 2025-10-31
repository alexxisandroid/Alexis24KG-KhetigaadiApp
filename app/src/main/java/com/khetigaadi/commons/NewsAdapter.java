package com.khetigaadi.commons;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.khetigaadi.R;
import com.khetigaadi.base.util.BaseRecyclerViewAdapter;
import com.khetigaadi.commons.utils.EmptyViewHolder;
import com.khetigaadi.commons.utils.MyClickListener;
import com.khetigaadi.commons.utils.News;
import com.khetigaadi.commons.utils.ProgressViewHolder;
import com.khetigaadi.databinding.EmptyViewBinding;
import com.khetigaadi.databinding.NewsItemBinding;
import com.khetigaadi.databinding.NewsLargeHomeItemBinding;
import com.khetigaadi.implement.newImplement.model.Others;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class NewsAdapter extends BaseRecyclerViewAdapter<News,Others> {
    private static final int EMPTY_VIEW = 10;
    private static final int VIEW_LARGE_ITEM = 2;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private final Context mContext;
    private MyClickListener myClickListener;
    public NewsAdapter(Context mContext, RecyclerView recyclerView) {
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
        } else if (position == 0)
            return VIEW_LARGE_ITEM;
        return items.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }
    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == EMPTY_VIEW) {
            return new EmptyViewHolder(EmptyViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else if (viewType == VIEW_LARGE_ITEM) {
            return new LargeViewHolder(NewsLargeHomeItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else if (viewType == VIEW_ITEM) {
            return new ViewHolder(NewsItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else {
            return new ProgressViewHolder(mContext, LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_for_horizontal_item, parent, false));
        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            News item = getList().get(position);
            ((ViewHolder) holder).binding.tvTitle.setText(item.getPostTitle());
            Glide.with(mContext)
                    .load(item.getImage() != null ? item.getImage().getImage() : null)
                    .placeholder(R.mipmap.no_image)
                    .error(R.mipmap.no_image)
                    .into(((ViewHolder) holder).binding.ivImage);
           // ((ViewHolder) holder).binding.tvDate.setText(DateUtils.convertDateToDate(item.getPostDate(), DateUtils.DateFormat.yyyy_MM_dd_HH_mm_ss, DateUtils.DateFormat.MMM_dd_yyyy));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateInString = item.getPostDate();
            SimpleDateFormat formatterOut = new SimpleDateFormat("MMM dd yyyy");
            try {
                Date date = formatter.parse(dateInString);
                System.out.println(date);
                //System.out.println(formatterOut.format(date));
                assert date != null;
                String dtt = formatterOut.format(date);
                ((ViewHolder) holder).binding.tvDate.setText(dtt);
            } catch(ParseException e) {
                e.printStackTrace();
            }
        } else if (holder instanceof LargeViewHolder) {
            News item = getList().get(position);
            Glide.with(mContext)
                    .load(item.getImage() != null ? item.getImage().getImage() : null)
                    .placeholder(R.mipmap.no_image)
                    .error(R.mipmap.no_image)
                    .into(((LargeViewHolder) holder).binding.ivImage);
        }
    }
    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final NewsItemBinding binding;
        public ViewHolder(@NonNull NewsItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAbsoluteAdapterPosition(), v);
        }
    }
    private class LargeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final NewsLargeHomeItemBinding binding;
        public LargeViewHolder(@NonNull NewsLargeHomeItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAbsoluteAdapterPosition(), v);
        }
    }
}
