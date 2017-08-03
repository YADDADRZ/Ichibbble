package com.project.liuzhenyu.ichibbble.View.shot_list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.liuzhenyu.ichibbble.R;
import com.project.liuzhenyu.ichibbble.View.base.BaseViewHolder;

import butterknife.BindView;

/**
 * Created by liuzhenyu on 7/30/17.
 */

public class ShotViewHolder extends BaseViewHolder {

    @BindView(R.id.shot_clickable_cover) public View cover;
    @BindView(R.id.shot_like_count) public TextView likeCount;
    @BindView(R.id.shot_bucket_count) public TextView bucketCount;
    @BindView(R.id.shot_view_count) public TextView viewCount;
    @BindView(R.id.shot_image) public ImageView image;

    public ShotViewHolder(View itemView) {
        super(itemView);
    }
}
