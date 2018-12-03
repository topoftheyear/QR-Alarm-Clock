package crundle.qralarmclock;
import android.Manifest;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

public class MainAlarmsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private static boolean firstOpen = true;

    /*
     * Makes sure the internal alarm list gets populated on start
     * Displays the Main Alarms Activity Page
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(firstOpen) {
            try {
                AlarmSettingsActivity.alarms = AlarmSettingsActivity.getSavedAlarms(this);
                firstOpen = false;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_alarms);
        File directory = this.getFilesDir();
        File file = new File(directory, "AlarmList.txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setupRecyclerView();
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 1);

    }

    /*
     * Sets up recycler view to display all the alarms
     */
    private void setupRecyclerView(){
        recyclerView  = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new AlarmAdapter(AlarmSettingsActivity.alarms));
    }

    /*
     * Opens settings for new alarm creation
     */
    public void openSettings(View view) {
        Intent intent;
        intent = new Intent(this, AlarmSettingsActivity.class);
        startActivity(intent);
    }

    /*
     * Open settings of an existing alarm
     */
    public void openEditSettings(View view) {
        Intent intent;
        intent = new Intent(this, AlarmSettingsActivity.class);

        TextView alarmTimeView = view.findViewById(R.id.tv_alarm_time);
        TextView alarmAmPmView = view.findViewById(R.id.am_pm);
        boolean single = false;
        String first = Character.toString(alarmTimeView.getText().charAt(0));
        String second = Character.toString(alarmTimeView.getText().charAt(1));
        String third = Character.toString(alarmTimeView.getText().charAt(2));
        String fourth = Character.toString(alarmTimeView.getText().charAt(3));
        String fifth = null;
        if(second.equals(":")) {
            single = true;
        }
        else{
            fifth = Character.toString(alarmTimeView.getText().charAt(4));
        }
        String alarmHour;
        String alarmMin;
        if(single){
            alarmHour = first;
            alarmMin = third + fourth;
        }
        else{
            alarmHour = first + second;
            alarmMin = fourth + fifth;
        }
        String amPM = alarmAmPmView.getText().toString();
        String alarmTime;

        if(amPM.equals("AM"))
            alarmTime = alarmHour + ":" + alarmMin;
        else
            alarmTime = Integer.toString(Integer.parseInt(alarmHour) + 12) + ":" + alarmMin;

        intent.putExtra("alarm", alarmTime);
        startActivity(intent);
    }

    /*
     * Code to execute when pressing the X to delete an alarm
     * Refreshes the RecyclerView to display changes
     */
    public void onDelete(View view){

        TextView alarmTimeView = ((FrameLayout)view.getParent()).findViewById(R.id.tv_alarm_time);
        TextView alarmAmPmView = ((FrameLayout)view.getParent()).findViewById(R.id.am_pm);
        boolean single = false;
        String first = Character.toString(alarmTimeView.getText().charAt(0));
        String second = Character.toString(alarmTimeView.getText().charAt(1));
        String third = Character.toString(alarmTimeView.getText().charAt(2));
        String fourth = Character.toString(alarmTimeView.getText().charAt(3));
        String fifth = null;
        if(second.equals(":")) {
            single = true;
        }
        else{
            fifth = Character.toString(alarmTimeView.getText().charAt(4));
        }
        String alarmHour;
        String alarmMin;
        if(single){
            alarmHour = first;
            alarmMin = third + fourth;
        }
        else{
            alarmHour = first + second;
            alarmMin = fourth + fifth;
        }
        String amPM = alarmAmPmView.getText().toString();
        String alarmTime;
        if(amPM.equals("AM"))
            alarmTime = alarmHour + ":" + alarmMin;
        else
            alarmTime = Integer.toString(Integer.parseInt(alarmHour) + 12) + ":" + alarmMin;
        Alarm a = new Alarm();
        for(int i = 0; i < AlarmSettingsActivity.alarms.size(); i++){
            if(AlarmSettingsActivity.alarms.get(i).getAlarmTime().equals(alarmTime)){
                a = AlarmSettingsActivity.alarms.get(i);
                break;
            }
        }
        AlarmSettingsActivity.deleteAlarm(this, a);
        setupRecyclerView();
    }

    /*
     * Code to execute when toggling an alarm on or off
     * Changes the alarm objects settings and saves them
     */
    public void onToggle(View view){
        Switch toggle = (Switch)view;
        boolean on = toggle.isChecked();


        TextView alarmTimeView = ((FrameLayout)view.getParent()).findViewById(R.id.tv_alarm_time);
        TextView alarmAmPmView = ((FrameLayout)view.getParent()).findViewById(R.id.am_pm);
        boolean single = false;
        String first = Character.toString(alarmTimeView.getText().charAt(0));
        String second = Character.toString(alarmTimeView.getText().charAt(1));
        String third = Character.toString(alarmTimeView.getText().charAt(2));
        String fourth = Character.toString(alarmTimeView.getText().charAt(3));
        String fifth = null;
        if(second.equals(":")) {
            single = true;
        }
        else{
            fifth = Character.toString(alarmTimeView.getText().charAt(4));
        }
        String alarmHour;
        String alarmMin;
        if(single){
            alarmHour = first;
            alarmMin = third + fourth;
        }
        else{
            alarmHour = first + second;
            alarmMin = fourth + fifth;
        }
        String amPM = alarmAmPmView.getText().toString();
        String alarmTime;
        if(amPM.equals("AM"))
            alarmTime = alarmHour + ":" + alarmMin;
        else
            alarmTime = Integer.toString(Integer.parseInt(alarmHour) + 12) + ":" + alarmMin;

        Alarm a = new Alarm();
        for(int i = 0; i < AlarmSettingsActivity.alarms.size(); i++){
            if(AlarmSettingsActivity.alarms.get(i).getAlarmTime().equals(alarmTime)){
                AlarmSettingsActivity.alarms.get(i).setActive(on);
                a = AlarmSettingsActivity.alarms.get(i);
                AlarmSettingsActivity.saveFile(this);
                break;
            }
        }

        if(on)
            AlarmReceiver.setAndroidAlarm(this, a);
        else
            AlarmReceiver.deleteAndroidAlarm(this, a);
    }
}
