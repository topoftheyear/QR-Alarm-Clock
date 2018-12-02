package crundle.qralarmclock;

import android.Manifest;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanningCodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView ScannerView;
    public boolean shouldAllowBack = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScannerView = new ZXingScannerView(this);
        setContentView(ScannerView);
        //Request permission to use the camera, something that has to be done in runtime post-Android 6.0
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 1);
        ScannerView.startCamera();

    }
    //Keeps the user from backing out of the scanner, thus defeating the purpose of the app
    public void onBackPressed() {
        if (!shouldAllowBack) {
            Toast.makeText(this, "Please scan your QR Code.", Toast.LENGTH_LONG);
        } else {
            super.onBackPressed();
        }
    }
    //Reads the QR code. If it's our "patented" pattern, i.e. the text "Stop Alarm", the alarm stops and the user is sent back to the main alarm manager
    @Override
    public void handleResult(Result result) {

        if(result.getText().equals("Stop Alarm")) {
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
