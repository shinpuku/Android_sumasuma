package com.example.test8;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class Setting_Fragment extends Fragment{
	
	public Setting_Fragment() {}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      
        //Bundleで保存されたデータを復元
 
         
     //第1引数:レイアウトXMLファイルのリソースID、
     //第2引数:
     //第3引数:trueにするかfalseにするかで戻り値となるルートビュー(View)が変わる。
     //       trueの場合には、第2引数で渡したViewGrou、falseの場合には第1引数で渡したリソースIDがルートビューになる
     View view = inflater.inflate(R.layout.setting_layout, container, false);
     //
     //
//     

        return view;
    }
	
	public static Setting_Fragment createInstance() {
		Setting_Fragment fragment = new Setting_Fragment();
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
