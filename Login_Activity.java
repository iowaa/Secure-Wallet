package com.example.imas.login_firebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login_Activity extends AppCompatActivity implements View.OnClickListener {
    private EditText mEditEmail;
    private EditText mEditPass;
    private Button mLogin;
    private TextView mText;
    private ProgressDialog mProgress;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        mProgress = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, Profile_activity.class));
        }

        mEditEmail = (EditText) findViewById(R.id.textfield);
        mEditPass = (EditText) findViewById(R.id.passfield);
        mLogin = (Button) findViewById(R.id.buttonfield);
        mText = (TextView) findViewById(R.id.textView);

        mLogin.setOnClickListener(this);
        mText.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if (view == mLogin) {
            userLogin();
        } else if (view == mText) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private void userLogin() {
        String email = mEditEmail.getText().toString().trim();
        String pass = mEditPass.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            mEditEmail.setError("Please Enter the Email ID");
            return;
        }

        if (!isEmailVaild(email)) {
            mEditEmail.setError("Please Enter the Valid Email Address");
            return;

        }
        if (TextUtils.isEmpty(pass)) {
            mEditPass.setError("Please Enter the Password");
            return;
        }
        if (isPasswordValid(pass)) {
            mEditPass.setError("Password must be of 4 or more characters");
            return;

        }

        mProgress.setMessage("Login In...Please Wait");
        mProgress.show();

        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mProgress.dismiss();
                if (task.isSuccessful()) {
                    finish();
                    startActivity(new Intent(Login_Activity.this, Profile_activity.class));


                } else {
                    Toast.makeText(Login_Activity.this, "Invalid Username or Passsword", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private boolean isPasswordValid(String pass) {
        return pass.length() < 4;
    }

    private boolean isEmailVaild(String email) {
        if (email == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }
}
