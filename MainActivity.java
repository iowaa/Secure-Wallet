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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEditEmail;
    private EditText mEditPass;
    private Button mRegisterbtn;
    private TextView mSignIn;

    private ProgressDialog mprogressview;

    //   private Firebase mRootRef;
    private FirebaseAuth fireAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //  Firebase.setAndroidContext(this);

        fireAuth = FirebaseAuth.getInstance();

        if (fireAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, Profile_activity.class));
        }

        mEditEmail = (EditText) findViewById(R.id.textfield);
        mEditPass = (EditText) findViewById(R.id.passfield);
        mRegisterbtn = (Button) findViewById(R.id.buttonfield);
        mSignIn = (TextView) findViewById(R.id.textView);

        mprogressview = new ProgressDialog(this);

        mRegisterbtn.setOnClickListener(this);
        mSignIn.setOnClickListener(this);


        //  mRootRef= new Firebase("https://login-firebase-5ffdb.firebaseio.com/");

    }

    @Override
    public void onClick(View view) {
        if (view == mRegisterbtn) {
            registeUser();
        } else if (view == mSignIn) {
            signInUser();
        }

    }

    public void registeUser() {
        final String email = mEditEmail.getText().toString().trim();
        final String pass = mEditPass.getText().toString().trim();//trim removes the spaces at the beginning and the end of the string not the middle ones
        View focusview = null;

        if (TextUtils.isEmpty(email)) {
            mEditEmail.setError("Email is must");
            focusview = mEditEmail;
            return;

        }
        if (!isValidEmail(email)) {
            mEditEmail.setError("Email address is not valid");
            focusview = mEditEmail;
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            mEditPass.setError("Password is must");
            focusview = mEditPass;
            return;
        }
        if (isPasswordValid(pass)) {
            mEditPass.setError("Password must be atleast 6 characters");
            focusview = mEditPass;
            return;
        }
        mprogressview.setMessage("Registering User...");
        mprogressview.show();

        fireAuth.createUserWithEmailAndPassword(email, pass).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Registration Successful..", Toast.LENGTH_SHORT).show();
                            mprogressview.dismiss();
                            finish();
                            startActivity(new Intent(MainActivity.this, Profile_activity.class));
                        } else {
                            Toast.makeText(MainActivity.this, "Registration UnSuccessful..Please try again", Toast.LENGTH_SHORT).show();
                            mprogressview.dismiss();
                        }
                    }
                });


    }

    public void signInUser() {
        finish();
        startActivity(new Intent(this, Login_Activity.class));

    }

    private boolean isPasswordValid(CharSequence pass) {
        return pass.length() < 6;

    }


    public static final boolean isValidEmail(CharSequence email) {
        if (email == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

}
