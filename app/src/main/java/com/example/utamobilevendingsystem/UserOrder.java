package com.example.utamobilevendingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.utamobilevendingsystem.domain.Item;
import com.example.utamobilevendingsystem.domain.OrderItem;
import com.example.utamobilevendingsystem.domain.Payments;
import com.example.utamobilevendingsystem.domain.UserCart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserOrder extends AppCompatActivity {
    SQLiteDatabase db;
    TextView swichAvl,drinksAvl,snacksAvl,totalPrice,switchPrice,drinksPrice,snacksPrice;
    EditText switchQty,drinksQty,snacksQty;
    Button placeOrder;
    UserCart userCart = new UserCart();
    HashMap<Integer,Integer> vehicleInventory = new HashMap<>();
    ContentValues cart = new ContentValues();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order);
        Intent myint = getIntent();
        Bundle extras = myint.getExtras();
        String location="";
        int locationId=0;
        if(extras!=null)
        {
             location = myint.getStringExtra("location");
             locationId = myint.getIntExtra("id",0);
        }

        db= DatabaseHelper.getInstance(getApplicationContext()).getWritableDatabase();
        swichAvl= findViewById(R.id.swichAvl);
        drinksAvl= findViewById(R.id.drinksAvl);
        snacksAvl= findViewById(R.id.snacksAvl);
        totalPrice= findViewById(R.id.totalPrice);
        switchPrice= findViewById(R.id.switchPrice);
        drinksPrice= findViewById(R.id.drinksPrice);
        snacksPrice= findViewById(R.id.snacksPrice);
        switchQty= findViewById(R.id.switchQty);
        drinksQty= findViewById(R.id.drinksQty);
        snacksQty= findViewById(R.id.snacksQty);
        placeOrder= findViewById(R.id.placeOrder);
        getInventory(locationId);
        switchQty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                getUserInput();
            }
        });
        drinksQty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                getUserInput();
            }
        });
        snacksQty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                getUserInput();
            }
        });
    }

    private void getInventory(int locationId) {
        String selectQuery = "SELECT "+Resources.VEHICLE_ID+ " FROM " + Resources.TABLE_VEHICLE + " WHERE "
                + Resources.VEHICLE_LOCATION_ID + " = " + locationId;
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.getCount() > 0){
            c.moveToFirst();
        }
        int  vehicleID =c.getInt(c.getColumnIndex(Resources.VEHICLE_ID));

        String inventoryQuery= "SELECT * FROM "+ Resources.TABLE_VEHICLE_INVENTORY+ " WHERE " +  Resources.VEHICLE_INVENTORY_VEHICLE_ID + " = " + vehicleID;
        Cursor c1 = db.rawQuery(inventoryQuery, null);
        int count= c1.getCount();

        while (count >0){
            c1.moveToPosition(count-1);
            int item_id= c1.getInt(c1.getColumnIndex(Resources.VEHICLE_INVENTORY_ITEM_ID));
            int quantity= c1.getInt(c1.getColumnIndex(Resources.VEHICLE_INVENTORY_QUANTITY));
            vehicleInventory.put(item_id,quantity);
            count--;
        }
        swichAvl.setText(String.valueOf(vehicleInventory.get(1)));
        drinksAvl.setText(String.valueOf(vehicleInventory.get(2)));
        snacksAvl.setText(String.valueOf(vehicleInventory.get(3)));
    }

    private void getUserInput() {
            double swichPrice = Double.parseDouble(switchPrice.getText().toString().substring(1)) * Integer.parseInt(switchQty.getText().toString());
            double drnksPrice = Double.parseDouble(drinksPrice.getText().toString().substring(1)) * Integer.parseInt(drinksQty.getText().toString());
            double snksPrice = Double.parseDouble(snacksPrice.getText().toString().substring(1)) * Integer.parseInt(snacksQty.getText().toString());

            double total = swichPrice+drnksPrice+snksPrice;
            totalPrice.setText("Total = $"+total);
            placeOrderMethod(total);
    }

    private void placeOrderMethod(double total) {
        placeOrder.setClickable(true);
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag= true;
                if(Integer.parseInt(switchQty.getText().toString()) > Integer.parseInt(switchQty.getText().toString())){
                    switchQty.setError("Please enter according to availability of item");
                    flag= false;
                }
                if(Integer.parseInt(drinksQty.getText().toString()) > Integer.parseInt(drinksQty.getText().toString())){
                    switchQty.setError("Please enter according to availability of item");
                    flag= false;
                }
                if(Integer.parseInt(snacksQty.getText().toString()) > Integer.parseInt(snacksQty.getText().toString())){
                    switchQty.setError("Please enter according to availability of item");
                    flag= false;
                }
                if(flag){
                    SQLiteDatabase db=DatabaseHelper.getInstance(getApplicationContext()).getWritableDatabase();
                    SharedPreferences prefs = getSharedPreferences("currUser", MODE_PRIVATE);
                    int uid =prefs.getInt("userid",0);
                    cart.put("cart_ID",uid);
                    cart.put("cart_item_id",1);
                    cart.put("quantity",Integer.parseInt(switchQty.getText().toString()));
                    db.insert(Resources.TABLE_CART,null, cart);
                    cart.put("cart_ID",uid);
                    cart.put("cart_item_id",2);
                    cart.put("quantity",Integer.parseInt(drinksQty.getText().toString()));
                    db.insert(Resources.TABLE_CART,null, cart);
                    cart.put("cart_ID",uid);
                    cart.put("cart_item_id",3);
                    cart.put("quantity",Integer.parseInt(snacksQty.getText().toString()));
                    db.insert(Resources.TABLE_CART,null, cart);
                    Intent myint = new Intent(UserOrder.this, CardDetails.class);
                    myint.putExtra("total",total);
                    myint.putExtra("uid",uid);
                    startActivity(myint);
                }
            }
        });
    }

}
