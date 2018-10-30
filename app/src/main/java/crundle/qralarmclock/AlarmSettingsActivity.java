package crundle.qralarmclock;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import static android.widget.ArrayAdapter.*;

public class AlarmSettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_settings);
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

        if (tv.getCurrentTextColor() == getResources().getColor(R.color.dayNotSelected)) {
            tv.setTextColor(getResources().getColor(R.color.daySelected));
        } else {
            tv.setTextColor(getResources().getColor(R.color.dayNotSelected));
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

        // DO SAVE STUFF
        // Make mock alarm
        MockData md = new MockData();
        Alarm a = new Alarm();

        TimePicker tp = (TimePicker) findViewById(R.id.timePicker1);

        a.setAlarmTime(Integer.toString(tp.getCurrentHour()) + ":" + Integer.toString(tp.getCurrentMinute()));
        a.setActive(true);
        doSave(a);
        md.addAlarmToMockData(a);

        Intent intent;
        intent = new Intent(this, MainAlarmsActivity.class);
        startActivity(intent);
    }

    public void doSave(Alarm alarm) throws IOException, ClassNotFoundException {
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

        File alarmList = new File(this.getFilesDir(), "AlarmList.txt");
        if (alarmList.createNewFile())
        {
            System.out.println("File is created!");
        } else {
            System.out.println("File already exists.");
        }

        FileOutputStream fos = openFileOutput("AlarmList.txt", Context.MODE_PRIVATE);;
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
        FileInputStream fi = new FileInputStream(file);
        ObjectInputStream oi = new ObjectInputStream(fi);
        Boolean keepGoing = true;
        try{
            while(keepGoing){
                alarms.add((Alarm) oi.readObject());
            }
        }
        catch (EOFException e) {
            keepGoing = false;
        }

        fi.close();
        oi.close();
        return alarms;
    }
}
