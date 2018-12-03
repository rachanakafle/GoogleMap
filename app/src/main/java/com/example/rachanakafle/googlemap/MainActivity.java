package com.example.rachanakafle.googlemap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    Button go, satellite, normal, terrain;
    double lat, lng;
    EditText address;
    LatLng clicklatlng, newLatLng;
    GoogleMap map;
    String incompleteUrl = "http://maps.googleapis.com/maps/api/geocode/json?address=";
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        go = findViewById(R.id.btn1);
        satellite = findViewById(R.id.btn2);
        normal = findViewById(R.id.btn3);
        terrain = findViewById(R.id.btn4);
        address = findViewById(R.id.address);
        requestQueue= Volley.newRequestQueue(MainActivity.this);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_maps);
        supportMapFragment.getMapAsync(this);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String completeUrl = incompleteUrl + address.getText().toString();
                StringRequest stringRequest = new StringRequest(Request.Method.GET, completeUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj1 = new JSONObject(response);
                            JSONArray array1 = obj1.getJSONArray("results");
                            JSONObject obj2 = array1.getJSONObject(0);
                            JSONObject obj3 = obj2.getJSONObject("geometry");
                            JSONObject obj4 = obj3.getJSONObject("location");
                            lat = obj4.getDouble("lat");
                            lng = obj4.getDouble("lng");
                            newLatLng = new LatLng(lat, lng);
                            map.clear();
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, 10));
                            map.addMarker(new MarkerOptions().position(newLatLng));


                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, "Exception Catched", Toast.LENGTH_SHORT).show();

                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "No response", Toast.LENGTH_SHORT).show();
                    }
                });
                requestQueue.add(stringRequest);
            }
        });
        satellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

            }
        });
        normal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);


            }
        });
        terrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

            }
        });


    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng l = new LatLng(27.6853, 85.3743);
        clicklatlng = l;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(l, 10));
        googleMap.addMarker(new MarkerOptions().position(l));
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
                googleMap.clear();
                clicklatlng = latLng;
                googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));


            }
        });
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                PolylineOptions options = new PolylineOptions();
                options.add(clicklatlng);
                options.add(latLng);
                googleMap.addPolyline(options);
            }
        });

    }

}