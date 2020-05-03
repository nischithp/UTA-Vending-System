package com.example.utamobilevendingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.view.MenuItem;

public class VehicleInventoryScreen extends AppCompatActivity {

    final String VEHICLE_INVENTORY_QUERY = "select i.item_name, v.quantity from vehicle_inventory v join item i on v.item_id = i.item_id where v.vehicle_id = ?";

    DatabaseHelper dbHelper;
    SQLiteDatabase db;

    EditText swichAvl, drinksAvl, snacksAvl;
    Button updateInventoryBtn;
    int userID;

    String vehicleID, flag, role;

    private void fetchSharedPref() {
        SharedPreferences prefs = getSharedPreferences("currUser", MODE_PRIVATE);
        userID = prefs.getInt("userid", 0);
        role = prefs.getString("userRole", "");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_inventory_screen);

        flag = getIntent().getStringExtra("flag_btn");
        updateInventoryBtn = findViewById(R.id.updateInventoryBtn);
        if (flag.equals("1")) {   //Disabling for operator view
            updateInventoryBtn.setEnabled(false);
        }

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        swichAvl = findViewById(R.id.swichAvl);
        drinksAvl = findViewById(R.id.drinksAvl);
        snacksAvl = findViewById(R.id.snacksAvl);
        updateInventoryBtn = findViewById(R.id.updateInventoryBtn);

        if (flag.equals("1")) {   //Disabling for operator view
            swichAvl.setEnabled(false);
            drinksAvl.setEnabled(false);
            snacksAvl.setEnabled(false);
            updateInventoryBtn.setVisibility(View.GONE);
        }


        vehicleID = getIntent().getStringExtra("vehicleID");
        Cursor c = db.rawQuery(VEHICLE_INVENTORY_QUERY, new String[]{vehicleID});

        if (c.getCount() > 0) {
            c.moveToFirst();
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                switch (c.getString(c.getColumnIndex(Resources.ITEM_NAME))) {
                    case "Sandwiches":
                        swichAvl.setText(c.getString(c.getColumnIndex(Resources.VEHICLE_INVENTORY_QUANTITY)));
                        break;
                    case "Drinks":
                        drinksAvl.setText(c.getString(c.getColumnIndex(Resources.VEHICLE_INVENTORY_QUANTITY)));
                        break;
                    case "Snacks":
                        snacksAvl.setText(c.getString(c.getColumnIndex(Resources.VEHICLE_INVENTORY_QUANTITY)));
                        break;
                }
            }
        }

        updateInventoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateInventory();
                Intent intent = new Intent(VehicleInventoryScreen.this, VehicleScreen.class);
                startActivity(intent);
            }
        });
    }

    private void updateInventory() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Resources.VEHICLE_INVENTORY_QUANTITY, Integer.valueOf(swichAvl.getText().toString()));
        db.update(Resources.TABLE_VEHICLE_INVENTORY, contentValues, "item_id = ? and vehicle_id = ?", new String[]{"1", vehicleID});
        contentValues = new ContentValues();
        contentValues.put(Resources.VEHICLE_INVENTORY_QUANTITY, Integer.valueOf(drinksAvl.getText().toString()));
        db.update(Resources.TABLE_VEHICLE_INVENTORY, contentValues, "item_id = ? and vehicle_id = ?", new String[]{"2", vehicleID});
        contentValues = new ContentValues();
        contentValues.put(Resources.VEHICLE_INVENTORY_QUANTITY, Integer.valueOf(snacksAvl.getText().toString()));
        db.update(Resources.TABLE_VEHICLE_INVENTORY, contentValues, "item_id = ? and vehicle_id = ?", new String[]{"3", vehicleID});
        Toast.makeText(getApplicationContext(), "Inventory Updated", Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_menu, menu);
        SharedPreferences preferences = getSharedPreferences("currUser", MODE_PRIVATE);
        String role = preferences.getString("userRole", "");
        if ("Manager".equalsIgnoreCase(role)) {
            menu.findItem(R.id.app_bar_search).setVisible(true);
        }
        if ("Operator".equalsIgnoreCase(role)) {
            menu.findItem(R.id.Optr_vehicledetails).setVisible(true);
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
            case R.id.Optr_vehicledetails:
                vehicleSearch_optr();
                return true;
            case R.id.app_bar_search:
                vehicleSearch();
                return true;
            case R.id.menu_logout:
                logout();
                return true;
            case R.id.menu_home:
                SharedPreferences preferences = getSharedPreferences("currUser", MODE_PRIVATE);
                String role = preferences.getString("userRole", "");
                role = role + "HomeScreen";
                try {
                    Class<?> cls = Class.forName("com.example.utamobilevendingsystem.HomeScreens." + role);
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
        Intent viewOrders = new Intent(this, OperatorOrderDetails.class);
        SharedPreferences prefs = getSharedPreferences("currUser", MODE_PRIVATE);
        userID = prefs.getInt("userid", 0);
        viewOrders.putExtra("userId", String.valueOf(userID));
        startActivity(viewOrders);
    }

    private void vehicleSearch_optr() {
        Intent op_vehicle = new Intent(this, VehicleDetailsScreen.class);
        op_vehicle.putExtra("flag", "1");   //Sending a flag variable "1" as well
        startActivity(op_vehicle);
    }

    private void logout() {
        SharedPreferences.Editor editor = getSharedPreferences("currUser", MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
        Intent logout = new Intent(this, LoginActivity.class);
        startActivity(logout);
    }

    private void changePassword() {
        Intent changePasswordIntent = new Intent(this, ChangePassword.class);
        startActivity(changePasswordIntent);
    }

    private void viewLocationList() {
        Intent changePasswordIntent = new Intent(this, LocationScreen.class);
        startActivity(changePasswordIntent);
    }
}
