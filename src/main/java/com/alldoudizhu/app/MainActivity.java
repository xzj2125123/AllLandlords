package com.alldoudizhu.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {
	private WebView view;
	private RelativeLayout rootView;
	private MyWebChromeClient mWebChromeClient;
	private WebChromeClient.CustomViewCallback mCustomViewCallback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		rootView = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.activity_main, null);
		setContentView(rootView);
		view = (WebView) findViewById(R.id.webview);
		view.setWebViewClient(new MyWebViewClient());
		// Configure the webview
		WebSettings s = view.getSettings();
		s.setBuiltInZoomControls(true);
		s.setUseWideViewPort(true);
		s.setLoadWithOverviewMode(true);
		s.setJavaScriptEnabled(true);
		s.setJavaScriptCanOpenWindowsAutomatically(true);
		s.setDomStorageEnabled(true);
		if (isNetWorkAvaiable()) {
			view.loadUrl("http://ddz.yiqiyouxi.net:8080/login.html");
		} else {
			rootView.setBackgroundResource(R.drawable.no_net);
			view.setVisibility(View.GONE);
			rootView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if(android.os.Build.VERSION.SDK_INT > 10 ){
					     //3.0以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面
					    startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
					} else {
					    startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
					}
				}
			});
		}
	}

	private class MyWebChromeClient extends WebChromeClient {
		private Bitmap mDefaultVideoPoster;
		private View mVideoProgressView;

		@Override
		public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
		}

		@Override
		public void onHideCustomView() {
			mCustomViewCallback.onCustomViewHidden();
		}

		@Override
		public Bitmap getDefaultVideoPoster() {
			if (mDefaultVideoPoster == null) {
				mDefaultVideoPoster = BitmapFactory.decodeResource(getResources(), R.drawable.default_video_poster);
			}
			return mDefaultVideoPoster;
		}

		@Override
		public View getVideoLoadingProgressView() {
			if (mVideoProgressView == null) {
				LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
				mVideoProgressView = inflater.inflate(R.layout.video_loading_progress, null);
			}
			return mVideoProgressView;
		}

		@Override
		public void onReceivedTitle(WebView view, String title) {
			(MainActivity.this).setTitle(title);
		}

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			(MainActivity.this).getWindow().setFeatureInt(Window.FEATURE_PROGRESS, newProgress * 100);
		}

		@Override
		public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
			callback.invoke(origin, true, false);
		}
	}

	/**
	 * 网络判断
	 * 
	 */
	public boolean isNetWorkAvaiable() {
		// 系统里面提供的网络访问状况相关的服务
		ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info != null) {
			return info.isAvailable();
		} else {
			return false;
		}
	}

	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView v, String url) {
			if (isNetWorkAvaiable()) {
				Log.i("xzj","加载URL="+ url);
				v.loadUrl(url);
			} else {
				view.setVisibility(View.GONE);
				rootView.setBackgroundResource(R.drawable.no_net);
				rootView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						if(android.os.Build.VERSION.SDK_INT > 10 ){
						     //3.0以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面
						    startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
						} else {
						    startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
						}
					}
				});
			}
			return true;
		}
	}
}
