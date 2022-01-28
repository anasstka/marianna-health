package com.example.mhapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase firebaseDB;
    DatabaseReference users;

    TextInputLayout tilEmail,tilPassword;
    private EditText etEmail,etPassword;
    private TextView acceptLoginButton;

    private UserDBHelper helper;
    private SQLiteDatabase db;

    private UserProfileData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        firebaseDB = FirebaseDatabase.getInstance();
        users = firebaseDB.getReference(FirebaseConst.USERS_DB);

        data = new UserProfileData();

        tilEmail = findViewById(R.id.til_login);
        tilPassword = findViewById(R.id.til_password);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        acceptLoginButton = findViewById(R.id.accept_login_button);
        acceptLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateEmail() | !validatePassword()){
                    return;
                }
                boolean onSuccessAuthorization = false;
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                auth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                DatabaseReference user = users.child(auth.getCurrentUser().getUid());
                                user.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        data = snapshot.getValue(UserProfileData.class);

                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.putExtra("user_data", data);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "Такого профиля не существует!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private boolean validateEmail(){
        String emailInput = tilEmail.getEditText().getText().toString().trim();

        if(emailInput.isEmpty()){
            tilEmail.setError("Поле не может быть пустым");
            return false;
        }else{
            tilEmail.setError(null);
            return true;
        }
    }

    private boolean validatePassword(){
        String passwordInput = tilPassword.getEditText().getText().toString().trim();

        if(passwordInput.isEmpty()){
            tilPassword.setError("Поле не может быть пустым");
            return false;
        }else{
            tilPassword.setError(null);
            return true;
        }
    }

}