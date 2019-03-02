/* Copyright 2012 Michael Haar

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package com.mobilewebcam2.legacycode;

import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.SharedPreferences;

import java.lang.ref.WeakReference;

import com.mobilewebcam2.mobilewebcam2.R;
import com.mobilewebcam2.legacycode.PhotoSettings.Mode;

import android.app.PendingIntent;
import android.content.Intent;
import android.app.Notification;
import android.app.NotificationManager;

public class PhotoReceiver extends BroadcastReceiver
{
	private BackgroundPhoto mBGPhoto = new BackgroundPhoto();
	
	@Override
	public void onReceive(Context context, Intent intent)
	{
		SharedPreferences prefs = context.getSharedPreferences(MobileWebCam2.SHARED_PREFS_NAME, 0);
		if(!prefs.getBoolean("mobilewebcam_enabled", true))
			return;
		
		if(prefs.getBoolean("lowbattery_pause", false) && WorkImage.getBatteryLevel(context) < PhotoSettings.gLowBatteryPower)
		{
			MobileWebCam2.LogI("Battery low ... pause");
			return;
		}
		
		synchronized(BackgroundPhoto.class)
		{
			mBGPhoto.mContext = new WeakReference<Context>(context);
		}
		
		StartNotification(context);
		
        MobileWebCamHttpService.start(context);
        String broadcast = prefs.getString("cam_broadcast_activation", "");
		if(broadcast.length() > 0 && !MobileWebCam2.gCustomReceiverActive)
		{
			MobileWebCam2.LogE("Error: " + broadcast + " receiver not active but it should ... Restarting now!");
			CustomReceiverService.start(context);
		}
		
        PhotoSettings.Mode mode = PhotoSettings.getCamMode(prefs);
        mBGPhoto.TakePhoto(context, prefs, mode, intent.getStringExtra("event"));		
	}
	
	public static final int ACTIVE_ID = 1;

	public static void StartNotification(Context c)
	{
	    SharedPreferences prefs = c.getSharedPreferences(MobileWebCam2.SHARED_PREFS_NAME, 0);
        PhotoSettings.Mode mode = PhotoSettings.getCamMode(prefs);

	    String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager)c.getSystemService(ns);

		int icon = R.drawable.notification_icon;
		String broadcast_action = prefs.getString("cam_broadcast_activation", "");
		CharSequence tickerText[] = { "MobileWebCam2 background mode active", "MobileWebCam2 semi background mode active", "MobileWebCam2 broadcastreceiver '" + broadcast_action + "' active" };
		long when = System.currentTimeMillis();

		CharSequence txt = tickerText[0];
		if(broadcast_action.length() > 0)
			txt = tickerText[2]; // broadcast background mode
		else if(mode == Mode.BACKGROUND)
			txt = tickerText[1];
		Notification notification = new Notification(icon, txt, when);
		notification.flags |= Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;

		CharSequence contentTitle = "MobileWebCam2 Active";
		CharSequence contentText[] =  { "Hidden Mode", "Semi Background Mode" };
		txt =  contentText[0];
		if(mode == Mode.BACKGROUND)
			txt = contentText[1];
	    if(prefs.getBoolean("server_enabled", true))
	    {
			String myIP = RemoteControlSettings.getIpAddress(c, true);
			if(myIP != null)
				txt = txt.subSequence(0, txt.length() - 5) + " http://" + myIP + ":" + MobileWebCamHttpService.getPort(prefs);
	    }
		Intent notificationIntent = new Intent(c, MobileWebCam2.class);
		PendingIntent contentIntent = PendingIntent.getActivity(c, 0, notificationIntent, 0);

		notification.setLatestEventInfo(c, contentTitle, txt, contentIntent);

		mNotificationManager.notify(ACTIVE_ID, notification);
	}

	public static void StopNotification(Context c)
	{
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager)c.getSystemService(ns);
		mNotificationManager.cancel(ACTIVE_ID);
	}
}