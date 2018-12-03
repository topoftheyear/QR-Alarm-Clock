package crundle.qralarmclock;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class AlarmSettingsActivity extends AppCompatActivity {
    public static ArrayList<Alarm> alarms = new ArrayList<Alarm>();
    private Alarm currentAlarm = null;
    private Integer currentAlarmIndex = null;

    @Override
    @TargetApi(23)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_settings);
        Intent intent = getIntent();
        if (intent.hasExtra("alarm")) {
            String alarmTime = intent.getStringExtra("alarm");
            for (int i = 0; i < alarms.size(); i++) {
                if (alarms.get(i).getAlarmTime().equals(alarmTime)) {
                    currentAlarm = alarms.get(i);
                    currentAlarmIndex = i;
                }
            }
            TimePicker tp = (TimePicker) findViewById(R.id.timePicker1);
            LinearLayout daysView = (LinearLayout) findViewById(R.id.linearLayout2);

            if (currentAlarm.getAM())
                tp.setHour(currentAlarm.getHour());
            else
                tp.setHour(currentAlarm.getHour() + 12);
            tp.setMinute(currentAlarm.getMin());

            boolean[] days = currentAlarm.daysActive;
            for (int i = 0; i < daysView.getChildCount(); i++) {
                View v = daysView.getChildAt(i);
                if (days[i]) {
                    ((TextView) v).setTextColor(Color.parseColor("#FF0000"));
                } else {
                    ((TextView) v).setTextColor(Color.parseColor("#800000"));
                }

            }
        } else {
            currentAlarm = null;
            currentAlarmIndex = null;
        }
    }

    /* Activate/Deactivate RepeatedDays
     * JUST CHANGES COLOR at the moment
     * Should eventually set relevant alarm's settings
     */
    public void toggleDay(View view) {
        TextView tv = (TextView) view;

        // Get day number from TextView tag, convert to int
        Log.d((String) tv.getTag(), "oi 12345");
        int dayNum = Integer.parseInt((String) tv.getTag()); //nasty

        if (tv.getCurrentTextColor() == Color.parseColor("#800000")) {
            tv.setTextColor(Color.parseColor("#FF0000"));
        } else {
            tv.setTextColor(Color.parseColor("#800000"));
        }
    }

    /* Cancel changes
     *   - Go back to MainAlarmsActivity
     */
    public void cancel(View view) {
        Intent intent;
        intent = new Intent(this, MainAlarmsActivity.class);
        startActivity(intent);
    }

    /* Save changes
     *   - Update alarm's settings, go back to MainAlarmsActivity
     */
    public void save(View view){
        Alarm a = new Alarm();

        TimePicker tp = (TimePicker) findViewById(R.id.timePicker1);
        LinearLayout days = (LinearLayout) findViewById(R.id.linearLayout2);
        // getCurrentMinute will return only 1 digit when <10,
        // so time ends up looking like "5:0"
        // Add in a 0 before single digit minutes
        String min = Integer.toString(tp.getCurrentMinute());
        if (min.length() == 1) {
            min = "0" + min;
        }
        int hour = tp.getCurrentHour();


        // get the days that are active
        for (int i = 0; i < days.getChildCount(); i++) {
            View v = days.getChildAt(i);

            if (v instanceof TextView) {
                int tag = Integer.parseInt((String) v.getTag());
                if (((TextView) v).getCurrentTextColor() == Color.parseColor("#FF0000")) {
                    a.setDaysActive(tag);
                }
            }
        }

        if (hour > 12) {
            a.setHour(hour - 12);
            a.setAM(false);
        } else {
            a.setHour(hour);
            a.setAM(true);
        }

        a.setMin(tp.getCurrentMinute());
        a.setAlarmTime(Integer.toString(tp.getCurrentHour()) + ":" + min);
        Log.e("DEBUGGING", Integer.toString(tp.getCurrentHour()) + ":" + min);
        Toast.makeText(this, "Alarm Saved", Toast.LENGTH_LONG).show();

        for (int i = 0; i < alarms.size(); i++) {
            if (alarms.get(i).getAlarmTime().equals(tp.getCurrentHour() + ":" + min)) {
                deleteAlarm(this, alarms.get(i));
                break;
            }
        }

        a.setActive(true);
        if (!(currentAlarm == null)) {
            a.setActive(currentAlarm.isActive());
            deleteAlarm(this, currentAlarm);
        }
        alarms.add(a);
        if(a.isActive()) {
            AlarmReceiver.setAndroidAlarm(this, a);

            Log.e("DEBUGGING", "alarm set");
        }
        saveFile(this);

        Intent intent;
        intent = new Intent(this, MainAlarmsActivity.class);
        startActivity(intent);
    }

    public static void saveFile(Context context) {
        Collections.sort(alarms);

        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput("AlarmList.txt", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);

            for (Alarm a : alarms) {
                os.writeObject(a);
            }

            os.close();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Alarm> getSavedAlarms(Context context) throws IOException, ClassNotFoundException {
        ArrayList<Alarm> tempAlarms = new ArrayList<Alarm>();
        File directory = context.getFilesDir();
        File file = new File(directory, "AlarmList.txt");
        Boolean keepGoing = true;
        FileInputStream fi = null;
        ObjectInputStream oi = null;
        try {
            fi = new FileInputStream(file);
            oi = new ObjectInputStream(fi);
            while (keepGoing) {
                tempAlarms.add((Alarm) oi.readObject());
            }
        } catch (EOFException e) {
            keepGoing = false;
        }
        if (fi != null)
            fi.close();
        if (oi != null)
            oi.close();


        return tempAlarms;
    }

    public static void deleteAlarm(Context context, Alarm a) {
        alarms.remove(a);
        AlarmReceiver.deleteAndroidAlarm(context, a);
        saveFile(context);
    }
}
