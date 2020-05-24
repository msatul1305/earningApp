package com.example.earningapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.earningapplication.Model.UserInformation;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.sql.Time;

public class EarningOne extends AppCompatActivity {
    private Button btnTaskOne;
    private Button btnTaskTwo;
    private Button btnTaskThree;
    private Button btnTaskFour;

    private Button btnGoBack;

    private InterstitialAd adOne;
    private int currentRupees,previousRupees;
    private int currentPoints,previousPoints;

    private String email,name,number,password;
    long hours;

    //Firebase
    private FirebaseDatabase database;
    private DatabaseReference ref,userInfoDatabase;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener stateListener;
    String firebaseId;

    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earning_one);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(InitializationStatus initializationStatus) {

                    }
                });

        btnTaskOne=(Button)findViewById(R.id.taskOne);
        btnTaskTwo=(Button)findViewById(R.id.taskTwo);
        btnTaskThree=(Button)findViewById(R.id.taskThree);
        btnTaskFour=(Button)findViewById(R.id.taskFour);
        btnGoBack=(Button)findViewById(R.id.goBack);

        editor=getSharedPreferences("SAVING_TIME",MODE_PRIVATE).edit();
        hours=new Time(System.currentTimeMillis()).getHours();

        adOne=new InterstitialAd(this);
        adOne.setAdUnitId("ca-app-pub-6447107122173317/3188294873");
        adOne.loadAd(new AdRequest.Builder().build());

        settingUpFirebase();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showingDatabaseFromFirebase(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EarningOne.this,"Error "+databaseError.getMessage(),Toast.LENGTH_SHORT);
            }
        });

        adOne.setAdListener(new AdListener(){
            @Override
            public void onAdClosed(){
                Toast.makeText(EarningOne.this,"You got 5 points",Toast.LENGTH_SHORT);
                currentPoints+=5;
                if(currentPoints>=10){
                    currentRupees+=1;
                }
            }
        });
        onClickListeners();
    }
    private void onClickListeners(){
        btnTaskOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adOne.isLoaded()){
                    adOne.show();
                    btnTaskOne.setVisibility(View.INVISIBLE);
                    btnTaskTwo.setVisibility(View.VISIBLE);
                }
                else {
                    Toast.makeText(EarningOne.this,"Click again after 5 seconds",Toast.LENGTH_SHORT);
                    Log.d("TAG", "onClick: The interstitial wasn't loaded yet. ");
                }
            }
        });
        btnTaskTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adOne.loadAd(new AdRequest.Builder().build());
                if (adOne.isLoaded()){
                    adOne.show();
                    btnTaskTwo.setVisibility(View.INVISIBLE);
                    btnTaskThree.setVisibility(View.VISIBLE);
                }
                else {
                    Toast.makeText(EarningOne.this,"Click again after 5 seconds",Toast.LENGTH_SHORT);
                }
            }
        });
        btnTaskThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adOne.loadAd(new AdRequest.Builder().build());
                if (adOne.isLoaded()){
                    adOne.show();
                    btnTaskThree.setVisibility(View.INVISIBLE);
                    btnTaskFour.setVisibility(View.VISIBLE);
                }
                else {
                    Toast.makeText(EarningOne.this,"Click again after 5 seconds",Toast.LENGTH_SHORT);
                }
            }
        });
        btnTaskFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adOne.loadAd(new AdRequest.Builder().build());
                if (adOne.isLoaded()){
                    adOne.show();
                    btnTaskFour.setVisibility(View.INVISIBLE);
                    btnGoBack.setVisibility(View.VISIBLE);
                }
                else {
                    Toast.makeText(EarningOne.this,"Click again after 5 seconds",Toast.LENGTH_SHORT);
                }
            }
        });
        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor=getSharedPreferences("SAVING_TIME",MODE_PRIVATE).edit();
                editor.putLong("hours",hours);
                editor.commit();
                savingTheNewDataInFirebase();
                startActivity(new Intent(EarningOne.this,MainMenuActivity.class));
                finish();
            }
        });
    }
    private void savingTheNewDataInFirebase(){
        previousRupees+=currentRupees;
        UserInformation information=new UserInformation(email,password,number,name,previousRupees,previousPoints);
        userInfoDatabase.child(firebaseId).setValue(information);
    }
    private void showingDatabaseFromFirebase(DataSnapshot ds){
        for (DataSnapshot dataSnapshot:ds.getChildren()){
            UserInformation userInformation =new UserInformation();
            userInformation.setName(dataSnapshot.child(firebaseId).getValue(UserInformation.class).getName());
            userInformation.setEmail(dataSnapshot.child(firebaseId).getValue(UserInformation.class).getEmail());
            userInformation.setNumber(dataSnapshot.child(firebaseId).getValue(UserInformation.class).getNumber());
            userInformation.setRupees(dataSnapshot.child(firebaseId).getValue(UserInformation.class).getRupees());
            userInformation.setPoints(dataSnapshot.child(firebaseId).getValue(UserInformation.class).getPoints());
            userInformation.setPassword(dataSnapshot.child(firebaseId).getValue(UserInformation.class).getPassword());

            name=userInformation.getName();
            email=userInformation.getEmail();
            password=userInformation.getPassword();
            number=userInformation.getNumber();
            previousRupees=userInformation.getRupees();
            previousPoints=userInformation.getPoints();
        }
    }

    private void settingUpFirebase(){
        database=FirebaseDatabase.getInstance();
        ref=database.getReference();
        userInfoDatabase=database.getReference("Users");
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        firebaseId=user.getUid();
    }

}
