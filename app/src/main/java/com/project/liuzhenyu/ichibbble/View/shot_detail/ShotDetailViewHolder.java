package com.project.liuzhenyu.ichibbble.View.shot_detail;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.liuzhenyu.ichibbble.R;
import com.project.liuzhenyu.ichibbble.View.base.BaseViewHolder;

import butterknife.BindView;

/*--------------------------------------------------------------------------------------------------
 * Created by liuzhenyu on 8/3/17.
--------------------------------------------------------------------------------------------------*/

public class ShotDetailViewHolder extends BaseViewHolder {

    @BindView(R.id.shot_title) TextView shot_title;
    @BindView(R.id.shot_author_name) TextView author_name;
    @BindView(R.id.shot_view_count) TextView shot_view_count;
    @BindView(R.id.shot_like_count) TextView shot_like_count;
    @BindView(R.id.shot_bucket_count) TextView shot_bucket_count;
    @BindView(R.id.shot_author_picture) ImageView author_picture;
    @BindView(R.id.shot_description) TextView shot_description;

    // BTN
    @BindView(R.id.shot_action_like) ImageButton like_button;
    @BindView(R.id.shot_action_bucket) ImageButton bucket_button;
    @BindView(R.id.shot_share_button) TextView share_button;

    public ShotDetailViewHolder(View itemView) {
        super(itemView);
    }
}
