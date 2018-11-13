package crundle.qralarmclock;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.File;
import java.io.IOException;

public class MainAlarmsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_alarms);

        try {
            setupRecyclerView();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setupRecyclerView() throws IOException, ClassNotFoundException {

        recyclerView  = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new AlarmAdapter(MockData.getMockAlarms(this)));
    }

    public void openSettings(View view) {
        Intent intent;
        intent = new Intent(this, AlarmSettingsActivity.class);
        startActivity(intent);
    }


}
