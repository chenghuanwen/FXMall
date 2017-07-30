package com.dgkj.fxmall.view;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import com.dgkj.fxmall.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ServiceAgreementActivity extends AppCompatActivity {

    @BindView(R.id.webview)
    WebView webView;
    @BindView(R.id.ib_back)
    ImageButton ibBack;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_agreement);
        ButterKnife.bind(this);

        showServiceAgreement();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showServiceAgreement() {

        webView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        //webView.setLayerType(View.LAYER_TYPE_SOFTWARE,null);//设置分层类型为软件级别（取消了硬件加速）后界面没加载完全时，webview无法获取焦点
        webView.requestFocus();
        webView.requestFocusFromTouch();
        webView.setWebViewClient(new WebViewClient());
        final WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportMultipleWindows(true);
        // webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDisplayZoomControls(false);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setDomStorageEnabled(true);
        // webSettings.setAppCacheEnabled(false);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.loadUrl("file:///android_asset/tianmi.html");


    }
    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
