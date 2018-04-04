package com.example.kevinbarbian14.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button btn;
    private Marker mark;
    private EditText lat;
    private EditText longi;
    private Geocoder gc;
    private TextView cnt;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get entered latitude
                lat = findViewById(R.id.lat);
                //get entered longitude
                longi = findViewById(R.id.longi);
                //update location after dragging marker
                mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                    @Override
                    public void onMarkerDragStart(Marker marker) {

                    }

                    @Override
                    public void onMarkerDrag(Marker marker) {

                    }

                    @Override
                    public void onMarkerDragEnd(Marker marker) {
                        marker.getPosition();
                        double lat = marker.getPosition().latitude;
                        double longi = marker.getPosition().longitude;
                        Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
                        try {
                            List<Address> addresses = geoCoder.getFromLocation(lat, longi, 1);

                            String add = "";
                            if (addresses.size() > 0) {
                                for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++)
                                    add += addresses.get(0).getAddressLine(i) + "\n";
                            }
                            if (addresses.size() > 0) {
                                String count = addresses.get(0).getCountryName();
                                cnt.setText(count);
                            } else
                                cnt.setText("WATER");
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
                Random rand = new Random();
                Double r = rand.nextDouble();
                LatLng place = new LatLng(Double.parseDouble(lat.getText().toString()), Double.parseDouble(longi.getText().toString()));
                if (r > .5)
                    mMap.addMarker(new MarkerOptions().position(place).draggable(true).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("test", 75, 75))));
                else
                    mMap.addMarker(new MarkerOptions().position(place).draggable(true).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("taco", 75, 75))));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(place));
                cnt = findViewById(R.id.country);
                //get locations using geoCoder
                Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
                try {
                    List<Address> addresses = geoCoder.getFromLocation(Double.parseDouble(lat.getText().toString()), Double.parseDouble(longi.getText().toString()), 1);
                    if (addresses.size()>0) {
                        String count = addresses.get(0).getLocality() + ", "  +addresses.get(0).getCountryName();
                        cnt.setText(count);
                    }
                    else
                        cnt.setText("WATER");
                }
                catch (IOException | IllegalArgumentException e1) {
                    e1.printStackTrace();
                }



            }
        });

    }
    //see https://stackoverflow.com/questions/14851641/change-marker-size-in-google-maps-api-v2
    public Bitmap resizeMapIcons(String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

}
