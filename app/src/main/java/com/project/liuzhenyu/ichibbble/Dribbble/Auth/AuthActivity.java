package com.project.liuzhenyu.ichibbble.Dribbble.Auth;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.project.liuzhenyu.ichibbble.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/*-------------------------------------------------------------------------------------------------
  This class set up webview for login activity
------------------------------------------------------------------------------------------------ */

public class AuthActivity extends AppCompatActivity {

    public static final String KEY_URL = "url";
    public static final String KEY_CODE = "code";

    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.webview) WebView webView;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Log into Dribbble");

        progressBar.setMax(100);

        // Start the webView
        webView.setWebViewClient(new WebViewClient() {

            /*-------------------------------------------------------------------------------------
            Give the host application a chance to take over the control when a new url is
            about to be loaded in the current WebView.

            If WebViewClient is not provided, by default WebView will ask Activity Manager to
            choose the proper handler for the url.

            If WebViewClient is provided, return true means the host application handles the url,
            while return false means the current WebView handles the url.
            This method is not called for requests using the POST "method".
             ------------------------------------------------------------------------------------*/

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.startsWith(Auth.REDIRECT_URI)) {
                    Log.i("URL_2", url);
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent();
                    intent.putExtra(KEY_CODE, uri.getQueryParameter(KEY_CODE));
                    setResult(RESULT_OK, intent);
                    finish();
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);
            }
        });

        // Set up progress bar
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
            }
        });

        String url = getIntent().getStringExtra(KEY_URL);
        Log.i("URL_1", url);
        webView.loadUrl(url);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
