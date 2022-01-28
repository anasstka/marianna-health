package com.example.mhapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FoodRation extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase firebaseDB;
    DatabaseReference users;
    DatabaseReference userData;
    DatabaseReference userRation;

    String USER_ID = "";

//    private UserDBHelper helper;
//    private SQLiteDatabase db;
//    private Cursor cursor;

    private RationAdapter.RationGroup breakfast;
    private RationAdapter breakfastAdapter;
    private ExpandableListView elvBreakfast;

    private RationAdapter.RationGroup lunch;
    private RationAdapter lunchAdapter;
    private ExpandableListView elvLunch;

    private RationAdapter.RationGroup dinner;
    private RationAdapter dinnerAdapter;
    private ExpandableListView elvDinner;

    private RationAdapter.RationGroup snack;
    private RationAdapter snackAdapter;
    private ExpandableListView elvSnack;

    private LinearLayout buttonBack;

    private UserProfileData userProfileData;
    private UserRationData userRationData;

    private String currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_ration);

        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            userProfileData = (UserProfileData) arguments.getSerializable("user_data");
            currentDate = arguments.getString("current_date");
        }

        USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        auth = FirebaseAuth.getInstance();
        firebaseDB = FirebaseDatabase.getInstance();
        users = firebaseDB.getReference(FirebaseConst.USERS_DB);
        userData = firebaseDB.getReference(FirebaseConst.USER_DATA_DB).child(USER_ID);
        userRation = firebaseDB.getReference(FirebaseConst.USER_RATION_DB).child(USER_ID).child(currentDate);

        buttonBack = (LinearLayout) findViewById(R.id.btn_back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodRation.this, MainActivity.class);
                intent.putExtra("user_data", userProfileData);
                startActivity(intent);
            }
        });

        breakfast = new RationAdapter.RationGroup("Завтрак");
        lunch = new RationAdapter.RationGroup("Обед");
        dinner = new RationAdapter.RationGroup("Ужин");
        snack = new RationAdapter.RationGroup("Перекус");

        userRation.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dsCategories : snapshot.getChildren()) {
                    System.out.println(dsCategories.getChildrenCount() + " !!!!!!!!!!");
                    for (DataSnapshot dsFoods : dsCategories.getChildren()) {
                        RationAdapter.Product product = dsFoods.getValue(RationAdapter.Product.class);
                        switch (dsCategories.getKey()) {
                            case FirebaseConst.BREAKFAST:
                                breakfast.getProductsList().add(product);
                                break;
                            case FirebaseConst.LUNCH:
                                lunch.getProductsList().add(product);
                                break;
                            case FirebaseConst.DINNER:
                                dinner.getProductsList().add(product);
                                break;
                            case FirebaseConst.SNACK:
                                snack.getProductsList().add(product);
                                break;
                        }
                    }
                }

                updateUI();
            }

            private void updateUI() {
                elvBreakfast = (ExpandableListView) findViewById(R.id.elv_breakfast);
                breakfastAdapter = new RationAdapter(FoodRation.this, breakfast, userProfileData, currentDate);
                elvBreakfast.setAdapter(breakfastAdapter);
                View breakfastFooter = LayoutInflater.from(FoodRation.this).inflate(R.layout.food_ration_item_2, null, false);
                elvBreakfast.addFooterView(breakfastFooter);
                LinearLayout addBreakfastProduct = (LinearLayout) breakfastFooter.findViewById(R.id.btn_add_product);
                addBreakfastProduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(FoodRation.this, FoodSearch.class);
                        intent.putExtra("user_data", userProfileData);
                        intent.putExtra("ration_time", FirebaseConst.BREAKFAST);
                        intent.putExtra("current_date", currentDate);
                        startActivity(intent);
                    }
                });

                elvLunch = (ExpandableListView) findViewById(R.id.elv_lunch);
                lunchAdapter = new RationAdapter(FoodRation.this, lunch, userProfileData, currentDate);
                elvLunch.setAdapter(lunchAdapter);
                View lunchFooter = LayoutInflater.from(FoodRation.this).inflate(R.layout.food_ration_item_2, null, false);
                elvLunch.addFooterView(lunchFooter);
                LinearLayout addLunchProduct = (LinearLayout) lunchFooter.findViewById(R.id.btn_add_product);
                addLunchProduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(FoodRation.this, FoodSearch.class);
                        intent.putExtra("user_data", userProfileData);
                        intent.putExtra("ration_time", FirebaseConst.LUNCH);
                        intent.putExtra("current_date", currentDate);
                        startActivity(intent);
                    }
                });

                elvDinner = (ExpandableListView) findViewById(R.id.elv_dinner);
                dinnerAdapter = new RationAdapter(FoodRation.this, dinner, userProfileData, currentDate);
                elvDinner.setAdapter(dinnerAdapter);
                View dinnerFooter = LayoutInflater.from(FoodRation.this).inflate(R.layout.food_ration_item_2, null, false);
                elvDinner.addFooterView(dinnerFooter);
                LinearLayout addDinnerProduct = (LinearLayout) dinnerFooter.findViewById(R.id.btn_add_product);
                addDinnerProduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(FoodRation.this, FoodSearch.class);
                        intent.putExtra("user_data", userProfileData);
                        intent.putExtra("ration_time", FirebaseConst.DINNER);
                        intent.putExtra("current_date", currentDate);
                        startActivity(intent);
                    }
                });

                elvSnack = (ExpandableListView) findViewById(R.id.elv_snack);
                snackAdapter = new RationAdapter(FoodRation.this, snack, userProfileData, currentDate);
                elvSnack.setAdapter(snackAdapter);
                View snackFooter = LayoutInflater.from(FoodRation.this).inflate(R.layout.food_ration_item_2, null, false);
                elvSnack.addFooterView(snackFooter);
                LinearLayout addSnackProduct = (LinearLayout) snackFooter.findViewById(R.id.btn_add_product);
                addSnackProduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(FoodRation.this, FoodSearch.class);
                        intent.putExtra("user_data", userProfileData);
                        intent.putExtra("ration_time", FirebaseConst.SNACK);
                        intent.putExtra("current_date", currentDate);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        cursor = db.rawQuery("SELECT * FROM UserRation", null);
