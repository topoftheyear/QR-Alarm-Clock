package crundle.qralarmclock;

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

import java.sql.Time;

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
    public void save(View view) {
        // DO SAVE STUFF

        // Make mock alarm
        MockData md = new MockData();
        Alarm a = new Alarm();

        TimePicker tp = (TimePicker) findViewById(R.id.timePicker1);

        // getCurrentMinute will return only 1 digit when <10,
        // so time ends up looking like "5:0"
        // Add in a 0 before single digit minutes
        String min = Integer.toString(tp.getCurrentMinute());
        if (min.length() == 1) {
            min = "0" + min;
        }

        a.setAlarmTime(Integer.toString(tp.getCurrentHour()) + ":" + min);
        a.setActive(true);

        md.addAlarmToMockData(a);

        Intent intent;
        intent = new Intent(this, MainAlarmsActivity.class);
        startActivity(intent);
    }
}
