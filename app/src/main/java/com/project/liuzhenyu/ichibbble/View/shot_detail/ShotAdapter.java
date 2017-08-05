package com.project.liuzhenyu.ichibbble.View.shot_detail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.liuzhenyu.ichibbble.Model.Shot;
import com.project.liuzhenyu.ichibbble.R;
import com.project.liuzhenyu.ichibbble.View.shot_list.ShotViewHolder;

/**
 * Created by liuzhenyu on 8/3/17.
 */

public class ShotAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_SHOT_IMAGE = 100;
    private static final int VIEW_TYPE_SHOT_DETAIL = 200;

    private final ShotFragment shotFragment;
    private final Shot shot;

    // Constructor
    public ShotAdapter(@NonNull ShotFragment shotFragment, @NonNull Shot shot) {
        this.shotFragment = shotFragment;
        this.shot = shot;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
/*        View view;
        switch (viewType) {
            case VIEW_TYPE_SHOT_IMAGE:
                view = LayoutInflater.from(getContext())
                        .inflate(R.layout.list_item_shot_detail_image, parent, false);
                return new ShotImageViewHolder(view);
            case VIEW_TYPE_SHOT_DETAIL:
                view = LayoutInflater.from(getContext())
                        .inflate(R.layout.list_item_shot_detail_info, parent, false);
                return new ShotDetailViewHolder(view);
            default:
                return null;
        }*/
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 2; // A fragment (shot_detail) contains two layout (specificly define here)
    }

    /**
     * Convert item position accordingly to viewType
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return position == 0? VIEW_TYPE_SHOT_IMAGE : VIEW_TYPE_SHOT_DETAIL;
    }

    /*public Context getContext() {
        return shotFragment.getContext();
    }*/
}
