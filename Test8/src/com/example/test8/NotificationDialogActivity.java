package com.example.test8;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class NotificationDialogActivity extends /*Fragment*/Activity{

	public static String message= "nodata";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent i = this.getIntent();//表示テキストを受け取る
		message = i.getStringExtra("msg");
		//		NotificationDialogFragment fragment = new NotificationDialogFragment();
		//		fragment.show(getSupportFragmentManager(), "Notification_alert_dialog");

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		// アラートダイアログのタイトルを設定します
		alertDialogBuilder.setTitle("スマスマカードアプリ");
		// アラートダイアログのメッセージを設定します
		alertDialogBuilder.setMessage(message);
		// アラートダイアログの肯定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
		alertDialogBuilder.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});

		// アラートダイアログのキャンセルが可能かどうかを設定します
		alertDialogBuilder.setCancelable(true);
		AlertDialog alertDialog = alertDialogBuilder.create();
		// アラートダイアログを表示します
		alertDialog.show();

	}
	
	@Override
	public void onStop() {
		super.onStop();
		finish();
	}



}
