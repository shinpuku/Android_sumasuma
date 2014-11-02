package com.example.test8;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class NotificationDialogFragment extends DialogFragment{
	
//	NotificationDialogActivity activity = (NotificationDialogActivity) getActivity();

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("スマスマカードアプリ");
		builder.setMessage(NotificationDialogActivity.message);
		builder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
//				Intent i = new Intent(activity, MainActivity.class);
//				startActivity(i);
//				activity.finish();
			}
		});

		Dialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(true);
		return dialog;
	}

//	@Override
//	public void onStop() {
//		
//		super.onStop();
//		activity.finish();
//	}
}
