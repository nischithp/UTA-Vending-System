package com.example.utamobilevendingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.utamobilevendingsystem.domain.CardType;
import com.example.utamobilevendingsystem.domain.Payments;
import com.example.utamobilevendingsystem.domain.Status;

public class CardDetails extends AppCompatActivity {
    EditText expiryED,cvvED,cardNumberED;
    TextView totalPrice;
    Button validate;
    int cvv;
    String cardNumber;
    String expiry;
    int userId;
    double total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent newint = getIntent();
        userId=newint.getIntExtra("uid",0);
        total=newint.getDoubleExtra("total",0.0);
        setContentView(R.layout.activity_card_details);
        expiryED= findViewById(R.id.expiry);
        cvvED= findViewById(R.id.cvv);
        cardNumberED= findViewById(R.id.cardNumber);
        totalPrice=findViewById(R.id.totalPrice);
        totalPrice.setText("Total amount is : $"+total);
        validate= findViewById(R.id.validate);
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expiry= expiryED.getText().toString();
                cvv= Integer.parseInt(cvvED.getText().toString());
                cardNumber= cardNumberED.getText().toString();
                boolean flag=true;
                if(cardNumberED.getText().toString().length()!=16){
                    flag=false;
                    cardNumberED.setError("Please enter 16 digit card number");
                }
                if(cvvED.getText().toString().length()!=3){
                    flag=false;
                    cvvED.setError("Please enter 3 digit CVV");
                }

                if(flag){
                    Payments payments = new Payments();
                    payments.setExpirationDate(expiry);
                    payments.setCardType(CardType.CREDIT);
                    ContentValues cardDetails= new ContentValues();
                    cardDetails.put("user_id",userId);
                    cardDetails.put("card_number",cardNumber);
                    cardDetails.put("expiration_date",expiry);
                    cardDetails.put("card_type",CardType.CREDIT.name());
                    SQLiteDatabase db = DatabaseHelper.getInstance(getApplicationContext()).getWritableDatabase();
                    db.insert(Resources.TABLE_PAYMENTS,null, cardDetails);
                    Intent myint= new Intent(CardDetails.this,OrderConfirmation.class);
                    myint.putExtra("userid",userId);
                    startActivity(myint);
                }
            }
        });
    }
}