//        cursor.moveToFirst();
//        while(!cursor.isAfterLast()) {
//            if (cursor.getInt(cursor.getColumnIndex("user_id")) == userData.getId()) {
//                if (cursor.getString(cursor.getColumnIndex("date")).equals(currentDate)) {
//                    if (cursor.getInt(cursor.getColumnIndex("ration_time")) == 0) {
//                        RationAdapter.Product product = new RationAdapter.Product(cursor.getString(cursor.getColumnIndex("product_name")));
//                        product.setCalories(cursor.getFloat(cursor.getColumnIndex("calories")));
//                        product.setProteins(cursor.getFloat(cursor.getColumnIndex("proteins")));
//                        product.setFats(cursor.getFloat(cursor.getColumnIndex("fats")));
//                        product.setCarbohydrates(cursor.getFloat(cursor.getColumnIndex("carbohydrates")));
//                        breakfast.getProductsList().add(product);
//                    }
//                }
//            }
//            cursor.moveToNext();
//        }
//        cursor.close();


//        cursor = db.rawQuery("SELECT * FROM UserRation", null);
//        cursor.moveToFirst();
//        while(!cursor.isAfterLast()) {
//            if (cursor.getInt(cursor.getColumnIndex("user_id")) == userData.getId()) {
//                if (cursor.getString(cursor.getColumnIndex("date")).equals(currentDate)) {
//                    if (cursor.getInt(cursor.getColumnIndex("ration_time")) == 1) {
//                        RationAdapter.Product product = new RationAdapter.Product(cursor.getString(cursor.getColumnIndex("product_name")));
//                        product.setCalories(cursor.getFloat(cursor.getColumnIndex("calories")));
//                        product.setProteins(cursor.getFloat(cursor.getColumnIndex("proteins")));
//                        product.setFats(cursor.getFloat(cursor.getColumnIndex("fats")));
//                        product.setCarbohydrates(cursor.getFloat(cursor.getColumnIndex("carbohydrates")));
//                        lunch.getProductsList().add(product);
//                    }
//                }
//            }
//            cursor.moveToNext();
//        }
//        cursor.close();


//        cursor = db.rawQuery("SELECT * FROM UserRation", null);
//        cursor.moveToFirst();
//        while(!cursor.isAfterLast()) {
//            if (cursor.getInt(cursor.getColumnIndex("user_id")) == userData.getId()) {
//                if (cursor.getString(cursor.getColumnIndex("date")).equals(currentDate)) {
//                    if (cursor.getInt(cursor.getColumnIndex("ration_time")) == 2) {
//                        RationAdapter.Product product = new RationAdapter.Product(cursor.getString(cursor.getColumnIndex("product_name")));
//                        product.setCalories(cursor.getFloat(cursor.getColumnIndex("calories")));
//                        product.setProteins(cursor.getFloat(cursor.getColumnIndex("proteins")));
//                        product.setFats(cursor.getFloat(cursor.getColumnIndex("fats")));
//                        product.setCarbohydrates(cursor.getFloat(cursor.getColumnIndex("carbohydrates")));
//                        dinner.getProductsList().add(product);
//                    }
//                }
//            }
//            cursor.moveToNext();
//        }
//        cursor.close();

//        cursor = db.rawQuery("SELECT * FROM UserRation", null);
//        cursor.moveToFirst();
//        while(!cursor.isAfterLast()) {
//            if (cursor.getInt(cursor.getColumnIndex("user_id")) == userData.getId()) {
//                if (cursor.getString(cursor.getColumnIndex("date")).equals(currentDate)) {
//                    if (cursor.getInt(cursor.getColumnIndex("ration_time")) == 3) {
//                        RationAdapter.Product product = new RationAdapter.Product(cursor.getString(cursor.getColumnIndex("product_name")));
//                        product.setCalories(cursor.getFloat(cursor.getColumnIndex("calories")));
//                        product.setProteins(cursor.getFloat(cursor.getColumnIndex("proteins")));
//                        product.setFats(cursor.getFloat(cursor.getColumnIndex("fats")));
//                        product.setCarbohydrates(cursor.getFloat(cursor.getColumnIndex("carbohydrates")));
//                        snack.getProductsList().add(product);
//                    }
//                }
//            }
//            cursor.moveToNext();
//        }
//        cursor.close();


