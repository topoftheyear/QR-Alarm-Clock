package crundle.qralarmclock;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

import static android.media.RingtoneManager.*;

public class AlarmSettingsActivity extends AppCompatActivity{

    Spinner ringtonesSpinner;
    String uri, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_settings);

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
    }

    /* Activate/Deactivate RepeatedDays
     * JUST CHANGES COLOR at the moment
     * Should eventually set relevant alarm's settings
     */
    public void toggleDay(View view){
        TextView tv = (TextView) view;

        // Get day number from TextView tag, convert to int
        Log.d((String) tv.getTag(),"oi 12345");
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
    public void save(View view) throws IOException, ClassNotFoundException {
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
        a.setMin(tp.getCurrentMinute());
        int hour = tp.getCurrentHour();
        if(hour > 12) {
            a.setHour(hour - 12);
            a.setAM(false);
        }
        else{
            a.setHour(hour);
            a.setAM(true);
        }

        // get the days that are active
        for (int i = 0; i < days.getChildCount(); i++) {
            View v = days.getChildAt(i);

            if (v instanceof TextView) {
                int tag = Integer.parseInt((String)v.getTag());
                if(((TextView) v).getCurrentTextColor() == Color.parseColor("#FF0000")) {
                    a.setDaysActive(tag);
                }
            }
        }
        String uriString = (uri + "/" + id);
        a.setAlarm_id(uriString);

        AlarmReceiver aReceiver = new AlarmReceiver();
        a.setAlarmTime(Integer.toString(tp.getCurrentHour()) + ":" + min);
        a.setActive(true);
        Toast.makeText(this, "Alarm set", Toast.LENGTH_LONG).show();
        aReceiver.setAlarm(this, a);
        saveFile(a);


        Intent intent;
        intent = new Intent(this, MainAlarmsActivity.class);
        startActivity(intent);
    }

    public void saveFile(Alarm alarm) throws IOException, ClassNotFoundException {
        ArrayList<Alarm> alarms = getAlarms();
        boolean success = false;
        for(int i = 0; i < alarms.size();i++){
            Alarm a = alarms.get(i);
            if(alarm.getAlarmTime().equals(a.getAlarmTime())){
                alarms.set(i, alarm);
                success = true;
            }
        }
        if(!success) {
            alarms.add(alarm);
        }

        Collections.sort(alarms);

        FileOutputStream fos = openFileOutput("AlarmList.txt", Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);

        for(Alarm a:alarms){
            os.writeObject(a);
        }

        os.close();
        fos.close();
    }

    public ArrayList<Alarm> getAlarms() throws IOException, ClassNotFoundException {
        ArrayList<Alarm> alarms = new ArrayList<Alarm>();
        File directory = this.getFilesDir();
        File file = new File(directory, "AlarmList.txt");
        Boolean keepGoing = true;
        FileInputStream fi = null;
        ObjectInputStream oi = null;
        try{
            fi = new FileInputStream(file);
            oi = new ObjectInputStream(fi);
            while(keepGoing){
                alarms.add((Alarm) oi.readObject());
            }
        }
        catch (EOFException e) {
            keepGoing = false;
        }
        if(fi != null)
            fi.close();
        if(oi != null)
            oi.close();


        return alarms;
    }



}
