package com.khetigaadi.commons.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.khetigaadi.BuildConfig;
import com.khetigaadi.R;
import com.khetigaadi.base.model.State;
import com.khetigaadi.commons.SharedPreferenceUtils;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.databinding.StateHomeListItemBinding;
import java.util.ArrayList;
import java.util.List;
public class StateHomeListAdapter extends RecyclerView.Adapter<StateHomeListAdapter.MyViewHolder> implements Filterable {
    private List<State> mModelList;
    private List<State> filteredData = null;
    private ItemFilter mFilter = new ItemFilter();
    private OnClickListener onClickListener;
    private Context context;
    private SharedPreferenceUtils pref;
    public StateHomeListAdapter(Context context,SharedPreferenceUtils pref) {
        this.context = context;
        this.pref = pref;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(StateHomeListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final State item = filteredData.get(position);
        ((MyViewHolder) holder).binding.tvTitle.setText(item.getName());

         String imgPath= pref.getStringValue(Constants.STATE_BASE_URL, "") ;;
         //"https://khetigaadi.com/images/states/";
        if(imgPath==null || imgPath.equals(null) || imgPath.equals("null") || imgPath.equals(""))
            imgPath = BuildConfig.BASE_URL+"images/states/";
        Glide.with(context)
                .load(imgPath + (item.getImage() != null ? item.getImage() : null))
                .placeholder(R.mipmap.no_image)
                .error(R.mipmap.no_image)
                .into(((MyViewHolder) holder).binding.ivImage);
        ((MyViewHolder) holder).binding.getRoot().setOnClickListener(view -> {
            if (onClickListener != null)
                onClickListener.onClick(item);
        });
    }
    @Override
    public int getItemCount() {
        return filteredData == null ? 0 : filteredData.size();
    }
    @Override
    public Filter getFilter() {
        return mFilter;
    }
    public void setData(List<State> data) {
        filteredData = data;
        mModelList = data;
        notifyDataSetChanged();
    }
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private StateHomeListItemBinding binding;
        private MyViewHolder(StateHomeListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            final List<State> list = mModelList;
            int count = list.size();
            final ArrayList<State> nlist = new ArrayList<>(count);
            State filterableString;
            for (int i = 0; i < count; i++) {
                filterableString = list.get(i);
                if (filterableString.getName().toLowerCase().contains(filterString)) {
                    nlist.add(filterableString);
                }
            }
            results.values = nlist;
            results.count = nlist.size();
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<State>) results.values;
            notifyDataSetChanged();
        }
    }
    public interface OnClickListener {
        void onClick(State brand);
    }
}
