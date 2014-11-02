package com.example.test8;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;

public class IbeaconIntentService extends IntentService{

	public IbeaconIntentService(String name) {
		super(name);
	}

	public IbeaconIntentService() {
		super("IbeaconIntentService");
	}

	@Override
	protected void onHandleIntent(Intent data) {

		//        SystemClock.sleep(5000);
		// 
		//        Intent intent = new Intent();
		//        intent.setAction("RECEIVE_IBEACON");
		//        sendBroadcast(intent);

		try {
			Thread.sleep(10000);

			Intent broadcastIntent = new Intent();
			broadcastIntent.setAction("MY_ACTION");
			getBaseContext().sendBroadcast(broadcastIntent);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


}
