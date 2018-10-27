package crundle.qralarmclock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import static android.widget.ArrayAdapter.*;

public class AlarmSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_settings);
    }

    /* Cancel changes
    *   - Go back to MainAlarmsActivity*/
    public void cancel(View view) {
        Intent intent;
        intent = new Intent(this, MainAlarmsActivity.class);
        startActivity(intent);
    }

    /* Save changes
     *   - Update alarm's settings, go back to MainAlarmsActivity*/
    public void save(View view) {
        // DO SAVE STUFF

        Intent intent;
        intent = new Intent(this, MainAlarmsActivity.class);
        startActivity(intent);
    }
}
