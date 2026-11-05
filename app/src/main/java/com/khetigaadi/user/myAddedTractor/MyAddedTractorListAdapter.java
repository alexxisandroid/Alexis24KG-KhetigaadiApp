package com.khetigaadi.user.myAddedTractor;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.khetigaadi.AppConfig;
import com.khetigaadi.R;
import com.khetigaadi.commons.utils.EmptyViewHolder;
import com.khetigaadi.commons.utils.MyClickListener;
import com.khetigaadi.commons.utils.MyTrashClickListener;
import com.khetigaadi.commons.utils.ProgressViewHolder;
import com.khetigaadi.databinding.EmptyViewBinding;
import com.khetigaadi.databinding.MyuploadedRentTractorListItemBinding;
import com.khetigaadi.model.Others;
import com.khetigaadi.tractor.rent.model.RentTractor;
import com.khetigaadi.util.Utils;
import org.jetbrains.annotations.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
public class MyAddedTractorListAdapter extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private final Context mContext;
    private final List<AddedTractor> mTractors;
    private final Others others;
    private static MyClickListener myClickListener;
    private static MyTrashClickListener myTrashClickListener;
    private static final int EMPTY_VIEW = 10;
    public MyAddedTractorListAdapter(Context mContext,List<AddedTractor> tractorList,Others others) {
        this.mContext = mContext;
        this.mTractors = tractorList;
        this.others = others;
    }
    public void setOnItemClickListener(MyClickListener myClickListener) {
        MyAddedTractorListAdapter.myClickListener = myClickListener;
    }
    public void setOnTrashClickListener(MyTrashClickListener myTrashClickListener) {
        MyAddedTractorListAdapter.myTrashClickListener = myTrashClickListener;
    }
    @Override
    public int getItemViewType(int position) {
        if(mTractors.size() == 0) {
            return EMPTY_VIEW;
        }
        return mTractors.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }
    @Override
    public RecyclerView.@NotNull ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if(viewType == EMPTY_VIEW) {
            return new EmptyViewHolder(EmptyViewBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        }
        else if(viewType == VIEW_ITEM) {
            vh = new MyAddedTractorListAdapter.ViewHolder(MyuploadedRentTractorListItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        }
        else {
            vh = new ProgressViewHolder(mContext,LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_item, parent,false));
        }
        return vh;
    }
    @Override
    public void onBindViewHolder(final RecyclerView.@NotNull ViewHolder holder,int position) {
        if(holder instanceof MyAddedTractorListAdapter.ViewHolder) {
          try {
              AddedTractor item = mTractors.get(position);
              if(item.getBrand() != null && item.getBrand().getName() != null)
                  ((MyAddedTractorListAdapter.ViewHolder) holder).binding.tvNewTractorName.setText(item.getBrand().getName());
              if(item.getTractorModel() != null)
                  ((MyAddedTractorListAdapter.ViewHolder) holder).binding.tvNewTractorName.setText(item.getBrand().getName() + " (" + item.getTractorModel().getName() + ")");
              //+" ("+item.getTractorModel().getName()+")"
              if(item.getUser().getRegisteredDate().isEmpty() || item.getUser().getRegisteredDate().trim().equalsIgnoreCase("")) {
                  ((MyAddedTractorListAdapter.ViewHolder) holder).binding.tvOwnerPostedOn.setVisibility(View.GONE);
              } else {
                  ((ViewHolder) holder).binding.tvOwnerPostedOn.setVisibility(View.VISIBLE);
                  // ((ViewHolder) holder).binding.tvOwnerPostedOn.setText(DateUtils.convertDateToDate(item.getCreatedAt(), DateUtils.DateFormat.yyyy_MM_dd_T_HH_mm_ss_SSS_Z, DateUtils.DateFormat.dd_mm_yyyy));
                  @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                  String dateInString = item.getUser().getRegisteredDate();
                  @SuppressLint("SimpleDateFormat") SimpleDateFormat formatterOut = new SimpleDateFormat("dd MMM yyyy");
                  try {
                      Date date = formatter.parse(dateInString);
                      System.out.println(date);
                      //System.out.println(formatterOut.format(date));
                      assert date != null;
                      String dtt = formatterOut.format(date);
                      ((ViewHolder) holder).binding.tvOwnerPostedOn.setText(item.getTractorMonth() + " " + item.getTractorYear());
                  } catch (ParseException e) {
                      e.printStackTrace();
                  }
              }
              ((ViewHolder) holder).binding.ivDrive.setVisibility(View.GONE);
              ((ViewHolder) holder).binding.tvHP.setVisibility(View.GONE);
              ((ViewHolder) holder).binding.tvPrice.setVisibility(View.GONE);
//            if(item.getDrive() == null) {
//                ((ViewHolder) holder).binding.ivDrive.setVisibility(View.GONE);
//            }
//            else {
//                ((ViewHolder) holder).binding.ivDrive.setVisibility(View.VISIBLE);
//                if(item.getDrive().getId() == 1) {
//                    ((ViewHolder) holder).binding.ivDrive.setImageResource(R.drawable.ic_2wd);
//                }
//                else {
//                    ((ViewHolder) holder).binding.ivDrive.setImageResource(R.drawable.ic_4wd);
//                }
//            }
//            if(item.getExactHp() == null || item.getExactHp().isEmpty() || item.getExactHp().trim().equalsIgnoreCase("") || item.getExactHp().equals("0")) {
//                ((ViewHolder) holder).binding.tvHP.setVisibility(View.GONE);
//            }
//            else {
//                ((ViewHolder) holder).binding.tvHP.setText(String.format("%s %s", item.getExactHp(),mContext.getString(R.string.hp)));
//            }
//            if(item.getRentTractorImplement() == null) {
//                ((ViewHolder)holder).binding.tvPrice.setVisibility(View.GONE);
//            }
//            else {
//                ((ViewHolder)holder).binding.tvPrice.setText(String.format("%s%s/%s",mContext.getString(R.string.rs),Utils.priceConvert(item.getRentTractorImplement().getRate()), item.getRentTractorImplement().getUnit() != null ? item.getRentTractorImplement().getUnit().getUnitName() : "unit not coming"));
//            }
              String bUrl = AppConfig.BASE_URL + "images/owner_data/";
              if (others.getTractorImagePath() != null)
                  bUrl = others.getTractorImagePath();
              if (item.getImages() != null && !item.getImages().isEmpty())
                  Glide.with(mContext)//others.getTractorImagePath()
                          .load(bUrl + item.getImages().get(0).getName())
                          .error(R.mipmap.no_image)
                          .placeholder(R.mipmap.no_image)
                          .into(((ViewHolder) holder).binding.ivNewTractorImage);

          }
          catch(Exception e) {
              e.printStackTrace();
          }
        }
    }
    @Override
    public int getItemCount() {
        return mTractors.size() > 0 ? mTractors.size():1;
    }
    public AddedTractor getItem(int position) {
        if(mTractors == null || mTractors.size() == 0)
            return null;
        else
            return mTractors.get(position);
    }
    private static class ViewHolder extends RecyclerView.ViewHolder {
        private final MyuploadedRentTractorListItemBinding binding;
        public ViewHolder(@NonNull MyuploadedRentTractorListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(v -> {
                if(myClickListener != null)
                    myClickListener.onItemClick(getAbsoluteAdapterPosition(),v);
            });
            binding.ivTrash.setOnClickListener(v -> {
                if(myTrashClickListener != null)
                    myTrashClickListener.onTrashClick(getAbsoluteAdapterPosition(),v);
            });
        }
    }
}