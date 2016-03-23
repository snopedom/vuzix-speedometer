package com.vuzix.dominik.speedometer;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SpeedometerActivity extends AppCompatActivity {

    private Camera mCamera;
    private CameraBackground mPreview;
    private TextView speedView;
    private LocationManager locationManager;
    private LocationListener locationListener;
    double mySpeed;
    private boolean isSearching;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speedometer);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        mCamera = Camera.open();

        mPreview = new CameraBackground(this, mCamera);
        RelativeLayout preview = (RelativeLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        speedView = (TextView) findViewById(R.id.speedView);

        mySpeed = 0;
        locationManager = (LocationManager) getSystemService
                (Context.LOCATION_SERVICE);
        locationListener = new SpeedActionListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        boolean gpsEnabled = locationManager.isProviderEnabled(locationManager.GPS_PROVIDER);

        if(!gpsEnabled) {
            Toast.makeText(getApplicationContext(), "GPS IS NOT ENABLED!!!", Toast.LENGTH_LONG).show();
        }
    }

    private class SpeedActionListener implements LocationListener
    {
        @Override
        public void onLocationChanged(Location location) {
            if(location!=null) {
                if(location.hasSpeed()){
                    mySpeed = location.getSpeed();
                    speedView.setText(String.valueOf((int) mySpeed));
                }
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }
}
