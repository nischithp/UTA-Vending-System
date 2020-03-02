package com.example.utamobilevendingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

public class LocationScreen extends AppCompatActivity {
    TextView cooperUtaTV,nedderGreekTV,davisMitchellTV,cooperMitchellTV,oakUtaTV,spanioloWTV,spanioloMitchellTv,centerMitchellTV;
    String cooperUta, neederGreek,davisMitchell,cooperMitchell,oakUta,spanioloW,spanioloMithcell,centerMitchell;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_screen);
        cooperUtaTV= findViewById(R.id.cooperUtaTV);
        nedderGreekTV= findViewById(R.id.nedderGreekTV);
        davisMitchellTV= findViewById(R.id.davisMitchellTV);
        cooperMitchellTV= findViewById(R.id.cooperMitchellTV);
        oakUtaTV= findViewById(R.id.oakUtaTV);
        spanioloWTV= findViewById(R.id.spanioloWTV);
        spanioloMitchellTv= findViewById(R.id.spanioloMitchellTv);
        centerMitchellTV= findViewById(R.id.centerMitchellTV);
        cooperUta = cooperUtaTV.getText().toString();
        neederGreek = nedderGreekTV.getText().toString();
        davisMitchell = davisMitchellTV.getText().toString();
        cooperMitchell = cooperMitchellTV.getText().toString();
        oakUta = oakUtaTV.getText().toString();
        spanioloW = spanioloWTV.getText().toString();
        spanioloMithcell = spanioloMitchellTv.getText().toString();
        centerMitchell = centerMitchellTV.getText().toString();
        onClicks();
    }

    private void onClicks() {
        cooperUtaTV.setOnClickListener(v -> {
            Intent myint = new Intent(LocationScreen.this,UserOrder.class);
            myint.putExtra("location",cooperUta);
            myint.putExtra("id",1);
            startActivity(myint);
        });
        nedderGreekTV.setOnClickListener(v -> {
            Intent myint = new Intent(LocationScreen.this,UserOrder.class);
            myint.putExtra("location",neederGreek);
            myint.putExtra("id",2);
            startActivity(myint);
        });
        davisMitchellTV.setOnClickListener(v -> {
            Intent myint = new Intent(LocationScreen.this,UserOrder.class);
            myint.putExtra("location",davisMitchell);
            myint.putExtra("id",3);
            startActivity(myint);
        });
        cooperMitchellTV.setOnClickListener(v -> {
            Intent myint = new Intent(LocationScreen.this,UserOrder.class);
            myint.putExtra("location",centerMitchell);
            myint.putExtra("id",4);
            startActivity(myint);
        });
        oakUtaTV.setOnClickListener(v -> {
            Intent myint = new Intent(LocationScreen.this,UserOrder.class);
            myint.putExtra("location",oakUta);
            myint.putExtra("id",5);
            startActivity(myint);
        });
        spanioloWTV.setOnClickListener(v -> {
            Intent myint = new Intent(LocationScreen.this,UserOrder.class);
            myint.putExtra("location",spanioloW);
            myint.putExtra("id",6);
            startActivity(myint);
        });
        spanioloMitchellTv.setOnClickListener(v -> {
            Intent myint = new Intent(LocationScreen.this,UserOrder.class);
            myint.putExtra("location",spanioloMithcell);
            myint.putExtra("id",7);
            startActivity(myint);
        });
        centerMitchellTV.setOnClickListener(v -> {
            Intent myint = new Intent(LocationScreen.this,UserOrder.class);
            myint.putExtra("location",centerMitchell);
            myint.putExtra("id",8);
            startActivity(myint);
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_menu,menu);
        return true;
    }
}
