package com.example.locationapp;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class MyReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent arg1) {
		 Intent service = new Intent(context, MyService.class);
	        context.startService(service);
	        Log.d("BPH:UMS", " Service loaded at Boot-up");
	}

}