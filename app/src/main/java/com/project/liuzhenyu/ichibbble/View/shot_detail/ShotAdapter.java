package com.project.liuzhenyu.ichibbble.View.shot_detail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.liuzhenyu.ichibbble.Model.Shot;
import com.project.liuzhenyu.ichibbble.R;
import com.project.liuzhenyu.ichibbble.Utils.ImageUtils;
import com.project.liuzhenyu.ichibbble.View.shot_list.ShotViewHolder;

/*--------------------------------------------------------------------------------------------------
 * Created by liuzhenyu on 8/3/17.
 -------------------------------------------------------------------------------------------------*/

public class ShotAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_SHOT_IMAGE = 100;
    private static final int VIEW_TYPE_SHOT_DETAIL = 200;

    private final ShotFragment shotFragment;
    private final Shot shot; // Single Shot that pass from the shotFragment

    // Constructor
    public ShotAdapter(@NonNull ShotFragment shotFragment, @NonNull Shot shot) {
        this.shotFragment = shotFragment;
        this.shot = shot;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view;
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
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int viewType = getItemViewType(position); // only 2 type of view; depends on position 0 or 1
        switch (viewType){
            case VIEW_TYPE_SHOT_IMAGE:
                ImageUtils.loadShotImage(shot, ((ShotImageViewHolder) holder).image);
                break;
            case VIEW_TYPE_SHOT_DETAIL:
                final ShotDetailViewHolder detailViewHolder = (ShotDetailViewHolder) holder;
                detailViewHolder.shot_title.setText(shot.title);
                detailViewHolder.author_name.setText(shot.user.name);

                // Description from Dribbble is HTML String
                detailViewHolder.shot_description.
                        setText(Html.fromHtml(shot.description == null? "" : shot.description));
                // Provides cursor positioning, scrolling and text selection
                // functionality in a TextView.
                detailViewHolder.shot_description.setMovementMethod(LinkMovementMethod.getInstance());

                detailViewHolder.shot_like_count.setText(String.valueOf(shot.likes_count));
                detailViewHolder.shot_bucket_count.setText(String.valueOf(shot.buckets_count));
                detailViewHolder.shot_view_count.setText(String.valueOf(shot.views_count));

                // Author picture
                ImageUtils.loadUserPicture(getContext(),
                        detailViewHolder.author_picture,
                        shot.user.avatar_url);
                // TODO clickListener
                break;
        }
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

    public Context getContext() {
        return shotFragment.getContext();
    }
}
