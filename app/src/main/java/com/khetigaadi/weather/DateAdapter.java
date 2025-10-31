package com.khetigaadi.weather;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.khetigaadi.R;
import com.khetigaadi.databinding.DateItemBinding;
import com.khetigaadi.databinding.SelectedDateItemBinding;
import com.khetigaadi.weather.model.ForecastDay;
import org.jetbrains.annotations.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
/** @noinspection rawtypes*/
public class DateAdapter extends RecyclerView.Adapter {
    private static final int VIEW_ITEM = 0;
    private static final int VIEW_SELECTED = 1;
    private final Context mContext;
    private final List<ForecastDay> myDatas;
    private final RecyclerView rvDate;
    private OnClickListener onClickListener;
    // Constructor
    DateAdapter(Context context,List<ForecastDay> myDatas,RecyclerView rvDate) {
        mContext = context;
        this.myDatas = myDatas;
        this.rvDate = rvDate;
    }
    @Override
    public int getItemViewType(int position) {
        return myDatas.get(position).isSelected() ? VIEW_SELECTED : VIEW_ITEM;
    }
    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_SELECTED) {
            return new SelectedViewHolder(SelectedDateItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else {
            return new ViewHolder(DateItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    }
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        ForecastDay data = myDatas.get(position);
        if (holder instanceof DateAdapter.ViewHolder) {
            holder.itemView.setMinimumWidth(Math.round((float) rvDate.getWidth() / myDatas.size()));
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String dateInString = data.getDate();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatterOut = new SimpleDateFormat("EE");
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatterOut2 = new SimpleDateFormat("dd");
            try {
                Date date = formatter.parse(dateInString);
                System.out.println(date);
                //System.out.println(formatterOut.format(date));
                assert date != null;
                String dtt = formatterOut.format(date);
                String dtt2 = formatterOut2.format(date);
                ((ViewHolder) holder).binding.tvDayName.setText(dtt);
                ((ViewHolder) holder).binding.tvDate.setText(dtt2);
              //  ((ViewHolder) holder).binding.tvDayName.setText(DateUtils.convertDateToDate(data.getDate(), DateUtils.DateFormat.yyyy_MM_dd, DateUtils.DateFormat.EE));
            } catch(ParseException e) {
                //noinspection CallToPrintStackTrace
                e.printStackTrace();
            }
            Glide.with(mContext)
                    .load("https:" + data.getDay().getCondition().getIcon())
                    .error(R.mipmap.no_image)
                    .placeholder(R.mipmap.no_image).
                    into(((ViewHolder) holder).binding.ivImage);
        }
        else {
            holder.itemView.setMinimumWidth(Math.round((float) rvDate.getWidth() / myDatas.size()));
           // ((SelectedViewHolder) holder).binding.tvDayName.setText(DateUtils.convertDateToDate(data.getDate(), DateUtils.DateFormat.yyyy_MM_dd, DateUtils.DateFormat.EE));
           // ((SelectedViewHolder) holder).binding.tvDate.setText(DateUtils.convertDateToDate(data.getDate(), DateUtils.DateFormat.yyyy_MM_dd, DateUtils.DateFormat.dd));

            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String dateInString = data.getDate();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatterOut = new SimpleDateFormat("EE");
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatterOut2 = new SimpleDateFormat("dd");
            try {
                Date date = formatter.parse(dateInString);
                System.out.println(date);
                //System.out.println(formatterOut.format(date));
                assert date != null;
                String dtt = formatterOut.format(date);
                String dtt2 = formatterOut2.format(date);
               // ((ViewHolder)holder).binding.tvDayName.setText(dtt);
               if(holder instanceof  SelectedViewHolder)
                ((SelectedViewHolder) holder).binding.tvDate.setText(dtt2);

               if(holder instanceof  SelectedViewHolder)
                 ((SelectedViewHolder) holder).binding.tvDayName.setText(dtt);
                //  ((ViewHolder)holder).binding.tvDayName.setText(DateUtils.convertDateToDate(data.getDate(), DateUtils.DateFormat.yyyy_MM_dd, DateUtils.DateFormat.EE));
            }
            catch(ParseException e) {
                //noinspection CallToPrintStackTrace
                e.printStackTrace();
            }
        }
    }
    @Override
    public int getItemCount() {
        return myDatas.size();
    }
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
    private class ViewHolder extends RecyclerView.ViewHolder {
        private final DateItemBinding binding;
        public ViewHolder(DateItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(v -> onClickListener.onClick(getAbsoluteAdapterPosition()));
        }
    }
    private static class SelectedViewHolder extends RecyclerView.ViewHolder {
        private final SelectedDateItemBinding binding;
        public SelectedViewHolder(@NonNull SelectedDateItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public interface OnClickListener {
        void onClick(int adapterPosition);
    }
}