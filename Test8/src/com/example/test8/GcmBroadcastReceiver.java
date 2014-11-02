package com.example.test8;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver{
	
	public GcmBroadcastReceiver() {
        super();
    }
	
	 @Override
	    public void onReceive(Context context, Intent intent) {
	        // intentをGcmIntentServiceで処理する
	        ComponentName comp = new ComponentName(context.getPackageName(),
	                GcmIntentService.class.getName());
	        // サービスを起動、サービス動作中はWakeLockを保持する
	        startWakefulService(context, (intent.setComponent(comp)));
	        setResultCode(Activity.RESULT_OK);
	    }

}
