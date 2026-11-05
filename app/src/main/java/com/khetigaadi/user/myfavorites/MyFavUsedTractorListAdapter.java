package com.khetigaadi.user.myfavorites;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.khetigaadi.AppConfig;
import com.khetigaadi.R;
import com.khetigaadi.base.util.BaseRecyclerViewAdapter;
import com.khetigaadi.commons.SharedPreferenceUtils;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.commons.utils.EmptyViewHolder;
import com.khetigaadi.commons.utils.MyClickListener;
import com.khetigaadi.commons.utils.MyFavoritesClickListener;
import com.khetigaadi.commons.utils.MyTrashClickListener;
import com.khetigaadi.commons.utils.ProgressViewHolder;
import com.khetigaadi.databinding.EmptyViewBinding;
import com.khetigaadi.databinding.UsedTractorListItemBinding;
import com.khetigaadi.tractor.usedTractor.util.UsedTractor;
import com.khetigaadi.user.model.User;
import com.khetigaadi.user.myfavorites.model.MyFavUsedTractor;
import com.khetigaadi.user.myfavorites.model.Others;
import com.khetigaadi.util.NumberFormatting;
import com.khetigaadi.util.Utils;
import org.jetbrains.annotations.NotNull;
public class MyFavUsedTractorListAdapter extends BaseRecyclerViewAdapter<MyFavUsedTractor, Others> {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private final Context mContext;
    private final User user;
    private static final int EMPTY_VIEW = 10;
    private static MyClickListener myClickListener;
    private static MyTrashClickListener myTrashClickListener;
    private static MyFavoritesClickListener myFavoritesClickListener;
    public MyFavUsedTractorListAdapter(Context mContext,User user,RecyclerView recyclerView) {
        super(mContext, recyclerView);
        this.mContext = mContext;
        this.user = user;
    }
    public void setOnItemClickListener(MyClickListener myClickListener) {
        MyFavUsedTractorListAdapter.myClickListener = myClickListener;
    }
    public void setOnSellerDetailsClickListener(MyTrashClickListener myTrashClickListener) {
        MyFavUsedTractorListAdapter.myTrashClickListener = myTrashClickListener;
    }
    public void setOnFavoritesClickListener(MyFavoritesClickListener myFavoritesClickListener) {
        MyFavUsedTractorListAdapter.myFavoritesClickListener = myFavoritesClickListener;
    }
    @Override
    public int getItemViewType(int position) {
        if(items.size() == 0) {
            return EMPTY_VIEW;
        }
        return items.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }
    @Override
    public RecyclerView.@NotNull ViewHolder onCreateViewHolder(@NotNull ViewGroup parent,int viewType) {
        RecyclerView.ViewHolder vh;

        if (viewType == EMPTY_VIEW) {
            return new EmptyViewHolder(EmptyViewBinding.inflate(
                    LayoutInflater.from(parent.getContext()),
                    parent,
                    false
            ));
        } else if (viewType == VIEW_ITEM) {
            vh = new ViewHolder(UsedTractorListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);
            vh = new ProgressViewHolder(mContext, v);
        }
        return vh;
    }
    @Override
    public void onBindViewHolder(final RecyclerView.@NotNull ViewHolder holder, int position) {
        if(holder instanceof ViewHolder) {
            //UsedTractors currentTractor = mTractors.get(position);
            UsedTractor currentTractor = items.get(position).getUsedTractor();
            ((ViewHolder) holder).binding.tvTractorName.setText(currentTractor.getBrand().getName().concat(" ").concat(currentTractor.getModel().getName()));
            if (currentTractor.getUser() != null) {
                if (!TextUtils.isEmpty(currentTractor.getUser().getFname()) && currentTractor.getUser().getFname() != null
                        && !TextUtils.isEmpty(currentTractor.getUser().getLname()) && currentTractor.getUser().getLname() != null) {
                    ((ViewHolder) holder).binding.tvSeller.setText(currentTractor.getUser().getFname().concat(" " + currentTractor.getUser().getLname()));
                } else {
                    ((ViewHolder) holder).binding.tvSeller.setVisibility(View.GONE);
                }
                if (currentTractor.getUser().getCity() != null) {
                    if (TextUtils.isEmpty(currentTractor.getUser().getCity().getName())) {
                        ((ViewHolder) holder).binding.tvCity.setVisibility(View.GONE);
                    } else {
                        ((ViewHolder) holder).binding.tvCity.setText(currentTractor.getUser().getCity().getName());
                    }
                } else {
                    ((ViewHolder) holder).binding.tvCity.setVisibility(View.GONE);
                }
            } else {
                ((ViewHolder) holder).binding.tvSeller.setVisibility(View.GONE);
                ((ViewHolder) holder).binding.tvCity.setVisibility(View.GONE);
            }
            if (TextUtils.isEmpty(currentTractor.getModelMonth()) && currentTractor.getModelMonth() == null && TextUtils.isEmpty(currentTractor.getModelYear()) && currentTractor.getModelYear() == null) {
                ((ViewHolder) holder).binding.tvYear.setVisibility(View.GONE);
            } else {
                ((ViewHolder) holder).binding.tvYear.setText(currentTractor.getModelMonth().concat(" " + currentTractor.getModelYear()));
            }
            if (currentTractor.getHoursDriven().isEmpty()) {
                ((ViewHolder) holder).binding.tvDriven.setVisibility(View.GONE);
            } else {
                ((ViewHolder) holder).binding.tvDriven.setText( currentTractor.getHoursDriven());
            }
            if (currentTractor.getPrice().isEmpty()) {
                ((ViewHolder) holder).binding.tvPrice.setVisibility(View.GONE);
            }
            else {
                ((ViewHolder) holder).binding.tvPrice.setText(String.format("%s%s",mContext.getString(R.string.rs),Utils.priceConvert(currentTractor.getPrice())));
            }
            if(currentTractor.getUserViews() != null) {
                try {
                    ((ViewHolder) holder).binding.tvViews.setText(NumberFormatting.format(Integer.parseInt(currentTractor.getUserViews())));
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
            //    Temporarly commented by PRANAV
            //    ((ViewHolder) holder).binding.tvSponser.setVisibility(View.GONE);

            if(currentTractor.getImage() != null) {
               /* if (!TextUtils.isEmpty(currentTractor.getImage().getUsedTractorImage()) && currentTractor.getImage().getUsedTractorImage() != null &&
                        !TextUtils.equals(currentTractor.getImage().getUsedTractorImage(), "null")) {*/
               // Log.d("fdgdfgdfgd====",others.getTractorImagePath()+currentTractor.getImage().getUsedTractorImage());
                String bUrl = AppConfig.BASE_ASSET_URL+"used_tractor/"; //others.getTractorImagePath()
               //others.getTractorImagePath()
                Glide.with(mContext)
                        .load(Constants.decodeURL(bUrl+currentTractor.getImage().getUsedTractorImage()))
                        .error(R.mipmap.no_image)
                        .placeholder(R.mipmap.no_image)
                        .into(((ViewHolder) holder).binding.ivImage);
                //}
            }
            if(currentTractor.getBrand() != null) {
                if (!TextUtils.isEmpty(currentTractor.getBrand().getImage())) {
                    String baseUrl = SharedPreferenceUtils.getInstance(mContext).getStringValue(Constants.BRAND_BASE_URL, "");
                    Glide.with(mContext)
                            .load(Constants.decodeURL(baseUrl + currentTractor.getBrand().getImage()))
                            .error(R.mipmap.no_image)
                            .placeholder(R.mipmap.no_image)
                            .into(((ViewHolder) holder).binding.brandImage);
                }
            }
            if(user.getId() != null && currentTractor.getUser() != null) {
                if (!currentTractor.getUser().getId().isEmpty() && user.getId().equals(currentTractor.getUser().getId())) {
                    ((ViewHolder) holder).binding.btnSellerDetails.setVisibility(View.GONE);
                } else if (currentTractor.getSoldUnsoldStatus() != null && !currentTractor.getSoldUnsoldStatus().equals("0")) {
                    ((ViewHolder) holder).binding.btnSellerDetails.setVisibility(View.GONE);
                } else {
                    ((ViewHolder) holder).binding.btnSellerDetails.setVisibility(View.VISIBLE);
                }
            }
            else {
                ((ViewHolder) holder).binding.btnSellerDetails.setVisibility(View.GONE);
            }

            if(currentTractor.getSoldUnsoldStatus() != null && !currentTractor.getSoldUnsoldStatus().equals("0")) {
                ((ViewHolder) holder).binding.ivSold.setVisibility(View.VISIBLE);
            }
            else {
                ((ViewHolder) holder).binding.ivSold.setVisibility(View.GONE);
            }
            if(currentTractor.getFavoritesStatus()) {
                ((ViewHolder) holder).binding.ivFavorites.setImageResource(R.drawable.ic_favorites);
            }
            else {
                ((ViewHolder) holder).binding.ivFavorites.setImageResource(R.drawable.ic_favorites_border);
            }
        }
    }
    public void setLoaded() {
        loading = false;
    }
    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size() > 0 ? items.size() : 1;
        }
        return 0;
    }
    private static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final UsedTractorListItemBinding binding;
        public ViewHolder(UsedTractorListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
            binding.btnSellerDetails.setOnClickListener(v -> myTrashClickListener.onTrashClick(getAbsoluteAdapterPosition(), v));
            binding.ivFavorites.setOnClickListener(v -> myFavoritesClickListener.onClick(getAbsoluteAdapterPosition(), v));
        }
        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAbsoluteAdapterPosition(),v);
        }
    }
}
