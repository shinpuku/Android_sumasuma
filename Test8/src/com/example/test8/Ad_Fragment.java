package com.example.test8;

import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Ad_Fragment extends Fragment{
	
	public Ad_Fragment() {}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      
        //Bundleで保存されたデータを復元
 
         
     //第1引数:レイアウトXMLファイルのリソースID、
     //第2引数:
     //第3引数:trueにするかfalseにするかで戻り値となるルートビュー(View)が変わる。
     //trueの場合には、第2引数で渡したViewGrou、falseの場合には第1引数で渡したリソースIDがルートビューになる
     View view = inflater.inflate(R.layout.ad_layout, container, false);
     //
     //
//     
     WebView myWebView = (WebView)view.findViewById(R.id.wv2);
     myWebView.setWebViewClient(new WebViewClient());
     myWebView.getSettings().setAppCacheEnabled(true);
     myWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
     myWebView.getSettings().setJavaScriptEnabled(true);
     myWebView.setWebChromeClient(new WebChromeClient());
     myWebView.setWebViewClient(new WebViewClient());
     myWebView.getSettings().setLoadWithOverviewMode(true);
     myWebView.getSettings().setUseWideViewPort(true);
     myWebView.getSettings().setBuiltInZoomControls(true);
     myWebView.loadUrl("http://my-log.jp/sumasuma/ad/?customer_id="+First_Activity.preference.getString("id", "0"));
        return view;
    }
	
	public static Ad_Fragment createInstance() {
        Ad_Fragment fragment = new Ad_Fragment();
        // Fragment に渡す引数を詰めこむオブジェクト
//        Bundle args = new Bundle();
//        args.putInt("hoge", hoge);
//
//        // 詰め込んだオブジェクトを Fragment に渡す
//        fragment.setArguments(args);

        // 新しいインスタンスを返す
        return fragment;
    }

}
