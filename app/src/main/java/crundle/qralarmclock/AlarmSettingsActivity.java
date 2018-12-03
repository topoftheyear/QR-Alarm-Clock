package crundle.qralarmclock;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class AlarmSettingsActivity extends AppCompatActivity {
    public static ArrayList<Alarm> alarms = new ArrayList<Alarm>();
    private Alarm currentAlarm = null;
    private Integer currentAlarmIndex = null;
    private Spinner ringtonesSpinner;
    private static String id, uri, uri_used;
    /*
     * Sets the screen to the alarm settings page. It will set the information to display the proper
     * information from the alarm object and be edit a selected alarm.
     */
    @Override
    @TargetApi(23)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_settings);
        Intent intent = getIntent();
        if (intent.hasExtra("alarm")) {
            String alarmTime = intent.getStringExtra("alarm");
            //finding the alarm being edited
            for (int i = 0; i < alarms.size(); i++) {
                if (alarms.get(i).getAlarmTime().equals(alarmTime)) {
                    currentAlarm = alarms.get(i);
                    currentAlarmIndex = i;
                }
            }
            TimePicker tp = (TimePicker) findViewById(R.id.timePicker1);
            tp.setIs24HourView(true);
            LinearLayout daysView = (LinearLayout) findViewById(R.id.linearLayout2);

            //setting timepicker attributes of the alarm settings display
            if (currentAlarm.getAM())
                tp.setHour(currentAlarm.getHour());
            else
                tp.setHour(currentAlarm.getHour() + 12);
            tp.setMinute(currentAlarm.getMin());

            //setting days active attributes of the alarm settings display
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
            //in the case of a new alarm being created
            currentAlarm = null;
            currentAlarmIndex = null;
        }
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        RingtoneManager ringtoneMgr = new RingtoneManager(this);
        ringtoneMgr.setType(RingtoneManager.TYPE_ALARM);
        Cursor alarmsCursor = ringtoneMgr.getCursor();

        String[] from = {alarmsCursor.getColumnName(RingtoneManager.TITLE_COLUMN_INDEX)};
        int[] to = {android.R.id.text1};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, alarmsCursor, from, to);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ringtonesSpinner = (Spinner)findViewById(R.id.spinner);
        ringtonesSpinner.setAdapter(adapter);
        id = alarmsCursor.getString(RingtoneManager.ID_COLUMN_INDEX);
        uri = alarmsCursor.getString(RingtoneManager.URI_COLUMN_INDEX);
        uri_used = alarmsCursor.getString(RingtoneManager.URI_COLUMN_INDEX) + "/" + alarmsCursor.getString(RingtoneManager.ID_COLUMN_INDEX);
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

    /*
     * Save changes
     *   - Update alarm's settings, save file, go back to MainAlarmsActivity
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

        //setting various alarm settings
        if (hour > 12) {
            a.setHour(hour - 12);
            a.setAM(false);
        } else {
            a.setHour(hour);
            a.setAM(true);
        }

        a.setAlarm_id(uri_used);

        a.setMin(tp.getCurrentMinute());
        a.setAlarmTime(Integer.toString(tp.getCurrentHour()) + ":" + min);

        //Message of successful alarm save
        Toast.makeText(this, "Alarm Saved", Toast.LENGTH_LONG).show();

        //checks if an alarm with the same time exists and deletes it
        for (int i = 0; i < alarms.size(); i++) {
            if (alarms.get(i).getAlarmTime().equals(tp.getCurrentHour() + ":" + min)) {
                deleteAlarm(this, alarms.get(i));
                break;
            }
        }

        //checks if the alarm was toggled off before editing, if so it remains off
        a.setActive(true);
        if (!(currentAlarm == null)) {
            a.setActive(currentAlarm.isActive());
            deleteAlarm(this, currentAlarm);
        }
        alarms.add(a);

        //creates the Android alarm if it is active
        if(a.isActive()) {
            AlarmReceiver.setAndroidAlarm(this, a);
        }

        //saving to file
        saveFile(this);

        //return to Main Alarms Activity
        Intent intent;
        intent = new Intent(this, MainAlarmsActivity.class);
        startActivity(intent);
    }

    /*
     * Sorts and saves the alarms for use between sessions
     */
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

    /*
     * Returns an ArrayList of the alarms from the saved AlarmList file
     */
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

    /*
     * Deletes an alarm from the internal list, the saved file, and the Android alarms
     */
    public static void deleteAlarm(Context context, Alarm a) {
        alarms.remove(a);
        AlarmReceiver.deleteAndroidAlarm(context, a);
        saveFile(context);
    }
}
