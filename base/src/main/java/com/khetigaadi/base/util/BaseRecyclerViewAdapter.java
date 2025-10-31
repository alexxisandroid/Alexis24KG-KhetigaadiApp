package com.khetigaadi.base.util;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
public abstract class BaseRecyclerViewAdapter<T,R> extends RecyclerView.Adapter {
    protected static final int VIEW_LARGE_ITEM = 2;
    protected boolean loading;
    protected List<T> items;
    protected R others;
    private int lastVisibleItem,totalItemCount;
    private OnLoadMoreListener onLoadMoreListener;
    private final int visibleThreshold = 5;
    private int currPageNo = 1;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private int totPages = 1;
    public BaseRecyclerViewAdapter(Context mContext,RecyclerView recyclerView) {
        items = new ArrayList<>();
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();
            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition();
                            if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold) && currPageNo < totPages) {
                                if (onLoadMoreListener != null) {
                                    addItem(null);
                                    handler.postDelayed(() -> {
                                        if (getList() != null && getList().size() != 1) {
                                            ++currPageNo;
                                            if (currPageNo <= totPages) {
                                                onLoadMoreListener.onLoadMore(currPageNo);
                                            } else {
                                                itemRemove();
                                            }
                                        }
                                    }, 2000);

                                }
                                loading = true;
                            }
                        }
                    });
        }
    }
    private void setLoaded() {
        loading = false;
    }
    public int getTotPages() {
        return totPages;
    }
    public void setTotPages(int totPages) {
        this.totPages = totPages;
        if (currPageNo != 1)
            itemRemove();
    }
    @Override
    public int getItemCount() {
        return items.size() > 0 ? items.size() : 1;
    }
    public void addItem(T item) {
        if (items.size() != 0) {
            items.add(item);
            notifyItemInserted(items.size());
        }
    }
    public List<T> getList() {
        return items;
    }
    public T getItem(int position) {
        return items.get(position);
    }
    public void addItems(List<T> appendTractorArray,int currentPage,R others) {
        if (others != null)
            this.others = others;
        currPageNo = currentPage;
        itemRemove();
        items.addAll(appendTractorArray);
        notifyItemRangeInserted(items.size() - appendTractorArray.size() + 1, items.size());
        setLoaded();
    }
    public void addItems(List<T> appendTractorArray,int currentPage) {
        addItems(appendTractorArray, currentPage, null);
    }
    public void setCurrPageNo(int currPageNo) {
        this.currPageNo = currPageNo;
    }
    public void updateItems(List<T> items) {
        this.items = items;
        notifyDataSetChanged();
    }
    public void updateItems(List<T> items,int currentPage) {
        currPageNo = currentPage;
        updateItems(items);
    }
    public void addItemAtPosition(T item,int position) {
        this.items.add(position, item);
        notifyItemInserted(position);
    }
    public void updateItem(T item,int position) {
        this.items.set(position, item);
        notifyItemChanged(position);
    }
    private boolean itemRemove() {
        if (items.size() != 0 && items.get(items.size() - 1) == null) {
            items.remove(items.size() - 1);
            notifyItemRemoved(items.size());
            return true;
        }
        return false;
    }
    public void itemRemoveAll() {
        items.clear();
        notifyDataSetChanged();
    }
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }
    public boolean itemRemove(int position) {
        if (items.size() > 0) {
            items.remove(position);
            notifyItemRemoved(position);
            return true;
        }
        return false;
    }
    public void setOthers(R others) {
        this.others = others;
    }
    public R getOthers() {
        return others;
    }
    public interface OnLoadMoreListener {
        void onLoadMore(int nextPageNo);
    }
}
