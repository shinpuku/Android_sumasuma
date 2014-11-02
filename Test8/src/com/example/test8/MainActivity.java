package com.example.test8;

import java.io.IOException;


import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.app.Activity;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Main
 * 
 * @author bratech
 *
 */

public class MainActivity extends FragmentActivity
implements NavigationDrawerFragment.NavigationDrawerCallbacks ,OnClickListener{

	/**
	 * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;
	public static final int color = R.color.action_bar_background;

	/**
	 * Used to store the last screen title. For use in {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
	public static int g_position;
	public RadioButton rbtn;
	RadioGroup group;

	public static final String RSS_FEED_NUM_URL = "http://my-log.jp/sumasuma/bcard_check_push_registration_api?customer_id=";
	public static final String PUSH_URL = "http://my-log.jp/sumasuma/push_registration_api/?customer_id=";
	public static final String BARCODE_INFO_URL = "http://my-log.jp/sumasuma/bcard_info_api/?customer_id=";
	public static final String RESTRICTOR = "&restrictor_code=";
	public static final String REGISTRATION = "&gcm_registration_id=";
	public static final String MD5_BOTTOM_CODE = "bratechsumasuma";
	public static final String RSS_FEED_CODE_URL = "http://my-log.jp/sumasuma/wp-json/posts";
	public static String JSON_CHECK_NUM="0";
	public static String TEMP_USER_ID="";
	public static String md5_code= "";

	public static Drawable d = null;
	Home_Fragment home_fragment;
	Ad_Fragment ad_fragment;
	Present_Fragment present_fragment;
	Bcard_Fragment bcard_fragment;
	Setting_Fragment setting_fragment;
	Info_Fragment info_fragment;
	Policy_Fragment policy_fragment;
	Help_Fragment help_fragment;
	RadioButton radio1,radio2,radio3,radio4;

	IbeaconReceiver receiver;
	IntentFilter intentFilter;


	JsonLoader task = new JsonLoader("" ,this,true);  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.NormalTheme);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		group = (RadioGroup)findViewById(R.id.radioGroup2);

		mNavigationDrawerFragment = (NavigationDrawerFragment)
				getFragmentManager().findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(
				R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));

		home_fragment    = Home_Fragment.createInstance();
		ad_fragment      = Ad_Fragment.createInstance();
		present_fragment = Present_Fragment.createInstance();
		bcard_fragment   = Bcard_Fragment.createInstance();
		setting_fragment = Setting_Fragment.createInstance();
		info_fragment    = Info_Fragment.createInstance();
		policy_fragment  = Policy_Fragment.createInstance();
		help_fragment    = Help_Fragment.createInstance();

		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.add(R.id.FragmentContainer, home_fragment);
		transaction.commit();

		Time time = new Time("Asia/Tokyo");
		time.setToNow();
		String date = String.valueOf(time.year) + String.valueOf(time.month+1) + String.valueOf(time.monthDay) + String.valueOf(time.hour);
		Log.d("date",date);

		startService(new Intent(MainActivity.this, IbeaconService.class));

		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
		loadCardInfo();

		First_Activity.editor.putInt("n_num", 0);
		First_Activity.editor.commit();

		receiver = new IbeaconReceiver();
		intentFilter = new IntentFilter();
		intentFilter.addAction("RECEIVE_IBEACON");
		registerReceiver(receiver, intentFilter); 

	}


	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction()
		.replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
		.commit();
	}

	public void onSectionAttached(int number) {

		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();

		switch (number) {
		case 1:
			group.check(R.id.tab_button0);
			mTitle = getString(R.string.title_section1);
			transaction.replace(R.id.FragmentContainer, home_fragment);
			transaction.commit();
			break;

		case 2:
			group.check(R.id.tab_button1);
			mTitle = getString(R.string.title_section2);
			transaction.replace(R.id.FragmentContainer, ad_fragment);
			transaction.commit();
			break;

		case 3:
			group.check(R.id.tab_button2);
			mTitle = getString(R.string.title_section3);
			transaction.replace(R.id.FragmentContainer, present_fragment);
			transaction.commit();
			break;
		case 4:
			group.check(R.id.tab_button3);
			mTitle = getString(R.string.title_section4);
			transaction.replace(R.id.FragmentContainer, bcard_fragment);
			transaction.commit();
			break;

		case 5:
			group.clearCheck();
			mTitle = getString(R.string.title_section5);
			transaction.replace(R.id.FragmentContainer, info_fragment);
			transaction.commit();
			break;

		case 6:
			group.clearCheck();
			mTitle = getString(R.string.title_section6);
			transaction.replace(R.id.FragmentContainer, setting_fragment);
			transaction.commit();
			break;

		case 7:
			group.clearCheck();
			mTitle = getString(R.string.title_section7);
			transaction.replace(R.id.FragmentContainer, policy_fragment);
			transaction.commit();
			break;

		case 8:
			group.clearCheck();
			mTitle = getString(R.string.title_section8);
			transaction.replace(R.id.FragmentContainer, help_fragment);
			transaction.commit();
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);

		Drawable backgroundDrawable = getApplicationContext().getResources().getDrawable(color);
		actionBar.setBackgroundDrawable(backgroundDrawable);

	}

	public void loadCardInfo(){
		md5_code = Transform_md5.transform_Md5(Transform_md5.getTimeNow() + MainActivity.MD5_BOTTOM_CODE);
		if(First_Activity.preference.getString("regist", "0").equals("1")){
			task.execute(BARCODE_INFO_URL
					+ First_Activity.preference.getString("id", "0")
					+ RESTRICTOR
					+ md5_code
					);
		}


	}

	public void getBarcodeImage(){
		if(!First_Activity.preference.getString("image_url", "0").equals("0")){
			try {
				URL url = new URL(First_Activity.preference.getString("image_url","not found"));
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				Log.d("画像URL",First_Activity.preference.getString("image_url","not found"));
				InputStream istream = connection.getInputStream(); 
				d = Drawable.createFromStream(istream, "codeimg");
				istream.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}


		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		//		int id = item.getItemId();
		//		if (id == R.id.action_settings) {
		//			return true;
		//		}

		if (item.getItemId() == R.id.action_example) {

			g_position=5;
			NavigationDrawerFragment.mDrawerListView.setItemChecked(g_position, true);
			group.clearCheck();
			mTitle = getString(R.string.title_section6);

			FragmentManager manager = getSupportFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			transaction.replace(R.id.FragmentContainer, setting_fragment);
			transaction.commit();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();

		switch (v.getId()) {

		case R.id.tab_button0:
			g_position=0;
			NavigationDrawerFragment.mDrawerListView.setItemChecked(g_position, true);
			mTitle = getString(R.string.title_section1);
			restoreActionBar();
			transaction.replace(R.id.FragmentContainer, home_fragment);
			transaction.commit();


			break;

		case R.id.tab_button1:
			g_position=1;
			NavigationDrawerFragment.mDrawerListView.setItemChecked(g_position, true);
			mTitle = getString(R.string.title_section2);
			restoreActionBar();
			transaction.replace(R.id.FragmentContainer, ad_fragment);
			transaction.commit();
			break;

		case R.id.tab_button2:
			g_position=2;
			NavigationDrawerFragment.mDrawerListView.setItemChecked(g_position, true);
			mTitle = getString(R.string.title_section3);
			restoreActionBar();
			transaction.replace(R.id.FragmentContainer, present_fragment);
			transaction.commit();
			break;

		case R.id.tab_button3:
			g_position=3;
			NavigationDrawerFragment.mDrawerListView.setItemChecked(g_position, true);
			mTitle = getString(R.string.title_section4);
			restoreActionBar();
			transaction.replace(R.id.FragmentContainer, bcard_fragment);
			transaction.commit();

			break;

		case R.id.signUp_button:
			Intent i = new Intent(this,ReadBarcode_Activity.class);
			startActivity(i);
			break;


		case R.id.change_button:
			Intent intent = new Intent(this,ReadBarcode_Activity.class);
			startActivity(intent);
			break;

		}
	}
	
	@Override
	public void onStop(){
		super.onStop();
		Toast.makeText(this, "終了するよ", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		Toast.makeText(this, "終了しました", Toast.LENGTH_SHORT).show();
		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction("RECEIVE_IBEACON");
		sendBroadcast(broadcastIntent);
		
//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			// TODO 自動生成された catch ブロック
//			e.printStackTrace();
//		}
		unregisterReceiver(receiver);
		//		startService(new Intent(MainActivity.this, IbeaconService.class));
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section
		 * number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container, false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(
					getArguments().getInt(ARG_SECTION_NUMBER));
		}
	}
}
