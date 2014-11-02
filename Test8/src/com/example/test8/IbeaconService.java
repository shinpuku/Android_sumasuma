package com.example.test8;

import android.app.Service;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.widget.Toast;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;

public class IbeaconService extends Service{

	private final static String TAG = "TryService#";

	// Toastを何回表示されたか数えるためのカウント
	private int mCount = 0;

	// Toastを表示させるために使うハンドラ
	private Handler mHandler = new Handler();

	// スレッドを停止するために必要
	private boolean mThreadActive = true;


	@Override
	public IBinder onBind(Intent arg0) {
		final BluetoothManager bluetoothManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
		BluetoothAdapter mBluetoothAdapter = bluetoothManager.getAdapter();
		mBluetoothAdapter.startLeScan(mLeScanCallback);
		return null;

	}

	private void showText(final String text) {
		showText(this, text);
	}

	private void showText(Context ctx, final String text) {
		Toast.makeText(this, TAG + text, Toast.LENGTH_SHORT).show();
	}

	

	@Override   // onCreate:サービスが作成されたときに呼びされる(最初に1回だけ)
	public void onCreate() {
		this.showText("サービスが開始されました。");
		final BluetoothManager bluetoothManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
		BluetoothAdapter mBluetoothAdapter = bluetoothManager.getAdapter();
		mBluetoothAdapter.startLeScan(mLeScanCallback);
		
		
	}
	
	@Override   // onStartCommand:
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
         
        // 戻り値でサービスが強制終了されたときの挙動が変わる
        // START_NOT_STICKY,START_REDELIVER_INTENT,START_STICKY_COMPATIBILITY
        return Service.START_STICKY;
    }
	
	@Override   // onDestroy:
    public void onDestroy() {
		super.onDestroy();
		Log.i("iBeacon","サービスの終了");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
    }
	
	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
     
            //ここに結果に対して行う処理を記述する
        	Log.i("iBeacon","検出されました");
        	mHandler.post(new Runnable() {

				@Override
				public void run() {
					mCount++;
					showText("検出されました");
				}
			});
     
        }
    };

}
