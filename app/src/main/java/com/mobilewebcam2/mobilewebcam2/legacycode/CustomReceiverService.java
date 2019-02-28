package com.mobilewebcam2.mobilewebcam2.legacycode;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class CustomReceiverService extends Service implements OnSharedPreferenceChangeListener
{
	static public void start(Context c)
	{
		Intent i = new Intent(c, CustomReceiverService.class);  
		c.startService(i);
	}
	
	static public void stop(Context c)
	{
    	SharedPreferences prefs = c.getSharedPreferences(MobileWebCam2.SHARED_PREFS_NAME, 0);
		MobileWebCam2.LogI("Stop custom broadcast receiver service: " + prefs.getString("cam_broadcast_activation", ""));
   	 	Intent i = new Intent(c, CustomReceiverService.class);  
		c.stopService(i);
	}
	
	@Override
	public void onStart(Intent intent, int startId)
	{
		super.onStart(intent, startId);
		
    	SharedPreferences prefs = CustomReceiverService.this.getSharedPreferences(MobileWebCam2.SHARED_PREFS_NAME, 0);
		mBroadcastReceiver = prefs.getString("cam_broadcast_activation", "");

    	if(MobileWebCam2.gCustomReceiverActive)
    	{
	        try
	        {
	        	unregisterReceiver(gCustomReceiver);
		        MobileWebCam2.gCustomReceiverActive = false;
	        }
	        catch(IllegalArgumentException e)
	        {
	        	if(e.getMessage() != null)
	        		MobileWebCam2.LogE(e.getMessage());
	        	else
	        		MobileWebCam2.LogE(e.toString());
	        }
    	}

        if(!MobileWebCam2.gCustomReceiverActive && mBroadcastReceiver.length() > 0)
		{
        	IntentFilter filter = new IntentFilter(mBroadcastReceiver);
	        registerReceiver(gCustomReceiver, filter);
	        if(!prefs.getBoolean("no_messages", false))
	        	Toast.makeText(CustomReceiverService.this, "Registered broadcast receiver: " + mBroadcastReceiver, Toast.LENGTH_LONG).show();
	        MobileWebCam2.LogI("Registered broadcast receiver: " + mBroadcastReceiver);
	        MobileWebCam2.gCustomReceiverActive = true;
		}
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();

        if(MobileWebCam2.gCustomReceiverActive)
        {
	        MobileWebCam2.LogI("Destroyed broadcast receiver: " + mBroadcastReceiver);
	        try
	        {
				unregisterReceiver(gCustomReceiver);
		        MobileWebCam2.gCustomReceiverActive = false;
	        }
	        catch(IllegalArgumentException e)
	        {
	        	if(e.getMessage() != null)
	        		MobileWebCam2.LogE(e.getMessage());
	        	else
	        		MobileWebCam2.LogE(e.toString());
	        }
        }
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private String mBroadcastReceiver = "";
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
	{
		String old = mBroadcastReceiver;
		mBroadcastReceiver = sharedPreferences.getString("cam_broadcast_activation", "");
		
		if(key.equals("cam_broadcast_activation"))
		{
			if(!old.equals(mBroadcastReceiver))
				start(CustomReceiverService.this);
		}
	}
	
    static public final BroadcastReceiver gCustomReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
        	Log.i("MobileWebCam2", "Broadcast received: " + intent.getAction());
        	
	        SharedPreferences prefs = context.getSharedPreferences(MobileWebCam2.SHARED_PREFS_NAME, 0);
        	String action = prefs.getString("cam_broadcast_activation", "");
    		if(intent.getAction().equals(action))
    		{
    			int cnt = PhotoSettings.getEditInt(context, prefs, "cam_intents_repeat", 1);
    			ControlReceiver.EventPhoto(context, prefs, cnt, intent.getAction());
    		}
        }
    };		
}