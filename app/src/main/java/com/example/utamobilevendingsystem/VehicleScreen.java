package com.example.utamobilevendingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.utamobilevendingsystem.domain.Status;
import com.example.utamobilevendingsystem.domain.Vehicle;
import com.example.utamobilevendingsystem.domain.VehicleType;

import java.util.ArrayList;

public class VehicleScreen extends AppCompatActivity {
    Button vehicleDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_screen);
        vehicleDetails = findViewById(R.id.vehicleDetails);
        ListView vehicleListView = (ListView) findViewById(R.id.vehicleList);

        Vehicle v1 = new Vehicle("Vehicle 1",VehicleType.FOOD_TRUCK, Status.AVAILABLE, 1);
        Vehicle v2 = new Vehicle("Vehicle 2",VehicleType.CART, Status.AVAILABLE, 2);
        Vehicle v3 = new Vehicle("Vehicle 3",VehicleType.FOOD_TRUCK, Status.AVAILABLE, 3);
        Vehicle v4 = new Vehicle("Vehicle 4",VehicleType.CART, Status.AVAILABLE, 4);
        Vehicle v5 = new Vehicle("Vehicle 5",VehicleType.CART, Status.AVAILABLE, 5);
        Vehicle v6 = new Vehicle("Vehicle 6",VehicleType.CART, Status.AVAILABLE, 6);
        Vehicle v7 = new Vehicle("Vehicle 7",VehicleType.CART, Status.AVAILABLE, 7);

        ArrayList<Vehicle> vehicleList = new ArrayList<>();
        vehicleList.add(v1);vehicleList.add(v2);vehicleList.add(v3);vehicleList.add(v4);
        vehicleList.add(v5);vehicleList.add(v6);vehicleList.add(v7);

        VehicleListAdapter adapter = new VehicleListAdapter(this, R.layout.vehicle_list_adaptor_view_layout, vehicleList);
        vehicleListView.setAdapter(adapter);
        vehicleDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myint = new Intent(VehicleScreen.this,VehicleDetailsScreen.class);
                startActivity(myint);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_menu,menu);
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
