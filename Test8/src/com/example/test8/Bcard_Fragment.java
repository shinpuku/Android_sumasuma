package com.example.test8;

import java.io.IOException;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.view.Display;
import android.view.WindowManager;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class Bcard_Fragment extends Fragment{

	public Bcard_Fragment() {}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//Bundleで保存されたデータを復元
		//第1引数:レイアウトXMLファイルのリソースID、
		//第2引数:
		//第3引数:trueにするかfalseにするかで戻り値となるルートビュー(View)が変わる。
		//       trueの場合には、第2引数で渡したViewGrou、falseの場合には第1引数で渡したリソースIDがルートビューになる
		View view;
		if(First_Activity.preference.getString("regist", "erorr").equals("1")){
			
			view = inflater.inflate(R.layout.bcard_layout, container, false);
			TextView point_amount = (TextView)view.findViewById(R.id.point_text);
			point_amount.setText(First_Activity.preference.getString("p_amount", "erorr"));
			
			ImageView code_image = (ImageView)view.findViewById(R.id.code_image);
			code_image.setImageDrawable(MainActivity.d);
			code_image.setScaleType(ImageView.ScaleType.FIT_CENTER);

		}else{
			view = inflater.inflate(R.layout.bcard_layout2, container, false);
		}
		
		return view;
	}

	public static Bcard_Fragment createInstance() {
		Bcard_Fragment fragment = new Bcard_Fragment();
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
