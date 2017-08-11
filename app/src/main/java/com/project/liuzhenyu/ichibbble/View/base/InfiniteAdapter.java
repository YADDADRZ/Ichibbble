package com.project.liuzhenyu.ichibbble.View.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.liuzhenyu.ichibbble.R;

import java.util.List;

/*--------------------------------------------------------------------------------------------------
 * Created by liuzhenyu on 8/4/17.
 * This is the base adapter that Mainly handle the infinite loading
 -------------------------------------------------------------------------------------------------*/

public abstract class InfiniteAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    /*----------------------------------------------------------------------------------------------
       Constants
     ---------------------------------------------------------------------------------------------*/
    private static final int TYPE_ITEM = 100; // flag to load card
    private static final int TYPE_LOADING = 200; // flag to load loading anime

    private List<T> data;
    private final Context context;
    private boolean showLoading;

    // Callback function given by subclass
    private final LoadMoreListener loadMoreListener;
    public interface LoadMoreListener {
        void onLoadMore();
    }

    /*----------------------------------------------------------------------------------------------
       Constructor
     ---------------------------------------------------------------------------------------------*/

    public InfiniteAdapter(@NonNull Context context,
                           @NonNull List<T> data,
                           @NonNull LoadMoreListener loadMoreListener) {
        this.context = context;
        this.data = data;
        this.loadMoreListener = loadMoreListener;
        // default to show the loading anime when we reach the end
        // Until we found no more data can be load
        this.showLoading = true;
    }

    /*----------------------------------------------------------------------------------------------
       Abstract Methods
     ---------------------------------------------------------------------------------------------*/

    protected abstract BaseViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType);

    protected abstract void onBindItemViewHolder(BaseViewHolder holder, int position);

    /*----------------------------------------------------------------------------------------------
       Override Methods
     ---------------------------------------------------------------------------------------------*/

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // when type is item
        if (viewType == TYPE_LOADING) {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.list_item_loading, parent, false);
            return new BaseViewHolder(view);
        } else {
            // Do nothing Let subclass handle
            return onCreateItemViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        // Check type if we need to show loading
        final int viewType = getItemViewType(position);
        if (viewType == TYPE_LOADING) {
            loadMoreListener.onLoadMore(); // load more items
        } else {
            onBindItemViewHolder(holder, position);
        }
    }

    // When reach to the end, we show loading animation
    @Override
    public int getItemViewType(int position) {
        if (showLoading) {
            // when swipe to the last item we load, we want show loading anime
            return position == data.size()? TYPE_LOADING : TYPE_ITEM;
        } else {
            return TYPE_ITEM;
        }
    }

    // Decide if we need show a loading animation
    @Override
    public int getItemCount() {
        return showLoading? data.size() + 1 : data.size();
    }

    /*----------------------------------------------------------------------------------------------
       Utility
     ---------------------------------------------------------------------------------------------*/

    public void addFirst(@NonNull List<T> data) {
        this.data.addAll(0, data);
        notifyDataSetChanged(); // Notify RecyclerView that we have modified the data
    }

    public void addLast(@NonNull List<T> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public List<T> getData() {
        return data;
    }

    public void setData(@NonNull List<T> newData) {
        this.data.clear();
        this.data.addAll(newData);
        notifyDataSetChanged();
    }

    public void setShowLoading(boolean showLoading) {
        this.showLoading = showLoading;
        notifyDataSetChanged();
    }

    protected Context getContext() {
        return context;
    }
}

