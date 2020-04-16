package com.example.utamobilevendingsystem;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.utamobilevendingsystem.HomeScreens.ManagerHomeScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class UpdateLocationSchedule extends AppCompatActivity {
    EditText SpanioloAndWMitchellET,SDavisAndWMitchellET,OakandUTABlvdET,CooperAndUTABlvdET,SCenterAndWMitchellET,CooperAndWMitchellET,SpanioloAndW1stET,WNeddermanandGreekRowET;
    Button UPDATE_LOC_SCHEDULE;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_location_schedule);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbHelper = new DatabaseHelper(this);
        db= dbHelper.getReadableDatabase();

        SpanioloAndWMitchellET = findViewById(R.id.spanioloMitchellTv1);
        SDavisAndWMitchellET= findViewById(R.id.davisMitchellTV1);
        OakandUTABlvdET= findViewById(R.id.oakUtaTV1);
        CooperAndUTABlvdET= findViewById(R.id.cooperUtaTV1);
        SCenterAndWMitchellET= findViewById(R.id.centerMitchellTV1);
        CooperAndWMitchellET= findViewById(R.id.cooperMitchellTV1);
        SpanioloAndW1stET= findViewById(R.id.spanioloWTV1);
        WNeddermanandGreekRowET= findViewById(R.id.nedderGreekTV1);

        fetchdata();
        UPDATE_LOC_SCHEDULE= (Button) findViewById(R.id.updatelocschedule);

        UPDATE_LOC_SCHEDULE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatelocationschedulemanager();
            }
        });

    }

    private void fetchdata(){
        Cursor cursor = db.rawQuery("select  schedule from location order by locationName ", null);
        int i=0;
        while(cursor.moveToNext())
        {
            if(i==0){
                System.out.println("-----------------------------"+cursor.getString(cursor.getColumnIndex("schedule")));
                 CooperAndWMitchellET.setText(cursor.getString(cursor.getColumnIndex("schedule")));
             }
             if(i==1){
                 CooperAndUTABlvdET.setText(cursor.getString(cursor.getColumnIndex("schedule")));
             }
             if(i==2){
                 SCenterAndWMitchellET.setText(cursor.getString(cursor.getColumnIndex("schedule")));
             }
             if(i==3){
                 SDavisAndWMitchellET.setText(cursor.getString(cursor.getColumnIndex("schedule")));
             }
             if(i==4){
                 OakandUTABlvdET.setText(cursor.getString(cursor.getColumnIndex("schedule")));
             }
             if(i==5){
                 SpanioloAndW1stET.setText(cursor.getString(cursor.getColumnIndex("schedule")));
             }
             if(i==6){
                 SpanioloAndWMitchellET.setText(cursor.getString(cursor.getColumnIndex("schedule")));
             }
             if(i==7){
                 WNeddermanandGreekRowET.setText(cursor.getString(cursor.getColumnIndex("schedule")));
             }
            i++;
        }
    }

    private void updatelocationschedulemanager(){

        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateLocationSchedule.this);
        builder.setTitle("Update Location Schedule");
        builder.setMessage("Are you sure you want to make these changes to the Schedule?")

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        db.execSQL("UPDATE location SET schedule =\""+CooperAndWMitchellET.getText()+"\"  WHERE location_id=\"4\"");
                        db.execSQL("UPDATE location SET schedule =\""+CooperAndUTABlvdET.getText()+"\"  WHERE location_id=\"1\"");
                        db.execSQL("UPDATE location SET schedule =\""+SCenterAndWMitchellET.getText()+"\"  WHERE location_id=\"8\"");
                        db.execSQL("UPDATE location SET schedule =\""+SDavisAndWMitchellET.getText()+"\"  WHERE location_id=\"3\"");
                        db.execSQL("UPDATE location SET schedule =\""+OakandUTABlvdET.getText()+"\"  WHERE location_id=\"5\"");
                        db.execSQL("UPDATE location SET schedule =\""+SpanioloAndW1stET.getText()+"\"  WHERE location_id=\"6\"");
                        db.execSQL("UPDATE location SET schedule =\""+SpanioloAndWMitchellET.getText()+"\"  WHERE location_id=\"7\"");
                        db.execSQL("UPDATE location SET schedule =\""+WNeddermanandGreekRowET.getText()+"\"  WHERE location_id=\"2\"");
                        Intent myInt = new Intent(UpdateLocationSchedule.this, ManagerHomeScreen.class);
                        startActivity(myInt);

                        Toast.makeText(getApplicationContext(),"Updated Successfully",Toast.LENGTH_SHORT).show();
                    }
                })

                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_menu,menu);
        menu.findItem(R.id.app_bar_search).setVisible(true);
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
            case R.id.change_password:
                changePassword();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void vehicleSearch() {
        Intent myint = new Intent(UpdateLocationSchedule.this, VehicleScreen.class);
        startActivity(myint);
    }

    private void viewOrders() {
        Intent myint = new Intent(UpdateLocationSchedule.this, ManagerOrderDetails.class);
        startActivity(myint);
    }

    private void logout() {
        Intent logout = new Intent(UpdateLocationSchedule.this, LoginActivity.class);
        startActivity(logout);
    }

    private void changePassword() {
        Intent changePasswordIntent = new Intent(UpdateLocationSchedule.this, ChangePassword.class);
        startActivity(changePasswordIntent);
    }

    private void viewLocationList(){
        Intent changePasswordIntent = new Intent(UpdateLocationSchedule.this, LocationScreen.class);
        startActivity(changePasswordIntent);
    }




}
