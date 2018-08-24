/**
 * Main Screen, Entrypoint to Application
 */

package com.adesso.lklein.geofencing;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.logging.Logger;

import room.room.CreateProjektActivity;
import room.room.RoomMain;
import timetracking.EditData;
import timetracking.Timetracking;

public class MainActivity extends AppCompatActivity
        implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener,
        ResultCallback<Status> {

    private static final String TAG=MainActivity.class.getSimpleName();
    private GoogleMap map;
    private GoogleApiClient googleApiClient;
    private Location lastLocation;
    private TextView tv, textLong;
    private MapFragment mapFragment;
    private static final String NOTIFICATION_MSG = "NOTIFICATION MSG";

    //autocompletefragment
    PlaceAutocompleteFragment placeAutocompleteFragment;

    //currentlocation
    private LocationManager mLocationManager;

    //gpstracker
    private GPSTracker gpsTracker;
    private Location location;

    //seekbar
    private static SeekBar seekBar;
    private static TextView mTextView;

    /**
     * Create a notification
     *
     * @param context
     * @param msg
     * @return
     */

    public static Intent makeNotificationIntent(Context context, String msg){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(NOTIFICATION_MSG, msg);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.lat);
        textLong = (TextView) findViewById(R.id.lon);

        initGMaps();
        createGoogleApi();
        seebar();

        ImageButton IB = findViewById(R.id.locationbutton);
        IB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //need to paste in here
                //the code which fits
                //the currentlocation
            }
        });




    }



    private void createGoogleApi(){
        Log.d(TAG, "create Google API");
        if(googleApiClient == null){
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }


    //more seekbar
    public void seebar(){
        seekBar = (SeekBar) findViewById(R.id.seekBarID);
        mTextView = (TextView) findViewById(R.id.textviewseekbar);
        mTextView.setText("Covered: " +seekBar.getProgress() + " / " +seekBar.getMax());

        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    int progress_value;

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progress_value = progress;
                        mTextView.setText("Covered: " + progress + " / " +seekBar.getMax());
                        Toast.makeText(MainActivity.this, "Seekbar in progress", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        Toast.makeText(MainActivity.this, "Seekbar is starting", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        mTextView.setText("Covered: " + progress_value + " / " +seekBar.getMax());
                        Toast.makeText(MainActivity.this, "Seekbar is stopping", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }


    protected void onStart(){
        super.onStart();
        googleApiClient.connect();
    }

    protected void onStop(){
        super.onStop();
        googleApiClient.disconnect();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.geofence:{
                startGeofence();
                return true;
            }
            case R.id.clear:{
                clearGeofence();
                return true;
            }

            //fuer timetracking
            case R.id.timetracking:{
                Intent myIntent = new Intent(MainActivity.this, Timetracking.class);
                MainActivity.this.startActivity(myIntent);
            }

            case R.id.edittimetracking:{
                Intent myIntent = new Intent(MainActivity.this, EditData.class);
                MainActivity.this.startActivity(myIntent);
            }

            //fuer room
            case R.id.viewroomdata: {
               Intent myIntent = new Intent(MainActivity.this, CreateProjektActivity.class);
               MainActivity.this.startActivity(myIntent);
            }
            //fuer room
            case R.id.zeigDATEN: {
                Intent myIntent = new Intent(MainActivity.this, RoomMain.class);
                MainActivity.this.startActivity(myIntent);
            }




        }
        return super.onOptionsItemSelected(item);
    }

    private final int REQ_PERMISSION = 999;


    private boolean checkPermission(){
        Log.d(TAG, "checkPermission()");
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    private void askPermission(){
        Log.d(TAG, "askPermission()");
        ActivityCompat.requestPermissions(
                this,
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                REQ_PERMISSION
        );
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        Log.d(TAG, "onRequestPermissionResult()");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQ_PERMISSION: {
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getLastKnownLocation();
                } else {
                    permissionDenied();
                }
                break;
            }
        }

    }

    private void permissionDenied(){
        Log.w(TAG, "permissionDenied()");
    }

    private void initGMaps(){
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void onMapReady(GoogleMap googleMap){
        Log.d(TAG, "onMapReady()");
        map = googleMap;


        map.setOnMapClickListener(this);
        map.setOnMarkerClickListener(this);
    }




    public void onMapClick(LatLng latLng){
        Log.d(TAG, "onMapClick(" +latLng +")");
        markerForGeofence(latLng);
    }

    public boolean onMarkerClick(Marker marker){
        Log.d(TAG, "onMarkerClick: " +marker.getPosition());
        return false;
    }

    private LocationRequest locationRequest;
    private final int UPDATE_INTERVAL = 1000;
    private final int FASTEST_INVERVAL = 900;

    private void startLocationUpdates(){
        Log.i(TAG, "StartLocationUpdates()");
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INVERVAL);
        if(checkPermission())
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest,
                    (com.google.android.gms.location.LocationListener) this);
    }

    public void onLocationChanged(Location location){
        Log.d(TAG, "OnLocationChanged["+location+"]");
        lastLocation = location;
        writeActualLocation(location);
    }

    public void onConnected(Bundle bundle){
        Log.d(TAG, "onConnected()");
        getLastKnownLocation();
        recoverGeofenceMarker();
    }

    public void onConnectionSuspended(int i){
        Log.d(TAG, "OnConnectionSuspended()");
    }

    public void onConnectionFailed(ConnectionResult connectionResult){
        Log.d(TAG, "OnConnectionFailed()");
    }

    private void getLastKnownLocation(){
        Log.d(TAG, "LastKnownLocation()");
            if (checkPermission()){
                lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

                if(lastLocation != null){
                        Log.i(TAG, "LastKnown location." +
                        "Long: " +lastLocation.getLongitude() +
                        "/ Lat: " +lastLocation.getLatitude());
                             writeLastLocation();
                             startLocationUpdates();
                    } else {
                    Log.w(TAG, "No location");
                    startLocationUpdates();
                }
            }
            else{
                askPermission();
            }
        }


    private void writeActualLocation(Location location){

            tv.setText("Lat: " + location.getLatitude());
            textLong.setText("Long: " + location.getLongitude());
            markerLocation(new LatLng(location.getLatitude(), location.getLongitude()));

    }

    private void writeLastLocation(){
        writeActualLocation(lastLocation);
    }

    private Marker locationMarker;

    private void markerLocation(LatLng latLng){
        Log.i(TAG, "markerLocation("+latLng+")");
        String titel = latLng.latitude + ", " + latLng.longitude;
        MarkerOptions MO = new MarkerOptions()
                .position(latLng)
                .title(titel);

        if(map!=null){
            if(locationMarker != null)
                locationMarker.remove();
            locationMarker = map.addMarker(MO);
            float zoom = 14f;
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
            map.animateCamera(cameraUpdate);
        }

    }

    private Marker geoFenceMarker;

    private void markerForGeofence(LatLng latLng) {
        Log.i(TAG, "markerForGeofence("+latLng+")");
        String title = latLng.latitude + ", " + latLng.longitude;
        // Define marker options
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .title(title);
        if ( map!=null ) {
            // Remove last geoFenceMarker
            if (geoFenceMarker != null)
                geoFenceMarker.remove();

            geoFenceMarker = map.addMarker(markerOptions);
        }
    }

    private static final long GEO_DURATION = 60*60*1000;
    private static final String GEOFENCE_REQ_ID = "My Geofence";
    private static float GEOFENCE_RADIUS = 250.0f; //in metern

    private Geofence createGeofence (LatLng latLng, float radius){
        Log.d(TAG, "createGeofence");
        return new Geofence.Builder()
                .setRequestId(GEOFENCE_REQ_ID)
                .setCircularRegion(latLng.latitude, latLng.longitude, radius)
                .setExpirationDuration(GEO_DURATION)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER
                        | Geofence.GEOFENCE_TRANSITION_ENTER)
                .build();
    }

    //geofence aktivieren
    private void startGeofence(){
        Log.i(TAG, "startGeofence()");
        if(geoFenceMarker != null){
            Geofence geofence = createGeofence(geoFenceMarker.getPosition(), GEOFENCE_RADIUS);
            GeofencingRequest geofencingRequest = createGeofenceRequest(geofence);
            addGeofence(geofencingRequest);
        } else {
            Log.e(TAG, "Geofence Marker ist null");
        }
    }

    private GeofencingRequest createGeofenceRequest(Geofence geofence){
        Log.d(TAG, "createGeofenceRequest");
        return new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofence(geofence)
                .build();
    }

    private PendingIntent geoFencePendingIntent;
    private final int GEOFENCE_REQ_CODE = 0;

    private PendingIntent createGeofencePendingIntent(){
        Log.d(TAG, "createPendingIntent");
        if(geoFencePendingIntent != null){
            return geoFencePendingIntent;
        }
        Intent intent = new Intent(this, GeoFenceTransitionService.class);
        return PendingIntent.getService(
                this, GEOFENCE_REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT
        );

    }

    private void addGeofence(GeofencingRequest request) {
        Log.d(TAG, "addGeofence");
        if (checkPermission())
            LocationServices.GeofencingApi.addGeofences(
                    googleApiClient,
                    request,
                    createGeofencePendingIntent()
            ).setResultCallback(this);
    }



    public void onResult(Status status){
        Log.i(TAG, "onResult: " +status);
        if(status.isSuccess()){
            saveGeofence();
            drawGeofence();
        } else{
            Log.i(TAG, "Status failed");
        }
    }

    private Circle geoFenceLimits;

    private void drawGeofence(){
        Log.d(TAG, "drawGeofence");

        if(geoFenceLimits != null){
            geoFenceLimits.remove();
        }

        CircleOptions circleOptions = new CircleOptions()
                .center(geoFenceMarker.getPosition())
                .strokeColor(Color.argb(200, 17, 110, 187))
                .fillColor(Color.argb(75, 17, 110, 187))
                .radius(GEOFENCE_RADIUS);
        geoFenceLimits = map.addCircle(circleOptions);
    }

    private final String KEY_GEOFENCE_LAT ="GEOFENCE LATITUDE";
    private final String KEY_GEOFENCE_LON ="GEOFENCE LONGITUDE";

    //save GeoFence Daten

    private void saveGeofence(){
        Log.d(TAG, "saveGeofence");
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putLong(KEY_GEOFENCE_LAT, Double.doubleToRawLongBits(geoFenceMarker.getPosition().latitude));
        editor.putLong(KEY_GEOFENCE_LON, Double.doubleToRawLongBits(geoFenceMarker.getPosition().longitude));

        editor.apply();


    }

    //letzten geofence marker wieder herstellen

    private void recoverGeofenceMarker(){
        Log.d(TAG, "recoverGeofence");

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

        if(sharedPref.contains(KEY_GEOFENCE_LAT) && sharedPref.contains(KEY_GEOFENCE_LON)){
            double lat = Double.doubleToLongBits(sharedPref.getLong(KEY_GEOFENCE_LAT, -1));
            double lon = Double.doubleToLongBits(sharedPref.getLong(KEY_GEOFENCE_LON, -1));

            LatLng latLng = new LatLng(lat, lon);
            markerForGeofence(latLng);
            drawGeofence();
        }



    }

    //um den geokreis wieder zu entfernen

    private void clearGeofence(){
        Log.d(TAG, "clearGeofence");

        LocationServices.GeofencingApi.removeGeofences(
                googleApiClient,
                createGeofencePendingIntent()
        ).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if(status.isSuccess()){
                    removeGeofence();
                }
            }
        });
    }

    private void removeGeofence(){
        Log.d(TAG, "removeGeofence");
        if(geoFenceMarker != null){
            geoFenceMarker.remove();
        } if (geoFenceLimits != null){
            geoFenceLimits.remove();
        }
    }


    //for current location

    /*public static final int LOCATION_UPDATE_MIN_TIME = 5000;
    public static final int LOCATION_UPDATE_MIN_DISTANCE = 10;

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if(location != null){
                Logger.d(String.format("%f, %f", location.getLatitude(), location.getLongitude()));
                markerLocation(LatLng);
                mLocationManager.removeUpdates(mLocationListener);
            }
        }
    }
    private void getCurrentLocation(){

        boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEneabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location location = null;

        if(!(isGPSEnabled || isNetworkEneabled)){
            Toast.makeText(MainActivity.this, "GPS or Network is not enabeled", Toast.LENGTH_LONG).show();
        } else {
            if(isNetworkEneabled) {
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
            }
        }

    }
*/



}
