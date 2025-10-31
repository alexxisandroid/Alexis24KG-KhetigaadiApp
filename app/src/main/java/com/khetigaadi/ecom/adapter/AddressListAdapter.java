package com.khetigaadi.ecom.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.khetigaadi.R;
import com.khetigaadi.base.util.BaseRecyclerViewAdapter;
import com.khetigaadi.commons.utils.EmptyViewHolder;
import com.khetigaadi.commons.utils.ProgressViewHolder;
import com.khetigaadi.databinding.AddressListItemBinding;
import com.khetigaadi.databinding.EmptyViewBinding;
import com.khetigaadi.model.Others;
import com.khetigaadi.shop.def.AddressType;
import com.khetigaadi.shop.model.Address;
public class AddressListAdapter extends BaseRecyclerViewAdapter<Address,Others> {
    private final int VIEW_ITEM = 1;
    private final Context context;
    private static final int EMPTY_VIEW = 10;
    private final int VIEW_PROG = 0;
    private ClickListener clickListener;
    public AddressListAdapter(Context context,RecyclerView recyclerView) {
        super(context, recyclerView);
        this.context = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == EMPTY_VIEW) {
            return new EmptyViewHolder(EmptyViewBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        } else if (viewType == VIEW_ITEM) {
            vh = new ViewHolder(AddressListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent,false));
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_for_horizontal_item, parent,false);
            vh = new ProgressViewHolder(context,v);
        }
        return vh;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            Address item = items.get(position);
            ((ViewHolder) holder).binding.tvName.setText(String.format("%s %s", item.getFirstName(), item.getLastName()));
            ((ViewHolder) holder).binding.tvType.setText(item.getType());
            if (item.getType().equalsIgnoreCase(AddressType.ADDRESS_HOME)) {
                ((ViewHolder) holder).binding.tvType.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context,R.drawable.ic_home_black_18dp),null,null,null);
            } else if (item.getType().equalsIgnoreCase(AddressType.ADDRESS_OFFICE)) {
                ((ViewHolder) holder).binding.tvType.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context,R.drawable.ic_business_center_black_18dp),null,null,null);
            } else {
                ((ViewHolder) holder).binding.tvType.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context,R.drawable.ic_other_black_18dp),null,null,null);
            }
            ((ViewHolder) holder).binding.tvAddressLine1.setText(item.getAddress());
            try {
                ((ViewHolder) holder).binding.tvAddressLine2.setText(String.format("%s, %s - %s",item.getCity().getName(),item.getState().getName(),item.getPinCode()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            ((ViewHolder) holder).binding.radioAddress.setChecked(item.getIsDefault() == 1);
            ((ViewHolder) holder).binding.tvMobile.setText(item.getMobile());
            ((ViewHolder) holder).binding.getRoot().setOnClickListener(v -> {
                if (clickListener != null)
                    clickListener.onItemClick(((ViewHolder) holder).binding.getRoot(), position);
            });
            ((ViewHolder) holder).binding.tvEdit.setOnClickListener(v -> {
                if (clickListener != null)
                    clickListener.onEditClick(v, position);
            });
            ((ViewHolder) holder).binding.tvDelete.setOnClickListener(v -> {
                if (clickListener != null)
                    clickListener.onDeleteClick(v, position);

            });
            ((ViewHolder) holder).binding.radioAddress.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked)
                    if (clickListener != null)
                        clickListener.onItemClick(buttonView, position);
            });
        }
    }
    @Override
    public int getItemViewType(int position) {
        if (items.size() == 0) {
            return EMPTY_VIEW;
        }
        return items.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }
    @Override
    public int getItemCount() {
        return items.size() > 0 ? items.size() : 1;
    }
    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }
    public Address getDefaultAddress() {
        for (Address address : getList()) {
            if (address.getIsDefault() == 1) {
                return address;
            }
        }
        return getList().get(0);
    }
    private static class ViewHolder extends RecyclerView.ViewHolder {
        private final AddressListItemBinding binding;
        public ViewHolder(@NonNull AddressListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public interface ClickListener {
        void onItemClick(View view, int position);
        void onEditClick(View view, int position);
        void onDeleteClick(View view, int position);
    }
}
