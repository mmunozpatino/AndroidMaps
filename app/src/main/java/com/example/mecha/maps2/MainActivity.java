package com.example.mecha.maps2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button BCurrentPlace,BSearchPlace;
    public static final int REQUEST_LOCATION_CODE = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkLocationPermission();
        }

        BCurrentPlace = (Button) findViewById(R.id.BCurrentPlace);
        BCurrentPlace.setOnClickListener(this);

        BSearchPlace = (Button) findViewById(R.id.BSearchPlace);
        BSearchPlace.setOnClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_LOCATION_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //permiso concevido
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission
                            .ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                    }
                }
                //permiso denegado
                else {
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.BCurrentPlace:
                Intent current = new Intent(this, MapsActivity1.class);
                startActivity(current);
                break;

            case R.id.BSearchPlace:
                Intent search = new Intent(this, MapsSearchActivity.class);
                startActivity(search);
                break;
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
}
