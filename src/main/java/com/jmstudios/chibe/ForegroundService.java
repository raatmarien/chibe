package com.jmstudios.chibe;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ForegroundService extends Service {
	@Override
	public IBinder onBind (Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand (Intent intent, int flags, int startId) {
		NotificationChannel chan = new NotificationChannel (BuildConfig.APPLICATION_ID, "Chibing", NotificationManager.IMPORTANCE_NONE);
		chan.setLockscreenVisibility (Notification.VISIBILITY_PRIVATE);
		NotificationManager manager = (NotificationManager) getSystemService (Context.NOTIFICATION_SERVICE);
		manager.createNotificationChannel (chan);

		Notification.Builder notificationBuilder = new Notification.Builder (this, BuildConfig.APPLICATION_ID);
		@SuppressLint("WrongConstant")
		Notification notification = notificationBuilder
				.setOngoing (true)
				.setSmallIcon (R.drawable.ic_launcher)
				.setContentTitle ("Chibing")
				.setPriority (NotificationManager.IMPORTANCE_MIN)
				.setCategory (Notification.CATEGORY_SERVICE)
				.build ();
		startForeground (1, notification);

		new Handler (Looper.getMainLooper ()).postDelayed (new Runnable () {
			@Override
			public void run () {
				stopForeground (true);
				stopSelf ();
			}
		}, 1000);

		return super.onStartCommand (intent, flags, startId);
	}
}
