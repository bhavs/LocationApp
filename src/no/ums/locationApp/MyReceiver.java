package no.ums.locationApp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 
 * MyReceiver is used in order to start-up Location Service on bootup 
 *
 */
public class MyReceiver extends BroadcastReceiver {

	 @Override
     public void onReceive(Context context, Intent arg1) {
              Intent service = new Intent(context, LocationService.class);
             context.startService(service);
             Log.d("BPH:UMS", " Service loaded at Boot-up");
     }

}
