package com.example.utamobilevendingsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.utamobilevendingsystem.HomeScreens.OperatorHomeScreen;
import com.example.utamobilevendingsystem.users.UserOrderDetailsAdapter;

import java.util.ArrayList;

public class OperatorOrderDetails extends AppCompatActivity {

    ArrayList<String> orderID = new ArrayList<>();
    ArrayList<String> orderItemID = new ArrayList<>();
    ArrayList<String> orderItemQuantity = new ArrayList<>();
    ArrayList<String> orderItemPrice = new ArrayList<>();
    ArrayList<String> orderStatusID = new ArrayList<>();
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    String userID;
    String TAG = "OperatorOrderDetails";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_order_details);
        Log.i(TAG, "OperatorOrderDetails: onCreate");
        userID = getIntent().getStringExtra("userId");
        dbHelper = new DatabaseHelper(this);
        db= dbHelper.getReadableDatabase();
        getData();
        initRecyclerView();
    }
    private void getData() {
        Log.i(TAG, "OperatorOrderDetails: getData");

            Cursor cursor = db.rawQuery("SELECT O.order_id, sum(O.order_item_quantity), sum(O.order_item_price), O.order_status_id FROM orders O LEFT JOIN vehicle V ON O.order_vehicle_id=V.location_id WHERE V.user_ID = ? GROUP BY O.order_id", new String[]{userID});
        if (cursor.getCount() >= 1) {
            int i = 0;
            while (cursor.moveToNext()) {
                orderID.add(i, cursor.getString(0));
                orderItemQuantity.add(i, cursor.getString(1));
                orderItemPrice.add(i, cursor.getString(2));
                orderStatusID.add(i, cursor.getString(3));
                i += 1;
            }
        }
    }

    private void initRecyclerView() {
        Log.i(TAG, "OperatorOrderDetails: init recyclerview.");
        RecyclerView recyclerView = findViewById(R.id.recyclerViewManager);
        UserOrderDetailsAdapter adapter = new UserOrderDetailsAdapter(OperatorOrderDetails.this, orderID , orderItemQuantity, orderItemPrice, orderStatusID);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                //startSettings();
                return true;
            case R.id.menu_logout:
                logout();
                return true;
            case R.id.menu_home:
                opHome();
                return true;
            case R.id.change_password:
                changePassword();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void opHome() {
        Intent opHome = new Intent(OperatorOrderDetails.this, OperatorHomeScreen.class);
        startActivity(opHome);
    }

    private void viewOrders() {
        Intent viewOrders = new Intent(OperatorOrderDetails.this, OperatorOrderDetails.class);
        SharedPreferences prefs = getSharedPreferences("currUser", MODE_PRIVATE);
        String uID = prefs.getString("userid", String.valueOf(0));
        viewOrders.putExtra("userId", String.valueOf(uID));
        startActivity(viewOrders);
    }

    private void logout() {
        SharedPreferences.Editor editor = getSharedPreferences("currUser", MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
        Intent logout = new Intent(OperatorOrderDetails.this, LoginActivity.class);
        Toast.makeText(getApplicationContext(),"Logged out Successfully",Toast.LENGTH_SHORT).show();
        startActivity(logout);
    }

    private void changePassword() {
        Intent changePasswordIntent = new Intent(OperatorOrderDetails.this, ChangePassword.class);
        startActivity(changePasswordIntent);
    }

    private void viewLocationList(){
        Intent changePasswordIntent = new Intent(OperatorOrderDetails.this, LocationScreen.class);
        startActivity(changePasswordIntent);
    }
}
