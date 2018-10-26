package crundle.qralarmclock;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class MainAlarms extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_alarms);
    }

    public void openSettings(View view) {
        Intent intent;
        intent = new Intent(this, AlarmSettings.class);
        startActivity(intent);
    }
}
