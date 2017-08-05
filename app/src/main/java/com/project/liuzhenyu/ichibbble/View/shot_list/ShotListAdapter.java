package com.project.liuzhenyu.ichibbble.View.shot_list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.project.liuzhenyu.ichibbble.Model.Shot;
import com.project.liuzhenyu.ichibbble.R;
import com.project.liuzhenyu.ichibbble.Utils.ImageUtils;
import com.project.liuzhenyu.ichibbble.View.base.BaseViewHolder;
import com.project.liuzhenyu.ichibbble.View.base.InfiniteAdapter;

import java.util.List;

/**
 * Created by liuzhenyu on 7/30/17.
 */

public class ShotListAdapter extends InfiniteAdapter<Shot> {

    private final ShotListFragment shotListFragment;


    /*----------------------------------------------------------------------------------------------
       Constructor
     ---------------------------------------------------------------------------------------------*/
    public ShotListAdapter(@NonNull ShotListFragment shotListFragment,
                           @NonNull List<Shot> data,
                           @NonNull LoadMoreListener loadMoreListener) {
        super(shotListFragment.getContext(), data, loadMoreListener);
        this.shotListFragment = shotListFragment;
    }

    /*----------------------------------------------------------------------------------------------
      Override Methods
     ---------------------------------------------------------------------------------------------*/

    @Override
    protected BaseViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        /*
        A ViewGroup is a special view that can contain other views (called children.)
        The view group is the base class for layouts and views containers.
         */
        Fresco.initialize(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_shot,
                parent, false);
        return new ShotViewHolder(view);
    }

    @Override
    protected void onBindItemViewHolder(BaseViewHolder holder, int position) {
        ShotViewHolder shotViewHolder = (ShotViewHolder) holder;
        final Shot shot = getData().get(position);
        // TODO : start shotActivity Onclick for card

        shotViewHolder.viewCount.setText(String.valueOf(shot.views_count));
        shotViewHolder.bucketCount.setText(String.valueOf(shot.buckets_count));
        shotViewHolder.likeCount.setText(String.valueOf(shot.likes_count));
        ImageUtils.loadShotImage(shot, shotViewHolder.image);
    }
}
