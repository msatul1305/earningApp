package com.example.earningapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.earningapplication.Model.RequestPayment;
import com.example.earningapplication.Model.UserInformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

public class GetPayment extends AppCompatActivity {
    private EditText etUnumber;
    private Button btnGetPayment;

    private FirebaseDatabase database;
    private DatabaseReference userInfoDatabase,requestDatabase;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    String firebaseId;

    String usernameGT,emailGT,numberGT;
    int balanceGT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_payment);

        etUnumber=(EditText)findViewById(R.id.etNumber);
        btnGetPayment=(Button)findViewById(R.id.btnGetPayment);

        final String currentDateTime= DateFormat.getDateInstance().format(new Date());
        database=FirebaseDatabase.getInstance();
        userInfoDatabase=database.getReference();
        requestDatabase=database.getReference("RequestPayment");
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        firebaseId=user.getUid();

        userInfoDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showingDatabaseFromFirebase(dataSnapshot);
            }

            private void showingDatabaseFromFirebase(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    UserInformation userInformation =new UserInformation();
                    userInformation.setName(ds.child(firebaseId).getValue(UserInformation.class).getName());
                    userInformation.setEmail(ds.child(firebaseId).getValue(UserInformation.class).getEmail());
                    userInformation.setNumber(ds.child(firebaseId).getValue(UserInformation.class).getNumber());
                    userInformation.setRupees(ds.child(firebaseId).getValue(UserInformation.class).getRupees());

                    usernameGT=userInformation.getName();
                    emailGT=userInformation.getEmail();
                    numberGT=userInformation.getNumber();
                    balanceGT=userInformation.getRupees();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(GetPayment.this,"Error "+databaseError.getMessage(),Toast.LENGTH_SHORT);
            }
        });
        btnGetPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String paytmNumber=etUnumber.getText().toString();
                if(balanceGT>=2000){
                    if (!TextUtils.isEmpty(paytmNumber)){
                        RequestPayment payment=new RequestPayment(usernameGT,emailGT,numberGT,paytmNumber,currentDateTime,balanceGT);
                        requestDatabase.child(firebaseId).setValue(payment);
                        Toast.makeText(GetPayment.this,"Request Payment Sent",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(GetPayment.this,MainMenuActivity.class));
                        finish();
                    }
                    else {
                        Toast.makeText(GetPayment.this,"Fill all fields first",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(GetPayment.this,"Your balance is less than 2k",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
