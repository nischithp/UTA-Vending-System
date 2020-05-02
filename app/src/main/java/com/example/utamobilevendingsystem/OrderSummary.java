package com.example.utamobilevendingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.utamobilevendingsystem.users.UserOrderDetails;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class OrderSummary extends AppCompatActivity {

    private static DecimalFormat df = new DecimalFormat("0.00");
    DatabaseHelper dbHelper;
    String TAG = "OrderSummary";
    SQLiteDatabase db;
    TextView sandwichQuantity, drinkQuantity, snackQuantity;
    TextView sandwichPrice, drinkPrice, snackPrice;
    TextView totalPrice;
    ArrayList<String> orderItemQuantity = new ArrayList<>();
    ArrayList<String> orderItemPrice = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);
        getAndPutDataUsers();
//        confirmOrder = findViewById(R.id.orderConfirm);
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
        Intent viewOrders = new Intent(this, OperatorOrderDetails.class);
        SharedPreferences prefs = getSharedPreferences("currUser", MODE_PRIVATE);
        String uID = prefs.getString("userid", String.valueOf(0));
        viewOrders.putExtra("userId", String.valueOf(uID));
        startActivity(viewOrders);
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

    private void getAndPutDataUsers() {
        Log.i(TAG, "OrderSummary: getAndPutDataUsers");
        sandwichPrice = findViewById(R.id.sandwichPrice);
        drinkPrice = findViewById(R.id.drinkPrice);
        snackPrice = findViewById(R.id.snackPrice);
        sandwichQuantity = findViewById(R.id.sandwichQuantity);
        drinkQuantity = findViewById(R.id.drinkQuantity);
        snackQuantity = findViewById(R.id.snackQuantity);
        totalPrice = findViewById(R.id.totalPrice);

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        SharedPreferences prefs = getSharedPreferences("currUser", MODE_PRIVATE);
        int uid = prefs.getInt("userid", 0);
        Intent intent = getIntent();
        String orderID = intent.getStringExtra("orderID");
        String totalprice = intent.getStringExtra("totalPrice");
        String context = intent.getStringExtra("context");
        Cursor cursor = db.rawQuery("SELECT order_item_quantity, order_item_price FROM orders WHERE order_id=" + orderID, null);
        int loopCounter = 0;
        int counter = 0;
        int i = 0;
        while (cursor.moveToNext()) {
            if (loopCounter == 0) {
                counter = 0;
                orderItemPrice.add(counter, cursor.getString(1));
                sandwichPrice.setText(orderItemPrice.get(counter));
                i += 1;
                orderItemQuantity.add(counter, cursor.getString(0));
                sandwichQuantity.setText(orderItemQuantity.get(counter));
            } else if (loopCounter == 1) {
                counter = 1;
                orderItemPrice.add(counter, cursor.getString(1));
                drinkQuantity.setText(orderItemPrice.get(counter));
                i += 1;
                orderItemQuantity.add(counter, cursor.getString(0));
                drinkPrice.setText(orderItemQuantity.get(counter));
            } else if (loopCounter == 2) {
                counter = 2;
                orderItemPrice.add(counter, cursor.getString(0));
                snackPrice.setText(orderItemPrice.get(counter));
                i += 1;
                orderItemQuantity.add(counter, cursor.getString(1));
                snackQuantity.setText(orderItemQuantity.get(counter));
            }
            loopCounter += 1;
            i += 1;
        }
        double totalWithTax = Double.parseDouble(totalprice) + (0.0825*Double.parseDouble(totalprice));
        totalPrice.setText(String.valueOf(df.format(totalWithTax)));
    }

}
