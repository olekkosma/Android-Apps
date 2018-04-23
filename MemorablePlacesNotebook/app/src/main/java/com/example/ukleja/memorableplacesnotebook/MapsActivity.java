package com.example.ukleja.memorableplacesnotebook;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;

    LocationManager locationManager;
    LocationListener locationListener;
    Location actualLocation;
    boolean flagAddLocation = true;

    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                centerMapOnLocation(lastKnownLocation, "Your last location");


            }

        }
    }

    public void centerMapOnLocation(Location location, String title) {

        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

        if(Math.abs(userLocation.latitude-actualLocation.getLatitude())>0.000015 && Math.abs(userLocation.longitude-actualLocation.getLongitude())>0.000015 ||
                userLocation.latitude-actualLocation.getLatitude()==0 && userLocation.longitude-actualLocation.getLongitude()==0){
            //Toast.makeText(this,(String.format("%.8f \n %.8f", Math.abs(userLocation.latitude-actualLocation.getLatitude()), Math.abs(userLocation.longitude-actualLocation.getLongitude()))),Toast.LENGTH_SHORT).show();

            //mMap.clear();
            mMap.addMarker(new MarkerOptions().position(userLocation).title("your location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 18));

            actualLocation.setLatitude(location.getLatitude());
            actualLocation.setLongitude(location.getLongitude());
        }
    }

    public void centerMapOnLocationMap(Location location, String title) {

        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(userLocation).title(title));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 18));


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);

        Intent intent = getIntent();

        if(intent.getIntExtra("placeNumber",0) ==0){
            flagAddLocation=true;
            //crop to user
            locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                        centerMapOnLocation(location,"Your location");
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


            if(Build.VERSION.SDK_INT<23){

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

            }else{

                if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    actualLocation = lastKnownLocation;

                    centerMapOnLocation(lastKnownLocation,"Your last location");

                }else{

                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                }
            }
        }else{
            flagAddLocation=false;

            Location placeLocation = new Location(locationManager.GPS_PROVIDER);
            placeLocation.setLatitude(MainActivity.locations.get(intent.getIntExtra("placeNumber",0)).latitude);
            placeLocation.setLongitude(MainActivity.locations.get(intent.getIntExtra("placeNumber",0)).longitude);
            centerMapOnLocationMap(placeLocation,MainActivity.places.get(intent.getIntExtra("placeNumber",0)));



        }

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        if (flagAddLocation) {


            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

            String address = "";
            try {
                List<Address> listAddresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

                if (listAddresses != null && listAddresses.size() > 0) {

                    if (listAddresses.get(0).getThoroughfare() != null) {

                        if (listAddresses.get(0).getSubThoroughfare() != null) {

                            address += listAddresses.get(0).getSubThoroughfare() + " ";

                        }

                        address += listAddresses.get(0).getThoroughfare() + " ";

                    }

                }
            } catch (IOException e) {

                e.printStackTrace();

            }
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm yyyy-MM-dd");
            address += "\n" + sdf.format(new Date());
            mMap.addMarker(new MarkerOptions().position(latLng).title(address));

            MainActivity.places.add(address);
            MainActivity.locations.add(latLng);
            MainActivity.arrayAdapter.notifyDataSetChanged();

            SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.ukleja.memorableplacesnotebook",Context.MODE_PRIVATE);
            try {

                ArrayList<String> latitudes = new ArrayList<>();
                ArrayList<String> longitudes = new ArrayList<>();

                for(LatLng coordinates : MainActivity.locations){

                    latitudes.add(Double.toString(coordinates.latitude));
                    longitudes.add(Double.toString(coordinates.longitude));

                }

                sharedPreferences.edit().putString("places",ObjectSerializer.serialize(MainActivity.places)).apply();
                sharedPreferences.edit().putString("latitudes",ObjectSerializer.serialize(latitudes)).apply();
                sharedPreferences.edit().putString("longitudes",ObjectSerializer.serialize(longitudes)).apply();

            } catch (IOException e) {
                e.printStackTrace();
            }

            Toast.makeText(this, "location saved", Toast.LENGTH_SHORT).show();

        }
    }
}
