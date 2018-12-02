package crundle.qralarmclock;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class AlarmManagerClass extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_settings);

        scheduleAlarm();
    }

    public void scheduleAlarm() {
        /* Request the AlarmManagerClass object */
        android.app.AlarmManager manager = (android.app.AlarmManager) getSystemService(Context.ALARM_SERVICE);

        /* Create the PendingIntent that will launch the BroadcastReceiver */
        PendingIntent pending = PendingIntent.getBroadcast(this, 0, new Intent(this, AlarmReceiver.class), 0);

        /* Schedule Alarm with and authorize to WakeUp the device during sleep */
        manager.set(android.app.AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5 * 1000, pending);
    }
}
