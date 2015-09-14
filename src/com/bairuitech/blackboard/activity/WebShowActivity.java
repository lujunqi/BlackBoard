package com.bairuitech.blackboard.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.bairuitech.blackboard.R;

/**
 * web页的方式
 */

public class WebShowActivity extends Activity {
	private TextView tv_title;
	private Context context;
	private WebView mWebView;
	private static final String TAG = "WebShowActivity";

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		context = this;
		setContentView(R.layout.activity_web_show);
		Intent intent = this.getIntent();
		String s_tv_title = intent.getStringExtra("tv_title");
		tv_title = (TextView) this.findViewById(R.id.tv_title);
		tv_title.setText(s_tv_title);

		String s_url = intent.getStringExtra("url");
		mWebView = (WebView) this.findViewById(R.id.webshow);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		mWebView.setWebChromeClient(webchromeclient);
		mWebView.requestFocus();
		mWebView.loadUrl(s_url);
		mWebView.setWebViewClient(new MyWebViewClient());
	}

	WebChromeClient webchromeclient = new WebChromeClient() {
		public boolean onJsAlert(WebView view, String url, String message,
				JsResult result) {
			Toast.makeText(WebShowActivity.this, message, Toast.LENGTH_LONG)
					.show();
			result.confirm();
			return true;
		}
	};

	class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url_) {
			view.loadUrl(url_);
			return true;
		}
	}

	@Override
	protected void onDestroy() {
		if (mWebView != null) {
			mWebView.clearHistory();
			mWebView.removeAllViewsInLayout();
			mWebView.clearDisappearingChildren();
			mWebView.clearFocus();
//			mWebView.clearView();
			mWebView.destroy();
		}
		super.onDestroy();
	}

	public void back(View v) {

		((Activity) context).finish();
	}
}
