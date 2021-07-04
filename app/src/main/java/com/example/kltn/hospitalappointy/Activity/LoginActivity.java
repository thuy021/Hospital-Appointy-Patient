package com.example.kltn.hospitalappointy.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.kltn.hospitalappointy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout mEmail;
    private TextInputLayout mPassword;
    private Button mLogin;
    private Button mForgot;
    private Button mRegister;


    //Firebase Auth
    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private ProgressDialog mLoginProgress;

    SharedPreferences sharedPreferences;
    public static final String fileName = "login";
    public static final String UserName = "username";
    public static final String Password = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.login_toolbar);
        /*setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("LOG IN");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/


        mLoginProgress = new ProgressDialog(this);

        mEmail = (TextInputLayout) findViewById(R.id.login_email_layout);
        mPassword = (TextInputLayout) findViewById(R.id.login_password_layout);
        mLogin = (Button) findViewById(R.id.login_button);
        mForgot = (Button) findViewById(R.id.login_forgot_button);
        mRegister = (Button) findViewById(R.id.login_register_button);

        sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);
        if(sharedPreferences.contains(UserName)){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmail.getEditText().getText().toString();
                String password = mPassword.getEditText().getText().toString();

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

                    mLoginProgress.setTitle("Loggin In");
                    mLoginProgress.setMessage("Please wait! While your Account is Logging In");
                    mLoginProgress.setCanceledOnTouchOutside(false);
                    mLoginProgress.show();

                    login(email,password);
                }
                else {
                    Toast.makeText(LoginActivity.this,"Please Enter Email & Password",Toast.LENGTH_LONG).show();
                }

            }
        });

        mForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotPassword_Intent = new Intent(LoginActivity.this, Forgot_PasswordActivity.class);
                startActivity(forgotPassword_Intent);
            }
        });
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registration_Intent = new Intent(LoginActivity.this, Patient_RegistrationActivity.class);
                startActivity(registration_Intent);
            }
        });
    }

    private void login(final String email, final String password) {

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(UserName,email);
                            editor.putString(Password,password);
                            editor.commit();

                            mLoginProgress.dismiss();
                            Intent main_Intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(main_Intent);

                            //Toast.makeText(LoginActivity.this,"Successfully Logged IN",Toast.LENGTH_LONG).show();
                        }
                        else {

                            mLoginProgress.dismiss();
                            Toast.makeText(LoginActivity.this,"Entered Email & Password is wrong",Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}
