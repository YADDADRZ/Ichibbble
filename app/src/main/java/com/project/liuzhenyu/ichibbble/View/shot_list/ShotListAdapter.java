package com.project.liuzhenyu.ichibbble.View.shot_list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.liuzhenyu.ichibbble.Model.Shot;
import com.project.liuzhenyu.ichibbble.R;

import java.util.List;

/**
 * Created by liuzhenyu on 7/30/17.
 */

public class ShotListAdapter extends RecyclerView.Adapter {

    private List<Shot> data;

    public ShotListAdapter(@NonNull List<Shot> data) {
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*
        A ViewGroup is a special view that can contain other views (called children.)
        The view group is the base class for layouts and views containers.
         */
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_shot,
                parent, false);
        return new ShotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Shot shot = data.get(position);

        ShotViewHolder shotViewHolder = (ShotViewHolder) holder;
        shotViewHolder.viewCount.setText(String.valueOf(shot.views_count));
        shotViewHolder.bucketCount.setText(String.valueOf(shot.buckets_count));
        shotViewHolder.likeCount.setText(String.valueOf(shot.likes_count));
        shotViewHolder.image.setImageResource(R.drawable.placeholder);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
