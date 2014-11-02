package com.example.test8;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Toast;
import android.text.format.Time;

public class Camera_Activity extends Activity{

	public static final int color = R.color.action_bar_background;
	private CharSequence mTitle;
//	JsonLoader task = new JsonLoader("" ,this,true);  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//        super.onCreate(savedInstanceState);
		//        mSurfaceView = new SurfaceView(this);
		//        mSurfaceView.setOnClickListener(onClickListener);
		//        setContentView(mSurfaceView);
		setTheme(R.style.NormalTheme);
		super.onCreate(savedInstanceState);

		// Hide status-bar
		//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// Hide title-bar, must be before setContentView
		//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(new Camera_Preview(this));
		mTitle = "バーコードを読み取ります";
		setActionBar();

	}

	public void setActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);


		Drawable backgroundDrawable = getApplicationContext().getResources().getDrawable(color);
		actionBar.setBackgroundDrawable(backgroundDrawable);

	}
	public void moveActivity(){
		Intent i = new Intent(this, Middle_Activity.class);
		startActivity(i);
		finish();
	}

	public void checkId(String id){
		MainActivity.TEMP_USER_ID = id;
		MainActivity.md5_code = Transform_md5.transform_Md5(getTimeNow() + MainActivity.MD5_BOTTOM_CODE);

//		task.execute(MainActivity.RSS_FEED_NUM_URL
//				+ MainActivity.TEMP_USER_ID
//				+ MainActivity.RESTRICTOR
//				+ MainActivity.md5_code
//				+ MainActivity.REGISTRATION
//				+ First_Activity.preference.getString("redi_id", "0")
//				);

		Log.d("URL",MainActivity.RSS_FEED_NUM_URL
				+ MainActivity.TEMP_USER_ID
				+ MainActivity.RESTRICTOR
				+ MainActivity.md5_code
				+ MainActivity.REGISTRATION
				+ First_Activity.preference.getString("redi_id", "0")
				);


		//		Toast.makeText(this,MainActivity.RSS_FEED_NUM_URL+MainActivity.USER_ID
		//				+MainActivity.restrictor,Toast.LENGTH_LONG).show();
		//		
		//		Toast.makeText(this,First_Activity.registered.getString("regist", "erorr"),Toast.LENGTH_LONG).show();
		//		str = MainActivity.JSON_CHECK_NUM;
		//		
		//		if(First_Activity.registered.getString("regist", "erorr").equals("1")){
		//			Toast.makeText(this,"認証",Toast.LENGTH_LONG).show();
		//		}else{
		//			Toast.makeText(this,"否認",Toast.LENGTH_LONG).show();
		//		}
		//		moveActivity();
		//		finish();
	}

	public static String getTimeNow(){

		DateFormat df = new SimpleDateFormat("yyyyMMddHH");
		Date date = new Date(System.currentTimeMillis());
		Log.d("date",df.format(date));
		return df.format(date);
	}
}
