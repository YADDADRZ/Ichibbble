package com.project.liuzhenyu.ichibbble.View.shot_list;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.project.liuzhenyu.ichibbble.Dribbble.DribbbleException;
import com.project.liuzhenyu.ichibbble.Dribbble.Dribbble;
import com.project.liuzhenyu.ichibbble.Model.Shot;
import com.project.liuzhenyu.ichibbble.R;
import com.project.liuzhenyu.ichibbble.Utils.ModelUtils;
import com.project.liuzhenyu.ichibbble.View.base.DribbbleTask;
import com.project.liuzhenyu.ichibbble.View.base.InfiniteAdapter;
import com.project.liuzhenyu.ichibbble.View.base.SpaceItemDecoration;
import com.project.liuzhenyu.ichibbble.View.shot_detail.ShotFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/*--------------------------------------------------------------------------------------------------
 * Created by liuzhenyu on 7/30/17.
 * Fragment covered one recyclerView and this recyclerView contain multiple card
 -------------------------------------------------------------------------------------------------*/

public class ShotListFragment extends Fragment {

    public static final int REQ_CODE_SHOT = 100;
    public static final String KEY_LIST_TYPE = "listType";
    public static final String KEY_BUCKET_ID = "bucketId";

    private int listType;
    public static final int LIST_TYPE_POPULAR = 1;
    public static final int LIST_TYPE_LIKED = 2;
    public static final int LIST_TYPE_BUCKET = 3;

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_container) SwipeRefreshLayout swipeRefreshLayout; // Refresh animation

    private ShotListAdapter adapter;

    public static ShotListFragment newInstance(int listType) {
        Bundle args = new Bundle();
        args.putInt(KEY_LIST_TYPE, listType);

        ShotListFragment fragment = new ShotListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_swipe_recycler_view, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Get list type
        listType = getArguments().getInt(KEY_LIST_TYPE);
        // Handle Refresh
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new LoadShotsTask(true).execute();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // add Divider to recyclerView
        recyclerView.addItemDecoration(new SpaceItemDecoration(
                getResources().getDimensionPixelOffset(R.dimen.space_medium)));

        adapter = new ShotListAdapter(this, new ArrayList<Shot>(), onLoadMore);
        recyclerView.setAdapter(adapter);
    }

    private InfiniteAdapter.LoadMoreListener onLoadMore = new InfiniteAdapter.LoadMoreListener() {
        @Override
        public void onLoadMore() {
            if (Dribbble.isLogin()) {
                new LoadShotsTask(false).execute();
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_SHOT && resultCode == Activity.RESULT_OK) {
            Shot updatedShot = ModelUtils.toObject(data.getStringExtra(ShotFragment.KEY_SHOT),
                    new TypeToken<Shot>(){});

            for (Shot shot : adapter.getData()) {
                if (TextUtils.equals(shot.id, updatedShot.id)) {
                    shot.likes_count = updatedShot.likes_count;
                    shot.buckets_count = updatedShot.likes_count;
                    adapter.notifyDataSetChanged();
                    return;
                }
            }
        }
    }

    /*----------------------------------------------------------------------------------------------
          AsycnTask load Shots from Dribbble
     ---------------------------------------------------------------------------------------------*/
    private class LoadShotsTask extends DribbbleTask<Void, Void, List<Shot>> {

        private boolean refresh;

        // Constructor
        private LoadShotsTask(boolean refresh) {
            this.refresh = refresh;
        }

        @Override
        protected List<Shot> doSomething(Void... params) throws DribbbleException {
            int page = refresh? 1 : adapter.getData().size() / Dribbble.COUNT_PER_LOAD + 1;

            switch (listType) {
                case LIST_TYPE_POPULAR:
                    return Dribbble.getShots(page);
                case LIST_TYPE_LIKED:
                    return Dribbble.getLikeShots(page);
                default:
                    return Dribbble.getShots(page);
            }
        }

        @Override
        protected void onSuccess(List<Shot> shots) {
            adapter.setShowLoading(shots.size() >= Dribbble.COUNT_PER_LOAD);

            if (refresh) {
                swipeRefreshLayout.setRefreshing(false);
                adapter.setData(shots);
            } else {
                swipeRefreshLayout.setEnabled(true);
                adapter.addLast(shots);
            }
        }

        @Override
        protected void onFailed(DribbbleException e) {
            Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_LONG).show();
        }
    }
}
