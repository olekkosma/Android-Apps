package com.example.ukleja.hikerwatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            startListening();
        }
    }

    public void startListening(){

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        }

    }

    public void updateLocationInfo(Location location){

        Log.i("Location:",location.toString());

        TextView latitude = (TextView)findViewById(R.id.latitudeTextView);
        TextView longitude = (TextView)findViewById(R.id.longitudeTextView);
        TextView altitude = (TextView)findViewById(R.id.altitudeTextView);
        TextView accuracy = (TextView)findViewById(R.id.accuracyTextView);
        TextView address = (TextView) findViewById(R.id.AddressTextView);

        latitude.setText("Latitude: "+location.getLatitude());
        longitude.setText("Longitude: "+location.getLongitude());
        altitude.setText("Altitude: "+location.getAltitude());
        accuracy.setText("Accuracy: "+location.getAccuracy());

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {

            String addressString ="Address:\n";


            List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            Log.i("ADDRESS:",listAddresses.toString());
            if(listAddresses !=null && listAddresses.size()>0){

                if(listAddresses.get(0).getSubThoroughfare() != null){
                    addressString+=listAddresses.get(0).getSubThoroughfare()+" \n";

                }
                if(listAddresses.get(0).getThoroughfare() != null){
                    addressString+=listAddresses.get(0).getThoroughfare()+" \n";

                }

                if(listAddresses.get(0).getPostalCode() != null){
                    addressString+=listAddresses.get(0).getPostalCode()+" \n";

                }

                if(listAddresses.get(0).getAdminArea() != null){
                    addressString+=listAddresses.get(0).getAdminArea()+" \n";

                }
                if(listAddresses.get(0).getSubAdminArea() != null){
                    addressString+=listAddresses.get(0).getSubAdminArea()+" \n";

                }

                if(listAddresses.get(0).getCountryName() != null){
                    addressString+=listAddresses.get(0).getCountryName()+" \n";

                }

            }
            address.setText(addressString);


        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

        updateLocationInfo(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if(Build.VERSION.SDK_INT <23){
           startListening();

        }else{

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(this,new String []{Manifest.permission.ACCESS_FINE_LOCATION},1);

            }else{
                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER,0,0,locationListener);

                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(location != null){
                    updateLocationInfo(location);

                }

            }
        }
    }
}
