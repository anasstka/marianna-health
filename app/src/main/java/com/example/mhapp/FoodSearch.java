package com.example.mhapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class FoodSearch extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase firebaseDB;
    DatabaseReference users;
    DatabaseReference userData;
    DatabaseReference userRation;

    String USER_ID = "";

    ListView productList;
    DBHelperProducts dbHelperProducts;
    //    UserDBHelper helper;
    SQLiteDatabase db;
    Cursor productCursor;
    SimpleCursorAdapter productAdapter;
    EditText productFilter;

    private ImageView backToRationButton;
    private UserProfileData userProfileData;
    private UserInfoData userInfoData;
    private String rationTime;
    private String currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_search);

        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            userProfileData = (UserProfileData) arguments.getSerializable("user_data");
            rationTime = arguments.getString("ration_time");
            currentDate = arguments.getString("current_date");
        }

        USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        auth = FirebaseAuth.getInstance();
        firebaseDB = FirebaseDatabase.getInstance();
        users = firebaseDB.getReference(FirebaseConst.USERS_DB);
        userData = firebaseDB.getReference(FirebaseConst.USER_DATA_DB).child(USER_ID).child(currentDate);
        userRation = firebaseDB.getReference(FirebaseConst.USER_RATION_DB).child(USER_ID).child(currentDate).child(rationTime);

        productList = findViewById(R.id.productList);
        productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // получение данных о продукте
                final String productName = ((SQLiteCursor)productAdapter.getItem(position)).getString(1);
                final float productCalories = ((SQLiteCursor)productAdapter.getItem(position)).getFloat(2);
                final float productProteins = ((SQLiteCursor)productAdapter.getItem(position)).getFloat(3);
                final float productFats = ((SQLiteCursor)productAdapter.getItem(position)).getFloat(4);
                final float productCarbohydrates = ((SQLiteCursor)productAdapter.getItem(position)).getFloat(5);

                // выгрузка старых данных из БД и сложение с данными нового продукта
                {
                    userData.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            userInfoData = snapshot.getValue(UserInfoData.class);
                            assert userInfoData != null;

                            DatabaseReference productReference = userRation.push();
                            RationAdapter.Product product = new RationAdapter.Product(productReference.getKey(), productName, productCalories, productProteins, productFats, productCarbohydrates);

                            userInfoData.setProteins(productProteins + userInfoData.getProteins());
                            userInfoData.setFats(productFats + userInfoData.getFats());
                            userInfoData.setCarbohydrates(productCarbohydrates + userInfoData.getCarbohydrates());
                            switch (rationTime) {
                                case FirebaseConst.BREAKFAST:
                                    userInfoData.setBreakfastCalories(productCalories + userInfoData.getBreakfastCalories());
                                    break;
                                case FirebaseConst.LUNCH:
                                    userInfoData.setLunchCalories(productCalories + userInfoData.getLunchCalories());
                                    break;
                                case FirebaseConst.DINNER:
                                    userInfoData.setDinnerCalories(productCalories + userInfoData.getDinnerCalories());
                                    break;
                                case FirebaseConst.SNACK:
                                    userInfoData.setSnackCalories(productCalories + userInfoData.getSnackCalories());
                                    break;
                            }

                            userData.setValue(userInfoData);
                            userRation.child(productReference.getKey()).setValue(product);

                            Intent intent = new Intent(FoodSearch.this, FoodRation.class);
                            intent.putExtra("user_data", userProfileData);
                            intent.putExtra("current_date", currentDate);
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

//                    Cursor c = db.rawQuery("SELECT * FROM UsersData", null);
//                    c.moveToFirst();
//                    while (!c.isAfterLast()) {
//                        if (c.getInt(c.getColumnIndex("user_id")) == userProfileData.getId()) {
//                            boolean isCorrectDate = false;
//                            int charCounter = 0;
//                            for (int i = 0; i < c.getString(c.getColumnIndex("date")).length(); i++) {
//                                if (currentDate.charAt(i) == c.getString(c.getColumnIndex("date")).charAt(i)) {
//                                    charCounter++;
//                                }
//                            }
//                            if (charCounter == c.getString(c.getColumnIndex("date")).length()) {
//                                isCorrectDate = true;
//                            }
//
//                            if (isCorrectDate) {
//                                productProteins += c.getFloat(c.getColumnIndex("proteins"));
//                                productFats += c.getFloat(c.getColumnIndex("fats"));
//                                productCarbohydrates += c.getFloat(c.getColumnIndex("carbohydrates"));
//                                if (rationTime.equals("breakfast")) {
//                                    productCalories += c.getFloat(c.getColumnIndex("breakfast_calories"));
//                                } else if (rationTime.equals("lunch")) {
//                                    productCalories += c.getFloat(c.getColumnIndex("lunch_calories"));
//                                } else if (rationTime.equals("dinner")) {
//                                    productCalories += c.getFloat(c.getColumnIndex("dinner_calories"));
//                                } else if (rationTime.equals("snack")) {
//                                    productCalories += c.getFloat(c.getColumnIndex("snack_calories"));
//                                }
//                                break;
//                            }
//                        }
//                        c.moveToNext();
//                    }
//                    c.close();
//                }
                // загрузка обновлённых данных в БД
//                {
//                    ContentValues cv = new ContentValues();
//                    cv = new ContentValues();
//                    if (rationTime.equals("breakfast")) {
//                        cv.put("breakfast_calories", productCalories);
//                    } else if (rationTime.equals("lunch")) {
//                        cv.put("lunch_calories", productCalories);
//                    } else if (rationTime.equals("dinner")) {
//                        cv.put("dinner_calories", productCalories);
//                    } else if (rationTime.equals("snack")) {
//                        cv.put("snack_calories", productCalories);
//                    }
//                    cv.put("proteins", productProteins);
//                    cv.put("fats", productFats);
//                    cv.put("carbohydrates", productCarbohydrates);
//                    db.update("UsersData", cv, "user_id=" + String.valueOf(userProfileData.getId()) + " and date=\'" + currentDate + "\'", null);
//                    db.close();
//                }

//                Intent intent = new Intent(FoodSearch.this, FoodRation.class);
//                intent.putExtra("user_data", userProfileData);
////                intent.putExtra("ration_time", rationTime);
////                intent.putExtra("product_name", productName);
////                intent.putExtra("product_calories", productCalories);
////                intent.putExtra("product_proteins", productProteins);
////                intent.putExtra("product_fats", productFats);
////                intent.putExtra("product_carbohydrates", productCarbohydrates);
//                intent.putExtra("current_date", currentDate);
//                startActivity(intent);
            }
        });
        productFilter = findViewById(R.id.productsFilter);

        dbHelperProducts = new DBHelperProducts(getApplicationContext());
        // создаем базу данных
        dbHelperProducts.create_db();

        backToRationButton = findViewById(R.id.btn_back_to_ration);
        backToRationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodSearch.this, FoodRation.class);
                intent.putExtra("user_data", userProfileData);
                intent.putExtra("current_date", currentDate);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            db = dbHelperProducts.open();
            productCursor = db.rawQuery("select * from " + DBHelperProducts.TABLE + " limit 30", null);
            String[] headers = new String[]{DBHelperProducts.COLUMN_NAME, DBHelperProducts.COLUMN_KCAL,
                    DBHelperProducts.COLUMN_PROTEIN, DBHelperProducts.COLUMN_FATS,DBHelperProducts.COLUMN_CARBS};
            productAdapter = new SimpleCursorAdapter(this, R.layout.product_info,
                    productCursor, headers, new int[]{R.id.name, R.id.callories,R.id.belki,R.id.fats,R.id.uglev}, 0);

            // если в текстовом поле есть текст, выполняем фильтрацию
            // данная проверка нужна при переходе от одной ориентации экрана к другой
            if(!productFilter.getText().toString().isEmpty())
                productAdapter.getFilter().filter(productFilter.getText().toString());

            // установка слушателя изменения текста
            productFilter.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) { }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                // при изменении текста выполняем фильтрацию
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    productAdapter.getFilter().filter(s.toString());
                }
            });

            // устанавливаем провайдер фильтрации
            productAdapter.setFilterQueryProvider(new FilterQueryProvider() {
                @Override
                public Cursor runQuery(CharSequence constraint) {
                    if (constraint == null || constraint.length() == 0 || constraint.toString().trim().isEmpty() || constraint.toString().length() < 3) {
//                        return db.rawQuery("select * from " + DBHelperProducts.TABLE, null);
                        return db.rawQuery("select * from " + DBHelperProducts.TABLE + " limit 30", null);
                    }
                    else {
                        return db.rawQuery("select * from " + DBHelperProducts.TABLE + " where " +
                                DBHelperProducts.COLUMN_NAME + " like ?", new String[]{"%" + constraint.toString() + "%"});
                    }
                }
            });

            productList.setAdapter(productAdapter);
        }
        catch (SQLException ex){}
    }

    public void onDestroy(){
        super.onDestroy();
        // Закрываем подключение и курсор
        db.close();
        productCursor.close();
    }
}