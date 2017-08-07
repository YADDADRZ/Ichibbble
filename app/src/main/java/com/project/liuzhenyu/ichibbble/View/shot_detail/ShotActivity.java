package com.project.liuzhenyu.ichibbble.View.shot_detail;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.project.liuzhenyu.ichibbble.View.base.SingleFragmentActivity;

/*--------------------------------------------------------------------------------------------------
 * Created by liuzhenyu on 8/3/17.
 -------------------------------------------------------------------------------------------------*/

public class ShotActivity extends SingleFragmentActivity {

    public static final String KEY_SHOT_TITLE = "shot_title";

    @NonNull
    @Override
    protected String getActivityTitle() {
        return getIntent().getStringExtra(KEY_SHOT_TITLE);
    }

    /**
     * Directly pass shot item to shortFragment to form a Fragment
     * @return a fragment that been ready to add to fragement container
     */
    @NonNull
    @Override
    protected Fragment newFragment() {
        // Make sure Use Android support lib for fragment in ShotFragment class
        // Otherwise it wont work here
        return ShotFragment.newInstance(getIntent().getExtras());
    }
}
