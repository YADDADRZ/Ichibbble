package com.project.liuzhenyu.ichibbble.View.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.project.liuzhenyu.ichibbble.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/*--------------------------------------------------------------------------------------------------
 * Created by liuzhenyu on 8/3/17.
 * This is a Base Activity for the activity with a single Fragment layout
 * Handle the toolbar, backbutton, setTitle and form the fragment
 -------------------------------------------------------------------------------------------------*/

public abstract class SingleFragmentActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment); // set up fragment container
        ButterKnife.bind(this);

        setSupportActionBar(toolbar); // switch actionBar to toolbar
        if (isBackEnabled()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setTitle(getActivityTitle()); // set title

        // Take Fragment into Fragemnt_container to form the view
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    /*------------------------------------------------------------------------------
                     * Notice that the data is setted by ShotFragment class
                    ------------------------------------------------------------------------------*/
                    .add(R.id.fragment_container, newFragment())
                    .commit();
        }
    }

    protected boolean isBackEnabled() {
        return true;
    };

    /*----------------------------------------------------------------------------------------------
       Back Button
     ---------------------------------------------------------------------------------------------*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Return back to upper activity when we press back button
        if (isBackEnabled() && item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*----------------------------------------------------------------------------------------------
       Abstract method
     ---------------------------------------------------------------------------------------------*/
    @NonNull
    protected abstract String getActivityTitle();

    @NonNull
    protected abstract Fragment newFragment();
}
