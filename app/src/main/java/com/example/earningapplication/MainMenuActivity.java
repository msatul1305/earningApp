package com.example.earningapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRouter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.earningapplication.Model.UserInformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;

public class MainMenuActivity extends AppCompatActivity {
    private Button btnProfile;
    private Button btnEarningOne;
    private Button btnEarningTwo;
    private Button btnWebView;
    private Button btnGetPayment;
    private TextView currentPoints;
    private TextView currentRupees;
    //Firebase
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener stateListener;
    String firebaseId;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    long hours,previousTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        btnProfile=(Button)findViewById(R.id.btnProfile);
        currentPoints=(TextView)findViewById(R.id.userPoints);
        currentRupees=(TextView)findViewById(R.id.userRupees);

        btnEarningOne=(Button)findViewById(R.id.btnEarning1);
        btnEarningTwo=(Button)findViewById(R.id.btnEarning2);
        btnGetPayment=(Button)findViewById(R.id.btnGetPayment);
        btnWebView=(Button)findViewById(R.id.btnLearn);

        preferences=getSharedPreferences("SAVING TIME",MODE_PRIVATE);
        hours=new Time(System.currentTimeMillis()).getHours();
        buttonEnableMethod();

        database=FirebaseDatabase.getInstance();
        ref=database.getReference();
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        firebaseId=user.getUid();

        authstateListener();
        showingDataFromDatabase();
        allOnClickListeners();
    }
    private void allOnClickListeners(){
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenuActivity.this,profileActivity.class));
            }
        });
        btnEarningOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenuActivity.this,EarningOne.class));
            }
        });
        btnEarningTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenuActivity.this,EarningTwo.class));
            }
        });
        btnGetPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenuActivity.this,GetPayment.class));
            }
        });
        btnWebView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenuActivity.this,WatchAndLearn.class));
            }
        });
    }
    private void showingDataFromDatabase(){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    UserInformation info=new UserInformation();
                    info.setRupees(ds.child(firebaseId).getValue(UserInformation.class).getRupees());
                    info.setPoints(ds.child(firebaseId).getValue(UserInformation.class).getPoints());

                    currentRupees.setText(Integer.toString(info.getRupees()));
                    currentPoints.setText(Integer.toString(info.getPoints()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainMenuActivity.this,"Error "+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void authstateListener(){
        stateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()==null){
                    startActivity(new Intent(MainMenuActivity.this,profileActivity.class));
                }
            }
        };
    }

    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(stateListener);
    }
    @Override
    protected void onStop(){
        super.onStop();
        if(stateListener!=null){
            mAuth.removeAuthStateListener(stateListener);
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        buttonEnableMethod();
    }
    private void buttonEnableMethod(){
        hours=new Time(System.currentTimeMillis()).getHours();
        previousTime=preferences.getLong("hours",0);
        if ((hours-previousTime>2)||(hours-previousTime>2)){
            btnEarningOne.setEnabled(true);
            btnEarningOne.setBackgroundResource(R.drawable.backbtn);
        }
        else {
            btnEarningOne.setEnabled(false);
            btnEarningOne.setBackgroundResource(R.color.orange);
        }
    }
}
