package crundle.qralarmclock;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    /*
     * Code that executes when the alarm time comes
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context, "ITS WORKING", Toast.LENGTH_LONG).show();
        String alarmTime = intent.getStringExtra("time");
        Alarm a = new Alarm();
        for(int i = 0; i < AlarmSettingsActivity.alarms.size(); i++){
            if(AlarmSettingsActivity.alarms.get(i).getAlarmTime().equals(alarmTime)){
                a = AlarmSettingsActivity.alarms.get(i);
                break;
            }
        }
        setAndroidAlarm(context, a);
        Intent intent_used = new Intent(context, ScanningCodeActivity.class);
        context.startActivity(intent_used);


    }

    /*
     * Sets the alarm in the Android System
     */
    @TargetApi(24)
    public static void setAndroidAlarm(Context context, Alarm a) {
        boolean[] days = a.daysActive;
        int min = a.getMin();
        int hour = a.getHour();
        String alarmTime = a.getAlarmTime();

        for (int i = 0; i < 7; i++) {
            if (!days[i])
                continue;
            Calendar calender = Calendar.getInstance();

            calender.set(Calendar.DAY_OF_WEEK, i + 1);  //here pass week number
            calender.set(Calendar.HOUR_OF_DAY, hour);  //pass hour which you have select
            calender.set(Calendar.MINUTE, min);  //pass min which you have select
            calender.set(Calendar.SECOND, 0);
            calender.set(Calendar.MILLISECOND, 0);

            if(a.getAM())
                calender.set(Calendar.AM_PM, 0);
            else
                calender.set(Calendar.AM_PM, 1);

            Calendar now = Calendar.getInstance();
            now.set(Calendar.SECOND, 0);
            now.set(Calendar.MILLISECOND, 0);

            if (calender.before(now)) {    //this condition is used for future reminder that means your reminder not fire for past time
                calender.add(Calendar.DATE, 7);
            }

            //final int _id = (int) System.currentTimeMillis();  //this id is used to set multiple alarms
            String id =Integer.toString(calender.get(Calendar.HOUR_OF_DAY)) + "0" + Integer.toString(calender.get(Calendar.MINUTE)) + "1" + Integer.toString(calender.get(Calendar.DAY_OF_WEEK)) + "2" + Integer.toString(calender.get(Calendar.AM_PM));
            int _id = Integer.parseInt(id);
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra("time", alarmTime);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, _id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis(), pendingIntent);

        }
    }

    /*
     * Deletes the alarm in the Android System
     */
    @TargetApi(24)
    public static void deleteAndroidAlarm(Context context, Alarm a){
        boolean[] days = a.daysActive;
        int min = a.getMin();
        int hour = a.getHour();
        String alarmTime = a.getAlarmTime();

        for (int i = 0; i < 7; i++) {
            if (!days[i])
                continue;
            Calendar calender = Calendar.getInstance();

            calender.set(Calendar.DAY_OF_WEEK, i + 1);  //here pass week number
            calender.set(Calendar.HOUR_OF_DAY, hour);  //pass hour which you have select
            calender.set(Calendar.MINUTE, min);  //pass min which you have select
            calender.set(Calendar.SECOND, 0);
            calender.set(Calendar.MILLISECOND, 0);

            if(a.getAM())
                calender.set(Calendar.AM_PM, 0);
            else
                calender.set(Calendar.AM_PM, 1);


            //final int _id = (int) System.currentTimeMillis();  //this id is used to set multiple alarms
            String id =Integer.toString(calender.get(Calendar.HOUR_OF_DAY)) + "0" + Integer.toString(calender.get(Calendar.MINUTE)) + "1" + Integer.toString(calender.get(Calendar.DAY_OF_WEEK)) + "2" + Integer.toString(calender.get(Calendar.AM_PM));
            int _id = Integer.parseInt(id);
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra("time", alarmTime);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, _id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

            alarmManager.cancel(pendingIntent);

        }
    }
}
