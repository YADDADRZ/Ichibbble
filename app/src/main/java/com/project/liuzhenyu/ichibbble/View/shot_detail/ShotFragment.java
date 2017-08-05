package com.project.liuzhenyu.ichibbble.View.shot_detail;

import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.project.liuzhenyu.ichibbble.Dribbble.Auth.DribbbleException;
import com.project.liuzhenyu.ichibbble.Dribbble.Dribbble;
import com.project.liuzhenyu.ichibbble.Model.Shot;
import com.project.liuzhenyu.ichibbble.R;
import com.project.liuzhenyu.ichibbble.Utils.ModelUtils;
import com.project.liuzhenyu.ichibbble.View.base.DribbbleTask;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liuzhenyu on 8/3/17.
 */

public class ShotFragment extends Fragment{

    public static final String KEY_SHOT = "shot";

    private static final int REQ_CODE_BUCKET = 100;

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private Shot shot;
    private boolean isLiking;
    private ArrayList<String> collectedBucketIds;

    public static ShotFragment newInstance(@NonNull Bundle args) {
        ShotFragment fragment = new ShotFragment();
        fragment.setArguments(args); // Supply the construction arguments for this fragment.
        return fragment;
    }

    /**
     * Fragment is build by recyclerView; we can re-use layout fragment_recycler_view
     *
     * @param inflater To return a layout from onCreateView(), you can inflate it from a
     *                 layout resource defined in XML. To help you do so, onCreateView() provides
     *                 a LayoutInflater object.
     *
     * @param container The container parameter passed to onCreateView() is the parent ViewGroup
     *                  (from the activity's layout) in which your fragment layout will be inserted.
     *
     * @param savedInstanceState  The savedInstanceState parameter is a Bundle that provides data
     *                            about the previous instance of the fragment, if the fragment is being
     *                            resumed (restoring state is discussed more in the section about
     *                            Handling the Fragment Lifecycle).
     *
     * @return  return a View that is the root of your fragment's layout.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container ,false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // get shot info from
        // getArgument() will return bundle;
        shot = ModelUtils.toObject(getArguments().getString(KEY_SHOT), new TypeToken<Shot>(){});
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ShotAdapter(this, shot));

        // TODO 1
    }

    private class checkLikeTask extends DribbbleTask<Void, Void, Boolean> {

        @Override
        protected Boolean doSomething(Void... params) throws DribbbleException {
            return Dribbble.isLikeShot(shot.id);
        }

        @Override
        protected void onSuccess(Boolean aBoolean) {

        }

        @Override
        protected void onFailed(DribbbleException e) {

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}

