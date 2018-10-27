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

//    Spinner spinner = (Spinner) findViewById(R.id.spinner);
//
//    // Create an ArrayAdapter using the string array and a default spinner layout
//    ArrayAdapter<CharSequence> adapter = createFromResource(this,
//            R.array.sounds_array, android.R.layout.simple_spinner_item);
//
//    // Specify the layout to use when the list of choices appears
//    adapter.setDropDownViewResource();
//
//    // Apply the adapter to the spinner
//    spinner.setAdapter(adapter);

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
