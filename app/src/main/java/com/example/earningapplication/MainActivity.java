package com.example.earningapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText etEmail;
    private  EditText etPassword;

    private CheckBox rememberMe;

    //Buttons
    private Button btnLogin;
    private Button btnSignUp;

    private SharedPreferences loginPref;
    private SharedPreferences.Editor prefs;
    private boolean check;
    private String email;
    private String password;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmail=(EditText) findViewById(R.id.etEmail);
        etPassword=(EditText)findViewById(R.id.etPassword);
        btnLogin=(Button) findViewById(R.id.btnLogin);
        btnSignUp=(Button) findViewById(R.id.btnRegister);
        rememberMe=(CheckBox) findViewById(R.id.checkbox);

        mAuth = FirebaseAuth.getInstance();
        loginPref=getSharedPreferences("loginPrefs",MODE_PRIVATE);
        prefs=loginPref.edit();

        check=loginPref.getBoolean("savelogin",false);
        if(check==true){
            etEmail.setText(loginPref.getString("username",""));
            etPassword.setText(loginPref.getString("password",""));
            rememberMe.setChecked(true);
        }

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SignUpActivity.class));
//                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRememberMeMethod();
                loginMethod();
            }
        });
    }
    private void loginMethod(){
        mAuth.signInWithEmailAndPassword(etEmail.getText().toString(),etPassword.getText().toString()).addOnSuccessListener(
                new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        startActivity(new Intent(MainActivity.this,MainMenuActivity.class));
                        finish();
                    }
                }
        ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,"Error"+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setRememberMeMethod(){
        InputMethodManager imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etEmail.getWindowToken(),0);

        email=etEmail.getText().toString();
        password=etPassword.getText().toString();

        if(rememberMe.isChecked()){
            prefs.putBoolean("savelogin",true);
            prefs.putString("username",email);
            prefs.putString("password",password);
            prefs.commit();
        }
        else{
            prefs.clear();
            prefs.commit();
        }
    }
}
