package com.example.utamobilevendingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

public class OrderConfirmation extends AppCompatActivity {
    int userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);
        Intent newint = getIntent();
        userId=newint.getIntExtra("uid",0);
        fetchCurrOrder(userId);
    }

    private void fetchCurrOrder(int userId) {
        SQLiteDatabase db = DatabaseHelper.getInstance(getApplicationContext()).getWritableDatabase();
        String inventoryQuery= "SELECT * FROM "+ Resources.TABLE_CART+ " WHERE " +  Resources.CART_ID + " = " + userId;
        Cursor c1 = db.rawQuery(inventoryQuery, null);
        int count= c1.getCount();

        while (count >0){
            c1.moveToPosition(count-1);
            int item_id= c1.getInt(c1.getColumnIndex(Resources.CART_ITEM_ID));
            int quantity= c1.getInt(c1.getColumnIndex(Resources.CART_QUANTITY));
            count--;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_menu,menu);
        return true;
    }
}
