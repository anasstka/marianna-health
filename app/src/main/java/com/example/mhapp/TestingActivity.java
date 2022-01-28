package com.example.mhapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class  TestingActivity extends AppCompatActivity {

    EditText nameField,tellField,etDate;
    AutoCompleteTextView sexSpinner,lifeStyleSpinner,purposeSpinner;
    EditText startWeightField;
    EditText finishWeightField;
    TextInputLayout tilName,tilGender,tilHigh,tilLifeStyle,tilPurpose,tilStartWeight,tilEndWeight,til_data_birth;
    TextView continueButton;

    UserPurpose temp_user_purpose = UserPurpose.SUPPORT_PURPOSE;
    UserSex temp_user_sex = UserSex.MEN;
    float activityValue = 1.2f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        tilName = findViewById(R.id.til_name);
        tilGender = findViewById(R.id.til_gender);
        tilHigh = findViewById(R.id.til_high);
        tilLifeStyle = findViewById(R.id.til_style_spinner);
        tilPurpose = findViewById(R.id.til_purpose_spinner);
        tilStartWeight = findViewById(R.id.til_start_weight);
        tilEndWeight = findViewById(R.id.til_finish_weight);

        etDate = findViewById(R.id.et_data_birth);
        til_data_birth = findViewById(R.id.til_data_birth);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        startWeightField = findViewById(R.id.et_start_weight);
        finishWeightField = findViewById(R.id.et_finish_weight);

        continueButton = findViewById(R.id.continue_button);

        etDate.setInputType(InputType.TYPE_NULL);

        etDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    DatePickerDialog datePickerDialog = new DatePickerDialog(TestingActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int day) {
                            month = month+1;
                            String date = day+"."+ (month < 10 ? "0" + month : month)+"."+year;
                            etDate.setText(date);
                        }
                    },year,month,day);
                    datePickerDialog.show();
                }
            }
        });
            continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateName() | !validateGender() | !validateHigh() | !validateLifeStyle() | !validatePurpose() | !validateDate() | !validateStartWeight() | !validateFinishWeight()){
                    return;
                }
                UserProfileData data = new UserProfileData();
                data.setName(nameField.getText().toString());
                Log.d("log",etDate.getText().toString());
                data.setDateOfBorn(etDate.getText().toString());
                data.setPurpose(temp_user_purpose);
                data.setTell(Float.parseFloat(tellField.getText().toString()));
                data.setStartWeight(Float.parseFloat(startWeightField.getText().toString()));
                if (temp_user_purpose == UserPurpose.SUPPORT_PURPOSE) {
                    data.setFinishWeight(Float.parseFloat(startWeightField.getText().toString()));
                } else {
                    data.setFinishWeight(Float.parseFloat(finishWeightField.getText().toString()));
                }
                data.setActivityValue(activityValue);
                data.setSex(temp_user_sex);

                Intent intent = new Intent(TestingActivity.this, RegistrationActivity.class);
                intent.putExtra("user_data", data);
                startActivity(intent);
            }
        });

        lifeStyleSpinner = findViewById(R.id.et_life_style_spinner);
        ArrayAdapter<String> lyfeStyleAdapter = new ArrayAdapter<>(TestingActivity.this, R.layout.support_simple_spinner_dropdown_item, new String[]{
            "Не активный", "Умеренные нагрузки", "3-5 раз в неделю", "Интенсивные нагрузки", "Я спортсмен"
        });
        lifeStyleSpinner.setAdapter(lyfeStyleAdapter);
        lifeStyleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        activityValue = 1.2f;
                        break;
                    case 1:
                        activityValue = 1.375f;
                        break;
                    case 2:
                        activityValue = 1.55f;
                        break;
                    case 3:
                        activityValue = 1.725f;
                        break;
                    case 4:
                        activityValue = 1.9f;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        purposeSpinner = findViewById(R.id.et_purpose_spinner);
        ArrayAdapter<String> purposeAdapter = new ArrayAdapter<>(TestingActivity.this, R.layout.support_simple_spinner_dropdown_item, new String[]{
                "Сбросить вес", "Поддержать вес", "Набрать вес"
        });
        purposeSpinner.setAdapter(purposeAdapter);
        purposeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        temp_user_purpose = UserPurpose.SLIM_PURPOSE;
                        finishWeightField.setEnabled(true);
                        break;
                    case 1:
                        temp_user_purpose = UserPurpose.SUPPORT_PURPOSE;
                        finishWeightField.setEnabled(false);
                        break;
                    case 2:
                        temp_user_purpose = UserPurpose.GAIN_PURPOSE;
                        finishWeightField.setEnabled(true);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        nameField = findViewById(R.id.et_name);
        sexSpinner = findViewById(R.id.et_user_sex_spinner);
        ArrayAdapter<String> sex = new ArrayAdapter<>(TestingActivity.this, R.layout.support_simple_spinner_dropdown_item, new String[]{
                "Мужской", "Женский"
        });
        sexSpinner.setAdapter(sex);
        sexSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Мужской") ) {
                    temp_user_sex = UserSex.MEN;
                }
                if (parent.getItemAtPosition(position).equals("Женский") ) {
                    temp_user_sex = UserSex.WOMEN;
                }
            }
        });
        tellField = findViewById(R.id.et_high);
    }
    private boolean validateName(){
        String nameInput = tilName.getEditText().getText().toString().trim();

        if(nameInput.isEmpty()){
            tilName.setError("Поле не может быть пустым");
            return false;
        }else if(nameInput.length() > 25){
            tilName.setError("Превышена длина имени");
            return false;
        }else{
            tilName.setError(null);
            return true;
        }
    }

    private boolean validateGender(){
        String genderInput = tilGender.getEditText().getText().toString().trim();

        if(genderInput.isEmpty()){
            tilGender.setError("Поле не может быть пустым");
            return false;
        }else{
            tilGender.setError(null);
            return true;
        }
    }

    private boolean validateHigh(){
        String highInput = tilHigh.getEditText().getText().toString().trim();

        if(highInput.isEmpty()){
            tilHigh.setError("Поле не может быть пустым");
            return false;
        }else{
            tilHigh.setError(null);
            return true;
        }
    }

    private boolean validateDate(){
        String dateInput = til_data_birth.getEditText().getText().toString().trim();

        if(dateInput.isEmpty()){
            til_data_birth.setError("Поле не может быть пустым");
            return false;
        }else{
            til_data_birth.setError(null);
            return true;
        }
    }

    private boolean validateLifeStyle(){
        String LifeStyleInput = tilLifeStyle.getEditText().getText().toString().trim();

        if(LifeStyleInput.isEmpty()){
            tilLifeStyle.setError("Поле не может быть пустым");
            return false;
        }else{
            tilLifeStyle.setError(null);
            return true;
        }
    }

    private boolean validatePurpose(){
        String purposeInput = tilPurpose.getEditText().getText().toString().trim();

        if(purposeInput.isEmpty()){
            tilPurpose.setError("Поле не может быть пустым");
            return false;
        }else{
            tilPurpose.setError(null);
            return true;
        }
    }

    private boolean validateStartWeight(){
        String startWeightInput = tilStartWeight.getEditText().getText().toString().trim();

        if(startWeightInput.isEmpty()){
            tilStartWeight.setError("Поле не может быть пустым");
            return false;
        }else{
            tilStartWeight.setError(null);
            return true;
        }
    }

    private boolean validateFinishWeight(){
        String finishWeightInput = tilEndWeight.getEditText().getText().toString().trim();

        if(finishWeightInput.isEmpty()){
            tilEndWeight.setError("Поле не может быть пустым");
            return false;
        }else{
            tilEndWeight.setError(null);
            return true;
        }
    }

}