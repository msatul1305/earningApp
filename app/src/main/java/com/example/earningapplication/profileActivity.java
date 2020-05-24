package com.example.earningapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class profileActivity extends AppCompatActivity {
    private TextView mUsername;
    private TextView mNumber;
    private TextView mEmail;
    private TextView currentRupees;
    private TextView currentPoints;
    private TextView userId;
    private Button btnLogout;

    private FirebaseAuth mAuth;
    private FirebaseDatabase userDatabase;
    private DatabaseReference reference;
    private FirebaseAuth.AuthStateListener listener;
    private FirebaseUser user;
    private String firebaseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btnLogout=(Button)findViewById(R.id.btnLogout);
        mUsername=(TextView)findViewById(R.id.username);
        mNumber=(TextView)findViewById(R.id.number);
        mEmail=(TextView)findViewById(R.id.email);

        currentPoints=(TextView)findViewById(R.id.userPoints);
        currentRupees=(TextView)findViewById(R.id.userRupees);
        userId=(TextView)findViewById(R.id.userUniqueId);

        userDatabase=FirebaseDatabase.getInstance();
        reference=userDatabase.getReference();
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        firebaseId=user.getUid();


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(profileActivity.this,MainMenuActivity.class));
                finish();
            }
        });
        stateListener();
        gettingDatabase();
    }
    private void stateListener(){
        listener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if (user==null){
                    Toast.makeText(profileActivity.this,"Logged out",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        };

    }
    private void gettingDatabase(){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    UserInformation information=new UserInformation();
                    information.setName(ds.child(firebaseId).getValue(UserInformation.class).getName());
                    information.setEmail(ds.child(firebaseId).getValue(UserInformation.class).getEmail());
                    information.setNumber(ds.child(firebaseId).getValue(UserInformation.class).getNumber());
                    information.setPoints(ds.child(firebaseId).getValue(UserInformation.class).getPoints());
                    information.setRupees(ds.child(firebaseId).getValue(UserInformation.class).getRupees());

                    mUsername.setText(information.getName());
                    mEmail.setText(information.getEmail());
                    mNumber.setText(information.getNumber());
                    currentPoints.setText(Integer.toString(information.getPoints()));
                    currentRupees.setText(Integer.toString(information.getRupees()));

                    userId.setText(firebaseId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(profileActivity.this,"Error"+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {

        super.onStart();
        mAuth.addAuthStateListener(listener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(listener!=null){
            mAuth.removeAuthStateListener(listener);
        }
    }
}
