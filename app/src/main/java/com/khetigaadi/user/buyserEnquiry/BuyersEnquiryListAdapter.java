package com.khetigaadi.user.buyserEnquiry;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.khetigaadi.R;
//import com.khetigaadi.base.util.DateUtils;
import com.khetigaadi.commons.utils.EmptyViewHolder;
import com.khetigaadi.commons.utils.MyClickListener;
import com.khetigaadi.commons.utils.MyTrashClickListener;
import com.khetigaadi.commons.utils.ProgressViewHolder;
import com.khetigaadi.databinding.BuyersEnqListItemBinding;
import com.khetigaadi.databinding.EmptyViewBinding;
import com.khetigaadi.databinding.ProgressbarItemBinding;
import com.khetigaadi.model.Others;
import com.khetigaadi.tractor.usedTractor.util.UsedTractor;
import com.khetigaadi.user.util.BuyersEnq;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
public class BuyersEnquiryListAdapter extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private final Context mContext;
    private final List<BuyersEnq> mTractors;
    private final Others others;
    private static MyClickListener myClickListener;
    private static final int EMPTY_VIEW = 10;
    private MyTrashClickListener myTrashClickListener;
    public BuyersEnquiryListAdapter(Context mContext, List<BuyersEnq> tractorList, Others others) {
        this.mContext = mContext;
        this.mTractors = tractorList;
        this.others = others;
    }
    public void setOnItemClickListener(MyClickListener myClickListener) {
        BuyersEnquiryListAdapter.myClickListener = myClickListener;
    }
    public void setOnCallClickListener(MyTrashClickListener myTrashClickListener) {
        this.myTrashClickListener = myTrashClickListener;
    }
    @Override
    public int getItemViewType(int position) {
        if (mTractors.size() == 0) {
            return EMPTY_VIEW;
        }
        return mTractors.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == EMPTY_VIEW) {
            return new EmptyViewHolder(EmptyViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else if (viewType == VIEW_ITEM) {
            return new ViewHolder(BuyersEnqListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else {
            return new ProgressViewHolder(mContext, ProgressbarItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    }
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            UsedTractor usedTractor = mTractors.get(position).getUsedTractor();
            if (usedTractor != null) {
                if (usedTractor.getBrand() != null && usedTractor.getModel() != null)
                    ((ViewHolder) holder).binding.tvTractorName.setText(usedTractor.getBrand().getName().concat(" " + usedTractor.getModel().getName()));
                if (usedTractor.getUser() != null) {
                    if (usedTractor.getUser().getFname().isEmpty()) {
                        ((ViewHolder) holder).binding.tvSeller.setVisibility(View.GONE);
                    } else {
                        ((ViewHolder) holder).binding.tvSeller.setVisibility(View.VISIBLE);
                        ((ViewHolder) holder).binding.tvSeller.setText(String.format("%s %s", usedTractor.getUser().getFname(), usedTractor.getUser().getLname()));
                    }
                    if (usedTractor.getUser().getMobile().isEmpty()) {
                        ((ViewHolder) holder).binding.tvMobile.setVisibility(View.GONE);
                    } else {
                        ((ViewHolder) holder).binding.tvMobile.setText(usedTractor.getUser().getMobile());
                    }
                }
                if (mTractors.get(position).getCreatedAt().isEmpty()) {
                    ((ViewHolder) holder).binding.tvDate.setVisibility(View.GONE);
                } else {
                   // ((ViewHolder) holder).binding.tvDate.setText(DateUtils.convertDateToDate(mTractors.get(position).getCreatedAt(),DateUtils.DateFormat.yyyy_MM_dd_HH_mm_ss,DateUtils.DateFormat.dd_MM_yyyy));

                    @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
                    String dateInString = mTractors.get(position).getCreatedAt();
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat formatterOut = new SimpleDateFormat("dd-MM-yyyy");
                    try {
                        Date date = formatter.parse(dateInString);
                        System.out.println(date);
                        assert date != null;
                        String dtt = formatterOut.format(date);
                        ((ViewHolder) holder).binding.tvDate.setText(dtt);
                    }
                    catch(ParseException e)
                    {
                        e.printStackTrace();
                    }
                }

                Glide.with(mContext)
                        .load(others != null && usedTractor.getImage() != null ? others.getTractorImagePath() + usedTractor.getImage().getUsedTractorImage() : null)
                        .placeholder(R.mipmap.no_image)
                        .error(R.mipmap.no_image)
                        .into(((ViewHolder) holder).binding.ivImage);
                Glide.with(mContext)
                        .load(others != null && usedTractor.getBrand() != null ? others.getBrandLogoPath() + usedTractor.getBrand().getImage() : null)
                        .placeholder(R.mipmap.no_image)
                        .error(R.mipmap.no_image)
                        .into(((ViewHolder) holder).binding.brandImage);
            }
        }
    }
    @Override
    public int getItemCount() {
        return mTractors.size() > 0 ? mTractors.size() : 1;
    }
    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final BuyersEnqListItemBinding binding;
        public ViewHolder(@NonNull BuyersEnqListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
            binding.tvMobile.setOnClickListener(v -> myTrashClickListener.onTrashClick(getAbsoluteAdapterPosition(), v));
        }
        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAbsoluteAdapterPosition(), v);
        }
    }
}
