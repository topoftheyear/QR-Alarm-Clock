package crundle.qralarmclock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AlarmSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_settings);
    }

    /* Cancel changes
    *   - Go back to MainAlarms*/
    public void cancel(View view) {
        Intent intent;
        intent = new Intent(this, MainAlarms.class);
        startActivity(intent);
    }

    /* Save changes
     *   - Update alarm's settings, go back to MainAlarms*/
    public void save(View view) {
        // DO SAVE STUFF

        Intent intent;
        intent = new Intent(this, MainAlarms.class);
        startActivity(intent);
    }
}
