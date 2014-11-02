package com.example.test8;

import java.io.IOException;
import java.io.StringReader;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


public class First_Activity extends FragmentActivity implements OnClickListener{

	public static final int color = R.color.action_bar_background;
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private CharSequence mTitle;

	public static SharedPreferences preference;
	public static Editor editor;

	Intent intent;
	ProgressDialog _pd;

	First_Fragment first_fragment;
	Second_Fragment second_fragment;
	private final First_Activity self = this;

	Context context;
	GoogleCloudMessaging gcm;
	AtomicInteger msgId = new AtomicInteger();
	SharedPreferences prefs;
	String regid;
	Boolean regi_flg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		try {
			// ここで2秒間スリープし、スプラッシュを表示させたままにする。
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}
		// 通常時のテーマをセットする。
		setTheme(R.style.NormalTheme);

		super.onCreate(savedInstanceState);

		preference =  PreferenceManager.getDefaultSharedPreferences(this);

		editor = preference.edit();

		startService(new Intent(self, IbeaconIntentService.class));

		setContentView(R.layout.first_layout);
		first_fragment = First_Fragment.createInstance();
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.add(R.id.FragmentContainer2, first_fragment);
		transaction.commit();



		setContentView(R.layout.first_layout);
		mTitle = "利用規約";
		setActionBar();

		context = getApplicationContext();

		if (checkPlayServices()) {

			Log.i("GCM_TAG", "Google Play Services APKが見つかりました");
			gcm = GoogleCloudMessaging.getInstance(this);
			regid = getRegistrationId(context);
			if (regid.isEmpty()) {
				registerInBackground(preference.getString("regist", "0").equals("1"));
				Log.i("TAG_登録", preference.getString("regist", "0"));
			}
		} else {
			Log.i("GCM_TAG", "Google Play Services APKが見つかりません");
		}

		second_fragment = Second_Fragment.createInstance();

		if (preference.getBoolean("Launched", false)==false) {	//初回起動の判別
			//初回起動時の処理  
			
			//プリファレンスの書き変え  
			editor.putBoolean("Launched", true);  
			editor.commit();
			//初期化
			editor.putString("regist", "0");
			editor.commit();  
			editor.putString("id", "0");
			editor.commit(); 
			editor.putString("redi_id", "0");
			editor.commit(); 
			editor.putString("image_url", "0");
			editor.commit(); 
			editor.putString("p_amount", "0");
			editor.commit(); 
			editor.putInt("n_num", 0);
			editor.commit(); 
		} else {
			//二回目以降の処理  
			Intent i = new Intent(this, MainActivity.class);
			startActivity(i);
			finish();
		}  
	}

	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGCMPreferences(context);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (registrationId.isEmpty()) {
			Log.i("TAG", "Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i("TAG", "App version changed.");
			return "";
		}
		return registrationId;
	}

	private SharedPreferences getGCMPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences, but
		// how you store the regID in your app is up to you.
		return getSharedPreferences(First_Activity.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}

	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	private void registerInBackground(final Boolean sendFlg) {

		new AsyncTask<Void, Void, String>() {
			
			@Override
			protected String doInBackground(Void... params) {
//				showProgress();
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					String regid = gcm.register("1003184300450");
					msg = regid;
					editor.putString("redi_id", msg);
					editor.commit();
					Log.d("tag", "Device registered, registration ID=" + msg);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				// registration IDを取得
				// 従来であれば、ここから送信サーバーへregistration IDを送信するような流れになる
				if(sendFlg){
//					sendID(msg);
				}
			}
		}.execute(null, null, null);
	}

	public void sendID(String id){
		MainActivity.md5_code = Transform_md5.transform_Md5(Transform_md5.getTimeNow() + MainActivity.MD5_BOTTOM_CODE);
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet request = new HttpGet(MainActivity.PUSH_URL
				+First_Activity.preference.getString("id", "0")
				+ MainActivity.RESTRICTOR
				+ MainActivity.md5_code
				+ MainActivity.REGISTRATION
				+ First_Activity.preference.getString("redi_id", "0")
				);

		Log.d("URL", MainActivity.PUSH_URL
				+First_Activity.preference.getString("id", "0")
				+ MainActivity.RESTRICTOR
				+ MainActivity.md5_code
				+ MainActivity.REGISTRATION
				+ First_Activity.preference.getString("redi_id", "0"));

		HttpResponse httpResponse = null;
		try {
			httpResponse = httpClient.execute(request);
			Log.d("HttpSampleActivity", "送られたよ");
		} catch (Exception e) {
			Log.d("HttpSampleActivity", "Error Execute");
		}

		//		int status = httpResponse.getStatusLine().getStatusCode();
		//		 
		//		if (HttpStatus.SC_OK == status) {
		//		    try {
		//		        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		//		        httpResponse.getEntity().writeTo(outputStream);
		//		        parse(outputStream.toString());
		//		    } catch (Exception e) {
		//		        Log.d("HttpSampleActivity", "Error");
		//		    }
		//		} else {
		//		    Log.d("HttpSampleActivity", "Status" + status);
		//		}


	}
	void parse(String str) {
		try{
			XmlPullParser xmlPullParser = Xml.newPullParser();
			xmlPullParser.setInput(new StringReader(str));

			int eventType;
			while ((eventType = xmlPullParser.next()) != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG && "title".equals(xmlPullParser.getName())) {
					Log.d("HttpSampleActivity", xmlPullParser.nextText());
				}
			}
		} catch (Exception e){
			Log.d("HttpSampleActivity", "Error:parse");
		}
	}


	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.next_button:
			//showDialog();
			FragmentManager manager = getSupportFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();

			transaction.replace(R.id.FragmentContainer2, second_fragment);
			transaction.remove(first_fragment);
			transaction.commit();
			break;

		case R.id.d_button1:
			moveActivity(1);

			break;

		case R.id.d_button2:
			moveActivity(2);
			break;

		}


	}

	public void setActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);

		Drawable backgroundDrawable = getApplicationContext().getResources().getDrawable(color);
		actionBar.setBackgroundDrawable(backgroundDrawable);

	}

	public void moveActivity(int num){

		if(num==1){
			intent = new Intent(this, ReadBarcode_Activity.class);
		}else{
			intent = new Intent(this, MainActivity.class);
		}
		startActivity(intent);
		finish();
	}

	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i("GCM_TAG", "Playサービスがサポートされていない端末です");
				finish();
			}
			return false;
		}
		return true;
	}

}
