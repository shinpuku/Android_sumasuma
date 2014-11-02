package com.example.test8;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.example.test8.CameraPreview;
import com.example.test8.R;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.FrameLayout;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Size;
import android.graphics.drawable.Drawable;

/* Import ZBar Class files */
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import net.sourceforge.zbar.Config;

/**
 * カメラを起動して1次元バーコードを読み取るクラス
 * @author bratech
 */

public class ReadBarcode_Activity extends Activity{

	private Camera mCamera;
	private CameraPreview mPreview;
	private Handler autoFocusHandler;
	private CharSequence mTitle;

	ImageScanner scanner;

	private boolean barcodeScanned = false;
	private boolean previewing = true;

	static {
		System.loadLibrary("iconv");
	} 
	
	JsonLoader task = new JsonLoader("" ,this,true);  

	public void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.NormalTheme);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.camera_layout1);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		autoFocusHandler = new Handler();
		mCamera = getCameraInstance();

		/* Instance barcode scanner */
		scanner = new ImageScanner();
		scanner.setConfig(0, Config.X_DENSITY, 3);
		scanner.setConfig(0, Config.Y_DENSITY, 3);

		mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
		FrameLayout preview = (FrameLayout)findViewById(R.id.cameraPreview);
		preview.addView(mPreview);

		mTitle = "バーコードを読み取ります";
		setActionBar();

	}

	public void onPause() {
		super.onPause();
		releaseCamera();
	}

	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance(){
		Camera c = null;
		try {
			c = Camera.open();
		} catch (Exception e){
		}
		return c;
	}

	private void releaseCamera() {
		if (mCamera != null) {
			previewing = false;
			mCamera.setPreviewCallback(null);
			mCamera.release();
			mCamera = null;
		}
	}

	private Runnable doAutoFocus = new Runnable() {
		public void run() {
			if (previewing)
				mCamera.autoFocus(autoFocusCB);
		}
	};

	PreviewCallback previewCb = new PreviewCallback() {
		public void onPreviewFrame(byte[] data, Camera camera) {
			Camera.Parameters parameters = camera.getParameters();
			Size size = parameters.getPreviewSize();

			Image barcode = new Image(size.width, size.height, "Y800");
			barcode.setData(data);

			int result = scanner.scanImage(barcode);
			String str = "";

			if (result != 0) {
				previewing = false;
				mCamera.setPreviewCallback(null);
				mCamera.stopPreview();

				SymbolSet syms = scanner.getResults();
				for (Symbol sym : syms) {
					str += sym.getData();
					barcodeScanned = true;
				}
				
				checkId(String.valueOf(str));
			}
		}
	};

	// Mimic continuous auto-focusing
	AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
		public void onAutoFocus(boolean success, Camera camera) {
			autoFocusHandler.postDelayed(doAutoFocus, 1000);
		}
	};
	
	/**
	 * アクションバーの初期設定
	 */
	public void setActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);

		Drawable backgroundDrawable = getApplicationContext().getResources().getDrawable(MainActivity.color);
		actionBar.setBackgroundDrawable(backgroundDrawable);
	}
	
	/**
	 * アクティビティを移動し、このアクティビティは閉じる
	 */
	public void moveActivity(){
		Intent i = new Intent(this, Middle_Activity.class);
		startActivity(i);
		finish();
	}
	
	/**
	 * 読み取ったバーコードより、登録されたBカードのものか判別する
	 * JsonLoaderに渡し、GET送信するし、JSONで結果を受け取る
	 * @param id 読み取ったバーコードの数字の文字列
	 */
	
	public void checkId(String id){
		MainActivity.TEMP_USER_ID = id;
		MainActivity.JSON_CHECK_NUM = "0";
		Log.d("id確認",id);
		MainActivity.md5_code = Transform_md5.transform_Md5(Transform_md5.getTimeNow() + MainActivity.MD5_BOTTOM_CODE);

		task.execute(MainActivity.RSS_FEED_NUM_URL
				+ MainActivity.TEMP_USER_ID
				+ MainActivity.RESTRICTOR
				+ MainActivity.md5_code
				+ MainActivity.REGISTRATION
				+ First_Activity.preference.getString("redi_id", "0")
				);

		Log.d("URL",MainActivity.RSS_FEED_NUM_URL
				+ MainActivity.TEMP_USER_ID
				+ MainActivity.RESTRICTOR
				+ MainActivity.md5_code
				+ MainActivity.REGISTRATION
				+ First_Activity.preference.getString("redi_id", "0")
				);
	}

}
