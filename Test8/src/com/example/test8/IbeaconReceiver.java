package com.example.test8;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class IbeaconReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent data) {
		//	        Intent intent = new Intent(context, MainActivity.class);
		//	        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		//	        try {
		//	            pendingIntent.send();
		//	        } catch (PendingIntent.CanceledException e) {
		//	            e.printStackTrace();
		//	        }
		
		Log.i("iBeacon","ブロードキャストが実行されました");

			Intent intent = new Intent(context, IbeaconService.class); 
			context.startService(intent); 
			
			Log.i("iBeacon","完了");

	}

}
