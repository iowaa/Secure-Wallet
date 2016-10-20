package com.example.imas.login_firebase;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Profile_activity extends AppCompatActivity implements View.OnClickListener {

    private TextView mText;
    private Button mButton, mSave;
    private FirebaseAuth mAuth;
    private EditText mEditName, mMobile, mAddress;

    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_activity);

        mAuth = FirebaseAuth.getInstance();

        dbRef = FirebaseDatabase.getInstance().getReference();

        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, Login_Activity.class));
        }

        FirebaseUser mFirebaseUser = mAuth.getCurrentUser();

        mText = (TextView) findViewById(R.id.textView3);
        mButton = (Button) findViewById(R.id.logoutbutton);
        mEditName = (EditText) findViewById(R.id.editName);
        mMobile = (EditText) findViewById(R.id.editMobile);
        mAddress = (EditText) findViewById(R.id.editAddress);
        mSave = (Button) findViewById(R.id.buttonSave);


        mText.setText("Welcome  " + mFirebaseUser.getEmail());

        mButton.setOnClickListener(this);
        mSave.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == mButton) {
            logout();

        }

        if (view == mSave) {
            saveData();
        }
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mAuth.signOut();
                finish();
                startActivity(new Intent(Profile_activity.this, Login_Activity.class));

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void saveData() {

        String name = mEditName.getText().toString().trim();
        String mobile = mMobile.getText().toString().trim();
        String address = mAddress.getText().toString().trim();

        if ((TextUtils.isEmpty(name)) || (TextUtils.isEmpty(mobile)) || (TextUtils.isEmpty(address))) {
            Toast.makeText(this, "Please fill all your details..", Toast.LENGTH_SHORT).show();
        } else {

            UserInformation userinfo = new UserInformation(name, mobile, address);
            FirebaseUser user = mAuth.getCurrentUser();
            dbRef.child(user.getUid()).setValue(userinfo);

            Toast.makeText(this, "Information Saved to Database", Toast.LENGTH_SHORT).show();

            mEditName.setText("");
            mMobile.setText("");
            mAddress.setText("");
        }
    }
}
