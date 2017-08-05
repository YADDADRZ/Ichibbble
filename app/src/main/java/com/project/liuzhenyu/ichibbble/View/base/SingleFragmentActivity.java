package com.project.liuzhenyu.ichibbble.View.base;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.project.liuzhenyu.ichibbble.R;

import butterknife.BindView;

/**
 * Created by liuzhenyu on 8/3/17.
 */

public abstract class SingleFragmentActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
}
