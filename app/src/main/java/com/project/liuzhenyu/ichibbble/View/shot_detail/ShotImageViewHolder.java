package com.project.liuzhenyu.ichibbble.View.shot_detail;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;

/*--------------------------------------------------------------------------------------------------
 * Created by liuzhenyu on 8/3/17.
 * This ViewHolder extends RecyclerView ViewHolder;
 * So that we can cast a RecyclerView to a this class
 * Convert the current view into a SimpleDraweeView
 -------------------------------------------------------------------------------------------------*/

public class ShotImageViewHolder extends RecyclerView.ViewHolder{

    SimpleDraweeView image;

    public ShotImageViewHolder(View itemView) {
        super(itemView);
        image = (SimpleDraweeView) itemView;
    }
}
