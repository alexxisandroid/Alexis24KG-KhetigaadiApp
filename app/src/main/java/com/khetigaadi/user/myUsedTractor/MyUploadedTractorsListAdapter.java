package com.khetigaadi.user.myUsedTractor;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
//import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.khetigaadi.AppConfig;
import com.khetigaadi.R;
import com.khetigaadi.base.util.BaseRecyclerViewAdapter;
//import com.khetigaadi.base.util.DateUtils;
//import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.commons.utils.EmptyViewHolder;
import com.khetigaadi.commons.utils.IntrestedBuyersListner;
import com.khetigaadi.commons.utils.MyClickListener;
import com.khetigaadi.commons.utils.MySoldClickListener;
import com.khetigaadi.commons.utils.MyTrashClickListener;
import com.khetigaadi.commons.utils.ProgressViewHolder;
import com.khetigaadi.databinding.EmptyViewBinding;
import com.khetigaadi.databinding.IntrestedBuyersListItemBinding;
import com.khetigaadi.databinding.MyuploadedUsedTractorListItemBinding;
import com.khetigaadi.model.Others;
import com.khetigaadi.user.myUsedTractor.model.InterestedBuyersData;
import com.khetigaadi.user.util.MyUsedTractor;
import com.khetigaadi.util.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
public class MyUploadedTractorsListAdapter extends BaseRecyclerViewAdapter<MyUsedTractor,Others> {
    private final int VIEW_ITEM = 1;
    private final Context context;
    private static MyClickListener myClickListener;
    private static MyTrashClickListener myTrashClickListener;
    private static final int EMPTY_VIEW = 10;
    private MySoldClickListener soldClickListener;
    private IntrestedBuyersListner intrestedClickListener;
    private MyTrashClickListener myChatClickListener;
    public MyUploadedTractorsListAdapter(Context context,RecyclerView recyclerView) {
        super(context, recyclerView);
        this.context = context;
    }
    public void setOnInterestedBuyersClickListener(IntrestedBuyersListner interestedClickListener) {
        this.intrestedClickListener = interestedClickListener;
    }
    public void setOnItemClickListener(MyClickListener myClickListener) {
        MyUploadedTractorsListAdapter.myClickListener = myClickListener;
    }
    public void setOnTrashClickListener(MyTrashClickListener myTrashClickListener) {
        MyUploadedTractorsListAdapter.myTrashClickListener = myTrashClickListener;
    }
    public void setOnSoldClickListener(MySoldClickListener soldClickListener) {
        this.soldClickListener = soldClickListener;
    }
    @Override
    public int getItemViewType(int position) {
        if (items.isEmpty()) {
            return EMPTY_VIEW;
        }
        int VIEW_PROG = 0;
        return items.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == EMPTY_VIEW) {
            return new EmptyViewHolder(EmptyViewBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        } else if (viewType == VIEW_ITEM) {
            vh = new ViewHolder(MyuploadedUsedTractorListItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        } else {
            vh = new ProgressViewHolder(context, LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_item,parent,false));
        }
        return vh;
    }
    @SuppressWarnings("CallToPrintStackTrace")
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,int position) {
        if (holder instanceof ViewHolder) {
            MyUsedTractor currentTractor = items.get(position);
            ((ViewHolder) holder).binding.tvTractorName.setText(String.format("%s %s", currentTractor.getBrand().getName(), currentTractor.getModel().getName()));

            if (currentTractor.getUser() != null) {
                ((ViewHolder) holder).binding.tvSeller.setVisibility(View.GONE);
            } else {
                ((ViewHolder) holder).binding.tvSeller.setVisibility(View.VISIBLE);
                ((ViewHolder) holder).binding.tvSeller.setText(String.format("%s %s", currentTractor.getUser().getFname(), currentTractor.getUser().getLname()));
            }
            if (currentTractor.getUser() != null && currentTractor.getUser().getCity() != null && currentTractor.getUser().getCity().getName() != null) {
                ((ViewHolder) holder).binding.tvCity.setVisibility(View.GONE);
            } else {
                ((ViewHolder) holder).binding.tvCity.setVisibility(View.VISIBLE);
                ((ViewHolder) holder).binding.tvCity.setText(currentTractor.getUser().getCity().getName());
            }
            if (currentTractor.getModelYear().isEmpty()) {
                ((ViewHolder) holder).binding.tvYear.setVisibility(View.GONE);
            }
            else {
                ((ViewHolder) holder).binding.tvYear.setVisibility(View.VISIBLE);
                ((ViewHolder) holder).binding.tvYear.setText(currentTractor.getModelYear());
            }
            if (currentTractor.getHoursDriven().isEmpty()) {
                ((ViewHolder) holder).binding.tvDriven.setVisibility(View.GONE);
            }
            else {
                ((ViewHolder) holder).binding.tvDriven.setVisibility(View.VISIBLE);
                ((ViewHolder) holder).binding.tvDriven.setText(currentTractor.getHoursDriven());
            }
            if (currentTractor.getPrice().isEmpty()) {
                ((ViewHolder) holder).binding.tvPrice.setVisibility(View.GONE);
            }
            else {
                ((ViewHolder) holder).binding.tvPrice.setVisibility(View.VISIBLE);
                ((ViewHolder) holder).binding.tvPrice.setText(context.getResources().getString(R.string.rs)+" "+Utils.priceConvert(currentTractor.getPrice()));
            }
            ((ViewHolder) holder).binding.tvViews.setText(currentTractor.getUserViews());
            try {
                if(Integer.parseInt(currentTractor.getSoldUnsoldStatus()) == 0) {
                    ((ViewHolder) holder).binding.btnSoldStatus.setText(context.getString(R.string.click_to_sold));
                } else {
                    ((ViewHolder) holder).binding.btnSoldStatus.setText(context.getString(R.string.click_to_unsold));

                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            ((ViewHolder) holder).binding.pbSold.setVisibility(View.GONE);
            ((ViewHolder) holder).binding.btnSoldStatus.setVisibility(View.VISIBLE);
           String bPath = AppConfig.BASE_ASSET_URL+"used_tractor/";  //others.getTractorImagePath()
            Glide.with(context)
                    .load(currentTractor.getImage() != null ? getImagePath(bPath, currentTractor.getImage().getUsedTractorImage()) : "")
                    .placeholder(R.mipmap.no_image)
                    .error(R.mipmap.no_image)
                    .into(((ViewHolder) holder).binding.ivImage);

            if(!currentTractor.getBuyersCount().isEmpty()) {
                ((ViewHolder) holder).binding.tvIntrestedBuyersCount.setText(currentTractor.getBuyersCount());
            }
            if(currentTractor.getInterestedBuyers() != null && !currentTractor.getBuyersCount().equals("0")) {
                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                ((ViewHolder) holder).binding.llIntrestedBuyers.removeAllViews();
                List<InterestedBuyersData> list = currentTractor.getInterestedBuyers();
                for(int j = 0; j < list.size(); j++) {
                    IntrestedBuyersListItemBinding rowBinding = IntrestedBuyersListItemBinding.inflate(((Activity) context).getLayoutInflater(),linearLayout,false);
                    rowBinding.tvName.setText(list.get(j).getUser().getFname() + " " + list.get(j).getUser().getLname());
                   // rowBinding.tvDate.setText(DateUtils.convertDateToDate(list.get(j).getCreatedAt(),DateUtils.DateFormat.yyyy_MM_dd_hh_mm_ss,DateUtils.DateFormat.dd_MM_yyyy));
                    // binding.etDOB.setText(DateUtils.getDate(selection,DateUtils.DateFormat.dd_mm_yyyy));
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    String dateInString = list.get(j).getCreatedAt();
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat formatterOut = new SimpleDateFormat("dd-MMM-yyyy");
                    try {
                        Date date = formatter.parse(dateInString);
                        System.out.println(date);
                        //System.out.println(formatterOut.format(date));
                        assert date != null;
                        String dtt = formatterOut.format(date);
                        rowBinding.tvDate.setText(dtt);
                    }
                    catch(ParseException e) {
                        e.printStackTrace();
                    }
                   // list.get(j).getUser().
                    rowBinding.tvEmail.setText(list.get(j).getUser().getEmail());
                    rowBinding.tvMobile.setText(list.get(j).getUser().getMobile());
                    int i = j;
                    rowBinding.tvCount.setText(String.valueOf(++i));
                    linearLayout.addView(rowBinding.getRoot());
                }
                ((ViewHolder) holder).binding.llIntrestedBuyers.addView(linearLayout);
                ((ViewHolder) holder).binding.ivInterestedBuyers.setImageResource(R.drawable.up_arrow);
            }
            else {
                ((ViewHolder) holder).binding.ivInterestedBuyers.setImageResource(R.drawable.down_arrow);
            }
        }
    }
    private String getImagePath(String basePath,String usedTractorImage) {
        if(usedTractorImage.contains("file"))
            return usedTractorImage;
        else return basePath+usedTractorImage;
    }
    @Override
    public int getItemCount() {
        return !items.isEmpty() ? items.size() : 1;
    }
    public void setOnChatClickListener(MyTrashClickListener myChatClickListener) {
        this.myChatClickListener = myChatClickListener;
    }
    private class ViewHolder extends RecyclerView.ViewHolder {
        private final MyuploadedUsedTractorListItemBinding binding;
        public ViewHolder(@NonNull MyuploadedUsedTractorListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(v -> {
                if (myChatClickListener != null)
                    myClickListener.onItemClick(getAbsoluteAdapterPosition(),v);
            });
            binding.btnSoldStatus.setOnClickListener(v -> {
                soldClickListener.onSoldClick(getAbsoluteAdapterPosition(),v);
                binding.pbSold.setVisibility(View.VISIBLE);
            });
            binding.ivTrash.setOnClickListener(v -> myTrashClickListener.onTrashClick(getAbsoluteAdapterPosition(),v));
            binding.ivInterestedBuyers.setOnClickListener(v->intrestedClickListener.onClick(getAbsoluteAdapterPosition(),v));
            binding.llBuyerLabel.setOnClickListener(v -> intrestedClickListener.onClick(getAbsoluteAdapterPosition(),v));
        }
    }
}
