package com.example.test8;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class GcmIntentService extends IntentService{

	private static final String TAG = "GcmIntentService";

	public GcmIntentService() {
		super(GcmIntentService.class.getName());
	}

	public GcmIntentService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();

		// intentからGCMのメッセージを取得する
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String messageType = gcm.getMessageType(intent);

		// GCMのメッセージをタイプ別にフィルタリングする。
		//  将来的に拡張されることを考慮し、存在しないタイプを無視するようにする
		if (!extras.isEmpty()) {
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) { 
				// エラー
				Log.d(TAG,"messageType: " + messageType + ",send error:" + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
				// サーバー側でメッセージを削除された
				Log.d(TAG,"messageType: " + messageType + ",message deleted:" + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) { 
				// メッセージ受信
				Log.d(TAG,"messageType: " + messageType + ",received message:" + extras.toString());
				sendNotification(parseMessage(extras.toString()));
			}
		}
		// 処理の終了をReceiverに通知し、ロックを解放する
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void sendNotification(String msg){

		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
		Notification n = new Notification(); // Notificationの生成
		n.icon = R.drawable.notification_icon; // アイコンの設定
		n.tickerText = msg;					//テキストの設定
//		n.number = First_Activity.preference.getInt("n_num", 0);	//通知の番号
//
			Intent i = new Intent(getApplicationContext(), First_Activity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
			n.setLatestEventInfo(getApplicationContext(), "スマスマカードアプリ", msg, pi);
//
//		//バイブレーションの設定
		n.defaults |= Notification.DEFAULT_VIBRATE;
		n.defaults |= Notification.DEFAULT_SOUND;
//		
		nm.notify(1, n); // 設定したNotificationを通知する
//		First_Activity.editor.putInt("n_num", First_Activity.preference.getInt("n_num", 0)+1);
//		First_Activity.editor.commit(); 
		
		Intent intent = new Intent(getApplicationContext(), NotificationDialogActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("msg", msg);		//表示テキストを送る
		startActivity(intent);
	}
	
	private String parseMessage(String msg){
		
		int topIndex = msg.indexOf("message=");
		msg = msg.substring(topIndex+"message=".length());
		int bottomIndex = msg.indexOf(", android");
		msg = msg.substring(0,bottomIndex);
		
		return msg;
	}
}
