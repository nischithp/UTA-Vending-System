package com.example.utamobilevendingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.utamobilevendingsystem.domain.UserDetails;

import java.io.Serializable;

public class ChangePassword extends AppCompatActivity {

    DatabaseHelper dbHelper;
    SQLiteDatabase db;

    EditText passwordET, reEnterPasswordET;
    Button changeBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        passwordET = findViewById(R.id.passwordET);
        reEnterPasswordET  = findViewById(R.id.reEnterPasswordET);
        changeBtn = findViewById(R.id.changeBtn);
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = passwordET.getText().toString();
                String reEnterPass = reEnterPasswordET.getText().toString();
                if(pass.isEmpty() || reEnterPass.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please enter both the fields..!",Toast.LENGTH_SHORT).show();
                } else if(validatePassword()){
                    updatePassword(passwordET.getText().toString());
                    Intent intent = new Intent(ChangePassword.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    passwordET.getText().clear();
                    reEnterPasswordET.getText().clear();
                    Toast.makeText(getApplicationContext(), "Entered Passwords Do Not Match..!!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean validatePassword(){
        String password = passwordET.getText().toString();
        String reEnterPassword = reEnterPasswordET.getText().toString();
        if(password.equalsIgnoreCase(reEnterPassword)){
            return true;
        }
        return false;
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

    public void updatePassword(String password){
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        SharedPreferences prefs = getSharedPreferences("currUser", MODE_PRIVATE);
        int userID = prefs.getInt("userid",0);
        Log.i(" userId","user ID  "+userID);
        ContentValues cv = new ContentValues();
        cv.put(Resources.USER_CREDS_PASSWORD, password);
        String tableName = Resources.TABLE_USER_CREDS;
        int value = db.update(Resources.TABLE_USER_CREDS,cv ,"user_id = "+ userID, null);
    }
}
