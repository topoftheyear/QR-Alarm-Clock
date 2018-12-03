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
import android.content.Context;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

//    Ringtone alarm = RingtoneManager.getRingtone(context, Uri.parse(a.getAlarm_id()));
//    AudioAttributes aa = new AudioAttributes.Builder()
//            .setUsage(AudioAttributes.USAGE_ALARM)
//            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//            .build();
//    alarm.setAudioAttributes(aa);
//    alarm.play();
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent intent_used = new Intent(context, ScanningCodeActivity.class);
        context.startActivity(intent_used);
    }

    @TargetApi(24)
    public void setAlarm(Context context, Alarm a) {
        boolean[] days = a.daysActive;
        int min = a.getMin();
        int hour = a.getHour();

        for (int i = 0; i < 7; i++) {
            if (!days[i])
                continue;
            Calendar calender = Calendar.getInstance();

            calender.set(Calendar.DAY_OF_WEEK, i + 1);  //here pass week number
            calender.set(Calendar.HOUR_OF_DAY, hour);  //pass hour which you have select
            calender.set(Calendar.MINUTE, min);  //pass min which you have select
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
            int _id = 0;
            Intent intent = new Intent(context, AlarmReceiver.class);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, _id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis(), pendingIntent);
            //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis(), 7 * 24 * 60 * 60 * 1000, pendingIntent);

        }
    }
}
