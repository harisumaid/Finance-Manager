package com.example.initish.finance_manager;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener{

    ProgressBar progressbar;
    EditText actusr,actmail,actpass;

    private FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        actmail=(EditText) findViewById(R.id.actmail);
        actpass=(EditText) findViewById(R.id.actpass);
        actusr=(EditText) findViewById(R.id.actusr);
        progressbar=(ProgressBar) findViewById(R.id.progressbar);
        Button btn= (Button) findViewById(R.id.actsignup);
        btn.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
    }

    private void registerUser(){
        String mail=actmail.getText().toString().trim();
        String passwd=actpass.getText().toString().trim();
        String usrname=actusr.getText().toString().trim();

        if(usrname.isEmpty()){
            actusr.setError("Username is required");
            actusr.requestFocus();
            return;
        }

        if(mail.isEmpty()){
            actmail.setError("E mail is required");
            actmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            actmail.setError("E mail is required");
            actmail.requestFocus();
            return;
        }
        if(passwd.isEmpty()){
            actpass.setError("Password is required");
            actpass.requestFocus();
            return;
        }
        if(passwd.length()<6){
            actpass.setError("Minimum length of Password is 6");
            actpass.requestFocus();
            return;
        }

        progressbar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(mail,passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressbar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"User registered Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),Main2Activity.class));
                }
                else{
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(),"You are already registered",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.actsignup:
                registerUser();
        }
    }

}
