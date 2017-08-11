package com.project.liuzhenyu.ichibbble.View.base;

import android.os.AsyncTask;

import com.project.liuzhenyu.ichibbble.Dribbble.DribbbleException;

/*--------------------------------------------------------------------------------------------------
 * Created by liuzhenyu on 8/3/17.
 * This Abstract class provide the base for all AsyncTask activity with a 3 callback function
 * Only doSomething runs on non-UI thread; other two will execute on UI thread
 -------------------------------------------------------------------------------------------------*/

public abstract class DribbbleTask<Params, Progress, Result> extends
        AsyncTask<Params, Progress, Result> {

    private DribbbleException exception;

    // code suppose to execute in background in non_main thread
    protected abstract Result doSomething(Params... params) throws DribbbleException;

    // Main thread method
    protected abstract void onSuccess(Result result);

    protected abstract void onFailed(DribbbleException e);

    @Override
    protected Result doInBackground(Params... paramses) {
        try {
            return doSomething(paramses);
        } catch (DribbbleException e) {
            e.printStackTrace();
            exception = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(Result result) {
        if (exception != null) {
            onFailed(exception);
        } else {
            onSuccess(result);
        }
    }
}
