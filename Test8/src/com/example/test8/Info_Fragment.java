package com.example.test8;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Info_Fragment extends Fragment{
	
	public Info_Fragment() {}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      
        //Bundleで保存されたデータを復元
 
         
     //第1引数:レイアウトXMLファイルのリソースID、
     //第2引数:
     //第3引数:trueにするかfalseにするかで戻り値となるルートビュー(View)が変わる。
     //       trueの場合には、第2引数で渡したViewGrou、falseの場合には第1引数で渡したリソースIDがルートビューになる
     View view = inflater.inflate(R.layout.info_layout, container, false);
     //
     //
//     
     WebView myWebView = (WebView)view.findViewById(R.id.wv4);
     myWebView.setWebViewClient(new WebViewClient());
     myWebView.getSettings().setAppCacheEnabled(true);
     myWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
     myWebView.getSettings().setJavaScriptEnabled(true);
     myWebView.getSettings().setLoadWithOverviewMode(true);
     myWebView.getSettings().setUseWideViewPort(true);
     myWebView.loadUrl("http://my-log.jp/sumasuma/shop/?customer_id="+First_Activity.preference.getString("id", "0"));
        return view;
    }
	
	public static Info_Fragment createInstance() {
		Info_Fragment fragment = new Info_Fragment();
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
