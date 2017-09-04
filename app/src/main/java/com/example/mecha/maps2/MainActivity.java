package com.example.mecha.maps2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button BCurrentPlace,BSearchPlace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BCurrentPlace = (Button) findViewById(R.id.BCurrentPlace);
        BCurrentPlace.setOnClickListener(this);

        BSearchPlace = (Button) findViewById(R.id.BSearchPlace);
        BSearchPlace.setOnClickListener(this);
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
}
