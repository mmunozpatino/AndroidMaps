package com.example.mecha.maps2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsSearchActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentLocationMarker;
    public static final int REQUEST_LOCATION_CODE = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps1);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case REQUEST_LOCATION_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //permiso concevido
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission
                            .ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        if(client == null){
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                //permiso denegado
                else {
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_LONG).show();
                }
                break;
        }
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

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED){
            buildGoogleApiClient();
            //Esta linea activa el boton para buscar la ubicación actual, no activa la ubicación
            // del telefono
            mMap.setMyLocationEnabled(true);
        }



    }

    protected synchronized void buildGoogleApiClient(){
        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        client.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;

        //si ya habia un marcador es necesario borrarlo antes de poner el actual
        if(currentLocationMarker != null){
            currentLocationMarker.remove();
        }
        LatLng latlng = new LatLng(location.getLatitude(),location.getLongitude());

        //ahora para setear propiedades a ese marcador (titulo, color)
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latlng);
        markerOptions.title("CurrentLocation");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));

        //inserto las markerOptions
        currentLocationMarker = mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(10));

        //debemos detener después de actualizar
        if(client != null){
            LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();

        locationRequest.setInterval(1000);//1 segundo
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED){
            //el fusedLocation permite obtener la currentLocation
            LocationServices.FusedLocationApi.requestLocationUpdates(client,locationRequest,this);
        }

    }

    public boolean checkLocationPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED){
            //si no tenemos el permiso vamos a pedirlo
            //MIRAR
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission
                        .ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission
                        .ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            }
            return false; //si selecciona no preguntar de nuevo
        }else{
            return false;
        }
    }



    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
