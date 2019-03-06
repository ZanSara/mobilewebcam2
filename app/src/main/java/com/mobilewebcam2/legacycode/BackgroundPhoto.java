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
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.mobilewebcam2.legacycode.PhotoSettings.Mode;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class BackgroundPhoto implements ITextUpdater
{
	WeakReference<Context> mContext = null;
	
	public void TakePhoto(Context context, SharedPreferences prefs, PhotoSettings.Mode mode, String event)
	{
		boolean ignoreinactivity = false; 
		long sincelastalive = System.currentTimeMillis() - MobileWebCam2.gLastMotionKeepAliveTime;
		long keepalivetime = PhotoSettings.getEditInt(context, prefs, "motion_keepalive_refresh", 3600);
		if(keepalivetime > 0 && sincelastalive >= keepalivetime * 1000)
		{
			MobileWebCam2.gLastMotionKeepAliveTime = System.currentTimeMillis();
			MobileWebCam2.LogI("Taking keep alive picture!");
			ignoreinactivity = true;
		}
		
		String startTime = prefs.getString("activity_starttime", "00:00");
		String endTime = prefs.getString("activity_endtime", "24:00");
		if(Preview.CheckInTime(new Date(), startTime, endTime, false))
		{
			if(mWakeLock == null || !mWakeLock.isHeld())
			{
				// get lock for one picture
				PowerManager pwrmgr = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
				mWakeLock = pwrmgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MobileWebCam2.PhotoAlarm");
			    mWakeLock.acquire();
			    
			    Log.v("MobileWebCam2", "PhotoAlarmWakeLock aquired!");
			}
			
			if(mode == Mode.HIDDEN)
			{
				ConnectivityManager connmgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
				if(connmgr.getNetworkPreference() == ConnectivityManager.TYPE_WIFI)
				{
					WifiManager wmgr = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
					if(mWifiLock == null || !mWifiLock.isHeld())
					{
						mWifiLock = wmgr.createWifiLock(WifiManager.WIFI_MODE_FULL, "MobileWebCam2.PhotoAlarm");
						mWifiLock.acquire();
						Log.v("MobileWebCam2", "WifiLock aquired!");
					}		
				}
				
				new PhotoService(context, this).TakePicture(event);
			}
			else if(!MobileWebCam2.gInSettings)
			{
				if(MobileWebCam2.gIsRunning)
				{
					Intent i = new Intent(context, MobileWebCam2.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					i.putExtra("alarm", "photo");
					i.putExtra("event", event);
					context.startActivity(i);
				}
				else if(mode == Mode.BACKGROUND)
				{
					Intent i = new Intent(context, TakeHiddenPicture.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_FROM_BACKGROUND);
					i.putExtra("alarm", "photo");
					i.putExtra("event", event);
					context.startActivity(i);
				}
			}
		}
	}	

	@Override
	public void UpdateText()
	{
		if(Preview.gPreview != null)
			Preview.gPreview.UpdateText();
	}
	
	// do a toast on the main thread from wherever we are!
	public static void Toast(final Context c, final String msg, final int length)
	{
		Handler h = new Handler(c.getMainLooper());
	    h.post(new Runnable()
	    {
	        @Override
	        public void run()
	        {
	             Toast.makeText(c, msg, Toast.LENGTH_LONG).show();
	        }
	    });
	}	

	@Override
	public void Toast(final String msg, int length)
	{
		synchronized(BackgroundPhoto.class)
		{
			final Context c = mContext.get();
			if(c != null)
			{
				SharedPreferences prefs = mContext.get().getSharedPreferences(MobileWebCam2.SHARED_PREFS_NAME, 0);
				if(!prefs.getBoolean("no_messages", false))
					Toast(c, msg, Toast.LENGTH_LONG);
			}
		}
		Log.i("MobileWebCam2", "BackgroundPhoto: " + msg);
	}
	
	@Override
	public void SetPreview(Bitmap image)
	{
		if(Preview.gPreview != null && MobileWebCam2.gIsRunning)
			Preview.gPreview.SetPreview(image);
	}
	
	private static PowerManager.WakeLock mWakeLock = null;
	private static WifiManager.WifiLock mWifiLock = null;
	
	private static AtomicInteger JobRefCount = new AtomicInteger(0);
	
	@Override
	public synchronized int JobStarted()
	{
		Log.i("MobileWebCam2", "JobStarted " + JobRefCount.get());
		return JobRefCount.getAndIncrement();
	}
	
	@Override
	public synchronized int JobFinished()
	{
		Log.i("MobileWebCam2", "JobFinished " + JobRefCount.get());
		
		int cnt = JobRefCount.decrementAndGet();
		if(cnt <= 0)
		{
			Log.i("MobileWebCam2", "Background Done!");
			
			if(Preview.gPreview != null)
				Preview.gPreview.JobFinished();
			
			releaseWakeLocks();
			
			if(Preview.mPhotoLock.getAndSet(false))
				Log.v("MobileWebCam2", "PhotoLock released because backgroundphoto job is finished!");
		}
		
		return cnt;
	}

	public static void releaseWakeLocks()
	{
		// release lock for one picture
		if(mWifiLock != null)
		{
			if(mWifiLock.isHeld())
				mWifiLock.release();
			Log.v("MobileWebCam2", "WifiLock released!");
			mWifiLock = null;
		}
		
		if(mWakeLock != null)
		{
			PowerManager.WakeLock tmp = mWakeLock;
			Log.v("MobileWebCam2", "PhotoAlarmWakeLock released!");
			mWakeLock = null;

			if(tmp.isHeld())
				tmp.release();
		}
	}
}