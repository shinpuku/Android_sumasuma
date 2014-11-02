package com.example.test8;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

/**
 * バーコードを読み取った後、結果をダイアログで表示するだけのアクティビティ
 * @author bratech
 *
 */
public class Middle_Activity extends Activity{

	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setTheme(R.style.NormalTheme);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.middle_layout);
		showDialog();
	}
	
	/**
	 * ダイアログ表示メソッド
	 * 登録された場合と、失敗した場合をif文により分岐
	 * 失敗した場合は「やり直す」ボタンを押せば、カメラでバーコードを読む段階からやり直せる
	 */
	public void showDialog(){

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

		// ダイアログの設定
		alertDialog.setTitle("スマスマカードアプリ");

		if(MainActivity.JSON_CHECK_NUM.equals("1")){

			First_Activity.editor.putString("id", MainActivity.TEMP_USER_ID);	
			First_Activity.editor.commit();

			alertDialog.setMessage("ポイントカードは、無事に登録されました。\nそれでは、アプリを十分にご活用ください。");

			// OK(肯定的な)ボタンの設定
			alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// OKボタン押下時の処理
					moveActivity(true);
				}
			});

		}else{
			alertDialog.setMessage("失敗");
			//			alertDialog.setView(layout);

			// OK(肯定的な)ボタンの設定
			alertDialog.setPositiveButton("やり直す", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// OKボタン押下時の処理
					moveActivity(false);
				}
			});

			//NG(否定的な)ボタンの設定
			alertDialog.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// NGボタン押下時の処理
					moveActivity(true);
				}
			});
		}
		alertDialog.setCancelable(false);
		alertDialog.show();
	}

	/**
	 * アクティビティを移動する
	 * flgがtrueの場合はMainActivityへ、falseの場合はReadBarcode_Activityへ
	 * @param flg やり直すかどうかの真理値
	 */
	public void moveActivity(Boolean flg){
		if(flg){
			intent = new Intent(this, MainActivity.class);
		}else{
			intent = new Intent(this, ReadBarcode_Activity.class);
		}
		startActivity(intent);
		finish();
	}
}
