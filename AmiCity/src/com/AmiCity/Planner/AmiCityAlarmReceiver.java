package com.AmiCity.Planner;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

// The class has to extend the BroadcastReceiver to get the notification from the system
public class AmiCityAlarmReceiver extends BroadcastReceiver {

@Override
 public void onReceive(Context context, Intent paramIntent) {

 // Request the notification manager
 NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

 // Create a new intent which will be fired if you click on the notification
 Intent intent = new Intent(context,HomeScreen.class);

 // Attach the intent to a pending intent
 PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

 String notificationText = "";
 Bundle receivedParams = paramIntent.getExtras();
 if(receivedParams != null)
 {
	 notificationText = receivedParams.getString("description");
	 
 }
 
 // Create the notification
 Notification notification = new Notification.Builder(context)
 .setContentTitle("You need to take a look at this task")
 .setContentText(notificationText)
 .setSmallIcon(R.drawable.ic_action_time)
 .setContentIntent(pendingIntent)
 .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
 .getNotification();

 // Fire the notification
 notificationManager.notify(1, notification);
 }

}