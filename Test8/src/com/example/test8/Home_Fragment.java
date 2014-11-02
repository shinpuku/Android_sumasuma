package com.example.test8;

import com.handmark.pulltorefresh.library.PullToRefreshWebView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("SetJavaScriptEnabled") public class Home_Fragment extends Fragment{

	PullToRefreshWebView mPullRefreshWebView;

	WebView myWebView;

	public Home_Fragment() {}

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



		//Bundleで保存されたデータを復元


		//第1引数:レイアウトXMLファイルのリソースID、
		//第2引数:
		//第3引数:trueにするかfalseにするかで戻り値となるルートビュー(View)が変わる。
		//       trueの場合には、第2引数で渡したViewGrou、falseの場合には第1引数で渡したリソースIDがルートビューになる
		View view = inflater.inflate(R.layout.home_layout, container, false);
		//
		//
		//     
//		mPullRefreshWebView = (PullToRefreshWebView)view.findViewById(R.id.wv);
		myWebView = (WebView)view.findViewById(R.id.wv);
//		myWebView = mPullRefreshWebView.getRefreshableView();
		myWebView.setWebViewClient(new WebViewClient());
		myWebView.getSettings().setAppCacheEnabled(true);
		myWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		myWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
		myWebView.getSettings().setJavaScriptEnabled(true);
		myWebView.setWebChromeClient(new WebChromeClient());
		myWebView.setWebViewClient(new WebViewClient());
//		myWebView.getSettings().setLoadWithOverviewMode(true);
//		myWebView.getSettings().setUseWideViewPort(true);
		myWebView.setVerticalScrollbarOverlay(true);

		myWebView.loadUrl("http://my-log.jp/sumasuma/?customer_id="+First_Activity.preference.getString("id", "0"));
		return view;
	}




	//	@Override
	//	 public void onResume() {
	//	        super.onResume();
	//	        try {
	//	            WebView.class.getMethod("onResume").invoke(myWebView);
	//	        } catch (Exception e) {
	//	            
	//	        }
	//	    }
	//
	//	    @Override
	//	    public void onPause() {
	//	        try {
	//	            WebView.class.getMethod("onPause").invoke(myWebView);
	//	        } catch (Exception e) {
	//	            
	//	        }
	//	        super.onPause();
	//	    }

	public static Home_Fragment createInstance() {
		Home_Fragment fragment = new Home_Fragment();
		// Fragment に渡す引数を詰めこむオブジェクト
		//        Bundle args = new Bundle();
		//        args.putInt("hoge", hoge);
		//
		//        // 詰め込んだオブジェクトを Fragment に渡す
		//        fragment.setArguments(args);

		// 新しいインスタンスを返す
		return fragment;
	}

//	private static class SampleWebViewClient extends WebViewClient {
//		@Override
//		public boolean shouldOverrideUrlLoading(WebView view, String url) {
//			view.loadUrl(url);
//			return true;
//		}
//	}

}
