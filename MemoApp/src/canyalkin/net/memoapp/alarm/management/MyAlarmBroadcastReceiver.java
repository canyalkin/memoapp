package canyalkin.net.memoapp.alarm.management;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import canyalkin.net.memoapp.AskActivity;
import canyalkin.net.memoapp.MemoAppMainActivity;
import canyalkin.net.memoapp.R;

public class MyAlarmBroadcastReceiver extends BroadcastReceiver {
	
	private static final String TAG="MyAlarmBroadcastReceiver"; 

	@Override
	public void onReceive(Context context, Intent intent) {
		
		Log.d("MyAlarmBroadcastReceiver", "alarm received");
		/*Intent intent2 = new Intent(context, AskActivity.class);
		intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent2);*/
		
		// Instantiate a Builder object.
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		// Creates an Intent for the Activity
		
		builder.setSmallIcon(R.drawable.ic_question_mark)
	    .setContentTitle("MemoApp")
	    .setContentText("Memorize it!")
	    .setAutoCancel(false).setOngoing(true);
		Intent newIntent =
		        new Intent(context, AskActivity.class);
		// Sets the Activity to start in a new, empty task
		//newIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP & Intent.FLAG_ACTIVITY_NEW_TASK);
		newIntent.setFlags(PendingIntent.FLAG_ONE_SHOT);
		// Creates the PendingIntent
		
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		
		stackBuilder.addParentStack(AskActivity.class); 
		stackBuilder.addNextIntent(newIntent);
		
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0, PendingIntent.FLAG_UPDATE_CURRENT );
		builder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		mNotificationManager.notify(AskActivity.NOTIFICATION_ID, builder.build());
		

	}
	
	public void setOnetimeTimer(Context context){
		final int MILLI_TO_HOUR = 1000 * 60 * 60;

		AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

		Intent intent = new Intent(context, MyAlarmBroadcastReceiver.class);

		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
		
		Calendar now = Calendar.getInstance();
		Log.d(TAG, "now:"+now);
		int hour = now.get(Calendar.HOUR_OF_DAY);
		
		long nextAlarmTime;
		if(hour>9 && hour < 22 ){
			nextAlarmTime =  SystemClock.elapsedRealtime() + MILLI_TO_HOUR;
		}else{
			long nowInMs = now.getTimeInMillis();
			if(hour >=22 && hour<=23){
				Calendar c=Calendar.getInstance();
				c.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)+1,9,0,0);
				nextAlarmTime= SystemClock.elapsedRealtime() + (c.getTimeInMillis()-nowInMs) ;
			}else{
				Calendar c=Calendar.getInstance();
				c.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH),9,0,0);
				Log.d(TAG, "calculated time :"+(c.getTimeInMillis()-nowInMs)) ;
				nextAlarmTime= SystemClock.elapsedRealtime() + (c.getTimeInMillis()-nowInMs) ;
			}
		}

		am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, nextAlarmTime, pi);
		Log.d(TAG, "alarm set...");
	}

}
