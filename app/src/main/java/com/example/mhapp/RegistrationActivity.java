package com.example.mhapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase firebaseDB;
    DatabaseReference users;
    DatabaseReference userData;

    TextInputLayout tilName,tilPassword,tilRepPassword;
    EditText etLoginField,etPasswordField,etRepPassword;
    TextView acceptButton;
    UserProfileData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        auth = FirebaseAuth.getInstance();
        firebaseDB = FirebaseDatabase.getInstance();
        users = firebaseDB.getReference(FirebaseConst.USERS_DB);
        userData = firebaseDB.getReference(FirebaseConst.USER_DATA_DB);

        Bundle bundle = getIntent().getExtras();
        data = (UserProfileData) bundle.getSerializable("user_data");

        tilName = findViewById(R.id.til_login);
        tilPassword = findViewById(R.id.til_password);
        tilRepPassword = findViewById(R.id.til_rep_password);

        etLoginField = findViewById(R.id.et_login);
        etPasswordField = findViewById(R.id.et_password);
        etRepPassword = findViewById(R.id.et_rep_password);
        acceptButton = findViewById(R.id.accept_registration_button);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!validateEmail() | !validatePassword() | !validateRepPassword()){
                    return;
                }

                String password1 = etPasswordField.getText().toString();
                String password2 = etRepPassword.getText().toString();

                boolean onEqualPasswords = false;
                if (password1.length() == password2.length()) {
                    int charCounter = 0;
                    for (int i = 0; i < password1.length(); i++) {
                        if (password1.charAt(i) == password2.charAt(i)) {
                            charCounter++;
                        }
                    }
                    if (charCounter == password1.length()) {
                        onEqualPasswords = true;
                    }
                }

                if (onEqualPasswords) {
                    String email =  etLoginField.getText().toString();
                    String password = etPasswordField.getText().toString();

                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(data)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    auth.signOut();
                                                    Toast.makeText(getApplicationContext(), "Регистрация прошла успешно!", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getApplicationContext(), "Ощибка регистрации. " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Ощибка регистрации. " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                } else {
                    Toast.makeText(RegistrationActivity.this, "Пароли не совпадают", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean validateEmail(){
        String emailInput = tilName.getEditText().getText().toString().trim();

        if(emailInput.isEmpty()){
            tilName.setError("Поле не может быть пустым");
            return false;
        }else if(emailInput.length() > 25){
            tilName.setError("Превышена длина почты");
            return false;
        }else{
            tilName.setError(null);
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

    private boolean validateRepPassword(){
        String passwordRepInput = tilRepPassword.getEditText().getText().toString().trim();

        if(passwordRepInput.isEmpty()){
            tilRepPassword.setError("Поле не может быть пустым");
            return false;
        }else{
            tilRepPassword.setError(null);
            return true;
        }
    }

}