//        db.close();

//        try {
//            db = helper.getWritableDatabase();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (arguments != null) {
//            if (arguments.getString("ration_time") != null) {
//                if (arguments.getString("ration_time").equals("breakfast")) {
//                    RationAdapter.Product product = new RationAdapter.Product(arguments.getString("product_name"));
//                    product.setCalories(arguments.getFloat("product_calories"));
//                    product.setProteins(arguments.getFloat("product_proteins"));
//                    product.setFats(arguments.getFloat("product_fats"));
//                    product.setCarbohydrates(arguments.getFloat("product_carbohydrates"));
//                    // добавление в БД
//                    {
//                        ContentValues cv = new ContentValues();
//                        cv.put("user_id", userData.getId());
//                        cv.put("date", currentDate);
//                        cv.put("ration_time", 0);
//                        cv.put("product_name", product.getName());
//                        cv.put("calories", product.getCalories());
//                        cv.put("proteins", product.getProteins());
//                        cv.put("fats", product.getFats());
//                        cv.put("carbohydrates", product.getCarbohydrates());
//                        db.insert("UserRation", null, cv);
//                    }
//                    breakfast.getProductsList().add(product);
//                    breakfastAdapter.notifyDataSetChanged();
//                } else if (arguments.getString("ration_time").equals("lunch")) {
//                    RationAdapter.Product product = new RationAdapter.Product(arguments.getString("product_name"));
//                    product.setCalories(arguments.getFloat("product_calories"));
//                    product.setProteins(arguments.getFloat("product_proteins"));
//                    product.setFats(arguments.getFloat("product_fats"));
//                    product.setCarbohydrates(arguments.getFloat("product_carbohydrates"));
//                    // добавление в БД
//                    {
//                        ContentValues cv = new ContentValues();
//                        cv.put("user_id", userData.getId());
//                        cv.put("date", currentDate);
//                        cv.put("ration_time", 1);
//                        cv.put("product_name", product.getName());
//                        cv.put("calories", product.getCalories());
//                        cv.put("proteins", product.getProteins());
//                        cv.put("fats", product.getFats());
//                        cv.put("carbohydrates", product.getCarbohydrates());
//                        db.insert("UserRation", null, cv);
//                    }
//                    lunch.getProductsList().add(product);
//                    lunchAdapter.notifyDataSetChanged();
//                } else if (arguments.getString("ration_time").equals("dinner")) {
//                    RationAdapter.Product product = new RationAdapter.Product(arguments.getString("product_name"));
//                    product.setCalories(arguments.getFloat("product_calories"));
//                    product.setProteins(arguments.getFloat("product_proteins"));
//                    product.setFats(arguments.getFloat("product_fats"));
//                    product.setCarbohydrates(arguments.getFloat("product_carbohydrates"));
//                    // добавление в БД
//                    {
//                        ContentValues cv = new ContentValues();
//                        cv.put("user_id", userData.getId());
//                        cv.put("date", currentDate);
//                        cv.put("ration_time", 2);
//                        cv.put("product_name", product.getName());
//                        cv.put("calories", product.getCalories());
//                        cv.put("proteins", product.getProteins());
//                        cv.put("fats", product.getFats());
//                        cv.put("carbohydrates", product.getCarbohydrates());
//                        db.insert("UserRation", null, cv);
//                    }
//                    dinner.getProductsList().add(product);
//                    dinnerAdapter.notifyDataSetChanged();
//                } else if (arguments.getString("ration_time").equals("snack")) {
//                    RationAdapter.Product product = new RationAdapter.Product(arguments.getString("product_name"));
//                    product.setCalories(arguments.getFloat("product_calories"));
//                    product.setProteins(arguments.getFloat("product_proteins"));
//                    product.setFats(arguments.getFloat("product_fats"));
//                    product.setCarbohydrates(arguments.getFloat("product_carbohydrates"));
//                    // добавление в БД
//                    {
//                        ContentValues cv = new ContentValues();
//                        cv.put("user_id", userData.getId());
//                        cv.put("date", currentDate);
//                        cv.put("ration_time", 3);
//                        cv.put("product_name", product.getName());
//                        cv.put("calories", product.getCalories());
//                        cv.put("proteins", product.getProteins());
//                        cv.put("fats", product.getFats());
//                        cv.put("carbohydrates", product.getCarbohydrates());
//                        db.insert("UserRation", null, cv);
//                    }
//                    snack.getProductsList().add(product);
//                    snackAdapter.notifyDataSetChanged();
//                }
//            }
//        }
//        db.close();
    }
}