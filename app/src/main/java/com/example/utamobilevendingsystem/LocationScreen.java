package com.example.utamobilevendingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.utamobilevendingsystem.HomeScreens.UserHomeScreen;
import com.example.utamobilevendingsystem.domain.Status;

public class LocationScreen extends AppCompatActivity {

    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    String vehicleID;

    TextView cooperUtaTV,nedderGreekTV,davisMitchellTV,cooperMitchellTV,oakUtaTV,spanioloWTV,spanioloMitchellTv,centerMitchellTV, removeAllocationTV;
    String cooperUta, neederGreek,davisMitchell,cooperMitchell,oakUta,spanioloW,spanioloMithcell,centerMitchell,removeAllocation;

    boolean isCallingActivityVehicleDetailScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_screen);

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        vehicleID = getIntent().getStringExtra("vehicleID");
        if(null != getIntent().getStringExtra("callingActivity")){
            isCallingActivityVehicleDetailScreen = (getIntent().getStringExtra("callingActivity")).contains("VehicleDetailsScreen") ? true:false;
        }
        cooperUtaTV= findViewById(R.id.cooperUtaTV);
        nedderGreekTV= findViewById(R.id.nedderGreekTV);
        davisMitchellTV= findViewById(R.id.davisMitchellTV);
        cooperMitchellTV= findViewById(R.id.cooperMitchellTV);
        oakUtaTV= findViewById(R.id.oakUtaTV);
        spanioloWTV= findViewById(R.id.spanioloWTV);
        spanioloMitchellTv= findViewById(R.id.spanioloMitchellTv);
        centerMitchellTV= findViewById(R.id.centerMitchellTV);
        removeAllocationTV = findViewById(R.id.removeAllocationTV);

        if(isCallingActivityVehicleDetailScreen){
            removeAllocationTV.setVisibility(View.VISIBLE);
        }

        cooperUta = cooperUtaTV.getText().toString();
        neederGreek = nedderGreekTV.getText().toString();
        davisMitchell = davisMitchellTV.getText().toString();
        cooperMitchell = cooperMitchellTV.getText().toString();
        oakUta = oakUtaTV.getText().toString();
        spanioloW = spanioloWTV.getText().toString();
        spanioloMithcell = spanioloMitchellTv.getText().toString();
        centerMitchell = centerMitchellTV.getText().toString();
        removeAllocation = removeAllocationTV.getText().toString();
        SharedPreferences prefs = getSharedPreferences("currUser", MODE_PRIVATE);
        String role= prefs.getString("userRole","");
        if(role!=null && role.equals("User") || isCallingActivityVehicleDetailScreen) {
            onClicks();
        }
    }

    private void onClicks() {
        cooperUtaTV.setOnClickListener(v -> {
            if(isCallingActivityVehicleDetailScreen){
                updateVehicleLocation("Cooper And UTA Blvd","1");
            } else{
                Intent myint = new Intent(LocationScreen.this,UserOrder.class);
                myint.putExtra("location",cooperUta);
                myint.putExtra("id",1);
                startActivity(myint);
            }
        });
        nedderGreekTV.setOnClickListener(v -> {
            if(isCallingActivityVehicleDetailScreen){
                updateVehicleLocation("W Nedderman & Greek Row","2");
            }  else{
                Intent myint = new Intent(LocationScreen.this,UserOrder.class);
                myint.putExtra("location",neederGreek);
                myint.putExtra("id",2);
                startActivity(myint);
            }

        });
        davisMitchellTV.setOnClickListener(v -> {
            if(isCallingActivityVehicleDetailScreen){
                updateVehicleLocation("S Davis & W Mitchell","3");
            }  else{
                Intent myint = new Intent(LocationScreen.this,UserOrder.class);
                myint.putExtra("location",davisMitchell);
                myint.putExtra("id",3);
                startActivity(myint);
            }

        });
        cooperMitchellTV.setOnClickListener(v -> {
            if(isCallingActivityVehicleDetailScreen){
                updateVehicleLocation("Cooper & W Mitchell","4");
            }  else{
                Intent myint = new Intent(LocationScreen.this,UserOrder.class);
                myint.putExtra("location",centerMitchell);
                myint.putExtra("id",4);
                startActivity(myint);
            }

        });
        oakUtaTV.setOnClickListener(v -> {
            if(isCallingActivityVehicleDetailScreen){
                updateVehicleLocation("S Oak & UTA Blvd","5");
            }  else{
                Intent myint = new Intent(LocationScreen.this,UserOrder.class);
                myint.putExtra("location",oakUta);
                myint.putExtra("id",5);
                startActivity(myint);
            }

        });
        spanioloWTV.setOnClickListener(v -> {
            if(isCallingActivityVehicleDetailScreen){
                updateVehicleLocation("Spaniolo & W 1st","6");
            }  else{
                Intent myint = new Intent(LocationScreen.this,UserOrder.class);
                myint.putExtra("location",spanioloW);
                myint.putExtra("id",6);
                startActivity(myint);
            }

        });
        spanioloMitchellTv.setOnClickListener(v -> {
            if(isCallingActivityVehicleDetailScreen){
                updateVehicleLocation("Spaniolo & W Mitchell","7");
            }  else{
                Intent myint = new Intent(LocationScreen.this,UserOrder.class);
                myint.putExtra("location",spanioloMithcell);
                myint.putExtra("id",7);
                startActivity(myint);
            }
        });
        centerMitchellTV.setOnClickListener(v -> {
            if(isCallingActivityVehicleDetailScreen){
                updateVehicleLocation("S Center & W Mitchell","8");
            }  else{
                Intent myint = new Intent(LocationScreen.this,UserOrder.class);
                myint.putExtra("location",centerMitchell);
                myint.putExtra("id",8);
                startActivity(myint);
            }
        });

        removeAllocationTV.setOnClickListener(v -> {
            if(isCallingActivityVehicleDetailScreen){
                updateVehicleLocation(Status.UNASSIGNED.getDescription(),"null");
            }
        });

    }

    private void updateVehicleLocation(String locationName,String locationID){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Resources.VEHICLE_LOCATION_ID, locationID);
        if("null".equalsIgnoreCase(locationID)){
            contentValues.putNull(Resources.VEHICLE_SCHEDULE_TIME);
        }
        db.update(Resources.TABLE_VEHICLE,contentValues, "vehicle_id = ?", new String[] {vehicleID});
        Intent output = new Intent();
        output.putExtra("locationName", locationName);
        setResult(RESULT_OK, output);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_menu,menu);
        SharedPreferences preferences = getSharedPreferences("currUser", MODE_PRIVATE);
        String role = preferences.getString("userRole","");
        if("Manager".equalsIgnoreCase(role)){
            menu.findItem(R.id.app_bar_search).setVisible(true);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_location:
                viewLocationList();
                return true;
            case R.id.menu_view_orders:
                viewOrders();
                return true;
            case R.id.app_bar_search:
                vehicleSearch();
                return true;
            case R.id.menu_logout:
                logout();
                return true;
            case R.id.menu_home:
                SharedPreferences preferences = getSharedPreferences("currUser", MODE_PRIVATE);
                String role = preferences.getString("userRole","");
                role= role+"HomeScreen";
                try {
                    Class<?> cls = Class.forName("com.example.utamobilevendingsystem.HomeScreens."+role);
                    Intent homeIntent = new Intent(this, cls);
                    startActivity(homeIntent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.change_password:
                changePassword();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void vehicleSearch() {
        Intent myint = new Intent(this, VehicleScreen.class);
        startActivity(myint);
    }

    private void viewOrders() {
        Intent myint = new Intent(this, OrderDetails.class);
        startActivity(myint);
    }

    private void logout() {
        SharedPreferences.Editor editor = getSharedPreferences("currUser", MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
        Intent logout = new Intent(this, LoginActivity.class);
        Toast.makeText(getApplicationContext(),"Logged out Successfully",Toast.LENGTH_SHORT).show();
        startActivity(logout);
    }

    private void changePassword() {
        Intent changePasswordIntent = new Intent(this, ChangePassword.class);
        startActivity(changePasswordIntent);
    }

    private void viewLocationList(){
        Intent changePasswordIntent = new Intent(this, LocationScreen.class);
        startActivity(changePasswordIntent);
    }
}
