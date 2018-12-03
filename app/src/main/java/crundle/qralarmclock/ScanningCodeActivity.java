package crundle.qralarmclock;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanningCodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    //Source for tutorial:
    //https://www.youtube.com/watch?v=A2PqYkLb_-A
    ZXingScannerView ScannerView;
    public boolean shouldAllowBack = false;
    public Uri notification;
    private static Ringtone r;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScannerView = new ZXingScannerView(this);
        //Initializes the QR Scanner
        setContentView(ScannerView);
        notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        r = RingtoneManager.getRingtone(this, notification);
        r.play();

    }
    //Keeps the user from using the back button, defeating the purpose of our alarm
    public void onBackPressed() {
        if (!shouldAllowBack) {
            Toast.makeText(this, "Please scan your QR Code.", Toast.LENGTH_LONG);
        } else {
            super.onBackPressed();
        }
    }
    //If the scanner reads our "patented" QR code, it will stop the ringtone and return to the main menu.
    @Override
    public void handleResult(Result result) {
        if(result.getText().equals("Stop Alarm")) {
            r.stop();
            startActivity(new Intent(getApplicationContext(),MainAlarmsActivity.class));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        ScannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();

        ScannerView.setResultHandler(this);
        ScannerView.startCamera();
    }
}
