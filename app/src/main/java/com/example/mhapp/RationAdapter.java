package com.example.mhapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RationAdapter extends BaseExpandableListAdapter {

    public static class Product {
        private String id;
        private String name;
        private float calories;
        private float proteins;
        private float fats;
        private float carbohydrates;

        public Product() {
        }

        public Product(String name) {
            this.id = "";
            this.name = name;
            this.calories = 0;
            this.proteins = 0;
            this.fats = 0;
            this.carbohydrates = 0;
        }

        public Product(String id, String name, float calories, float proteins, float fats, float carbohydrates) {
            this.id = id;
            this.name = name;
            this.calories = calories;
            this.proteins = proteins;
            this.fats = fats;
            this.carbohydrates = carbohydrates;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public float getCalories() {
            return calories;
        }

        public void setCalories(float calories) {
            this.calories = calories;
        }

        public float getProteins() {
            return proteins;
        }

        public void setProteins(float proteins) {
            this.proteins = proteins;
        }

        public float getFats() {
            return fats;
        }

        public void setFats(float fats) {
            this.fats = fats;
        }

        public float getCarbohydrates() {
            return carbohydrates;
        }

        public void setCarbohydrates(float carbohydrates) {
            this.carbohydrates = carbohydrates;
        }
    }

    public static class RationGroup {
        private String name;
        private List<Product> products;

        public RationGroup(String name) {
            this.name = name;
            products = new ArrayList<>();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Product> getProductsList() {
            return products;
        }
    }

    private Context context;
    private RationGroup group;
    private UserProfileData userProfileData;
    private String currentDate;

    FirebaseAuth auth;
    FirebaseDatabase firebaseDB;
    DatabaseReference users;
    DatabaseReference userData;
    DatabaseReference userRation;

    String USER_ID = "";

    public RationAdapter(Context context, RationGroup group, UserProfileData userProfileData, String currentDate) {
        this.context = context;
        this.group = group;
        this.userProfileData = userProfileData;
        this.currentDate = currentDate;

        USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        auth = FirebaseAuth.getInstance();
        firebaseDB = FirebaseDatabase.getInstance();
        users = firebaseDB.getReference(FirebaseConst.USERS_DB);
        userData = firebaseDB.getReference(FirebaseConst.USER_DATA_DB).child(USER_ID).child(currentDate);
    }

    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return group.getProductsList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return group;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return group.getProductsList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.food_ration_header, null, false);
        TextView header = (TextView) view.findViewById(R.id.elv_header);
        header.setText(group.getName());
        return view;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.food_ration_item_1, null, false);
        TextView productName = (TextView) view.findViewById(R.id.product_name);
        productName.setText(group.getProductsList().get(childPosition).getName());
        ImageView deleteButton = (ImageView) view.findViewById(R.id.btn_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialogDeleteProduct = new Dialog(context);
                dialogDeleteProduct.setContentView(R.layout.dialog_delete_product);
                dialogDeleteProduct.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView btnYes = dialogDeleteProduct.findViewById(R.id.dialog_btn_yes);
                TextView btnNo = dialogDeleteProduct.findViewById(R.id.dialog_btn_no);

//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setMessage("Уверены,что хотите удалить продукт?");
//                builder.setCancelable(true);
//                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String productKey = group.getProductsList().get(childPosition).getId();
                        final float productCalories = group.getProductsList().get(childPosition).getCalories();
                        final float productProteins = group.getProductsList().get(childPosition).getProteins();
                        final float productFats = group.getProductsList().get(childPosition).getFats();
                        final float productCarbohydrates = group.getProductsList().get(childPosition).getCarbohydrates();

                        String rationTime = "";
                        switch (group.name) {
                            case "Завтрак":
                                rationTime = FirebaseConst.BREAKFAST;
                                break;
                            case "Обед":
                                rationTime = FirebaseConst.LUNCH;
                                break;
                            case "Ужин":
                                rationTime = FirebaseConst.DINNER;
                                break;
                            case "Перекус":
                                rationTime = FirebaseConst.SNACK;
                                break;
                        }

                        userRation = firebaseDB.getReference(FirebaseConst.USER_RATION_DB).child(USER_ID).child(currentDate).child(rationTime);

                        // выгрузка старых данных из БД и сложение с данными нового продукта
                        {
                            String finalRationTime = rationTime;
                            userData.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    UserInfoData userInfoData = snapshot.getValue(UserInfoData.class);
                                    assert userInfoData != null;

                                    userInfoData.setProteins(userInfoData.getProteins() - productProteins);
                                    userInfoData.setFats(userInfoData.getFats() - productFats);
                                    userInfoData.setCarbohydrates(userInfoData.getCarbohydrates() - productCarbohydrates);
                                    switch (finalRationTime) {
                                        case FirebaseConst.BREAKFAST:
                                            userInfoData.setBreakfastCalories(userInfoData.getBreakfastCalories() - productCalories);
                                            break;
                                        case FirebaseConst.LUNCH:
                                            userInfoData.setLunchCalories(userInfoData.getLunchCalories() - productCalories);
                                            break;
                                        case FirebaseConst.DINNER:
                                            userInfoData.setDinnerCalories(userInfoData.getDinnerCalories() - productCalories);
                                            break;
                                        case FirebaseConst.SNACK:
                                            userInfoData.setSnackCalories(userInfoData.getSnackCalories() - productCalories);
                                            break;
                                    }

                                    userData.setValue(userInfoData);
                                    userRation.child(productKey).setValue(null);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
//                        // работа с БД
//                        {
//                            // подключение к БД
//                            UserDBHelper helper = new UserDBHelper(context);
//                            try {
//                                helper.updateDataBase();
//                            } catch (IOException mIOException) {
//                                throw new Error("UnableToUpdateDatabase");
//                            }
//                            SQLiteDatabase db = null;
//                            try {
//                                db = helper.getWritableDatabase();
//                            } catch (SQLException mSQLException) {
//                                throw mSQLException;
//                            }
//
//                            float proteins = 0;
//                            float fats = 0;
//                            float carbohydrates = 0;
//                            float calories = 0;
//
//                            // выгрузка старых данных из БД
//                            {
//                                Cursor c = db.rawQuery("SELECT * FROM UsersData", null);
//                                c.moveToFirst();
//                                while (!c.isAfterLast()) {
//                                    if (c.getInt(c.getColumnIndex("user_id")) == 0) {
//                                        boolean isCorrectDate = false;
//                                        int charCounter = 0;
//                                        for (int i = 0; i < c.getString(c.getColumnIndex("date")).length(); i++) {
//                                            if (currentDate.charAt(i) == c.getString(c.getColumnIndex("date")).charAt(i)) {
//                                                charCounter++;
//                                            }
//                                        }
//                                        if (charCounter == c.getString(c.getColumnIndex("date")).length()) {
//                                            isCorrectDate = true;
//                                        }
//
//                                        if (isCorrectDate) {
//                                            proteins = c.getFloat(c.getColumnIndex("proteins")) - group.getProductsList().get(childPosition).getProteins();
//                                            fats = c.getFloat(c.getColumnIndex("fats")) - group.getProductsList().get(childPosition).getFats();
//                                            carbohydrates = c.getFloat(c.getColumnIndex("carbohydrates")) - group.getProductsList().get(childPosition).getCarbohydrates();
//                                            if (group.name.equals("Завтрак")) {
//                                                calories = c.getFloat(c.getColumnIndex("breakfast_calories")) - group.getProductsList().get(childPosition).getCalories();
//                                            } else if (group.name.equals("Обед")) {
//                                                calories = c.getFloat(c.getColumnIndex("lunch_calories")) - group.getProductsList().get(childPosition).getCalories();
//                                            } else if (group.name.equals("Ужин")) {
//                                                calories = c.getFloat(c.getColumnIndex("dinner_calories")) - group.getProductsList().get(childPosition).getCalories();
//                                            } else if (group.name.equals("Перекус")) {
//                                                calories = c.getFloat(c.getColumnIndex("snack_calories")) - group.getProductsList().get(childPosition).getCalories();
//                                            }
//                                            break;
//                                        }
//                                    }
//                                    c.moveToNext();
//                                }
//                                c.close();
//                            }
//                            // загрузка обновлённых данных в БД
//                            {
//                                ContentValues cv = new ContentValues();
//                                cv = new ContentValues();
//                                if (group.name.equals("Завтрак")) {
//                                    if (calories < 0)
//                                        cv.put("breakfast_calories", 0);
//                                    else
//                                        cv.put("breakfast_calories", calories);
//                                } else if (group.name.equals("Обед")) {
//                                    if (calories < 0)
//                                        cv.put("lunch_calories", 0);
//                                    else
//                                        cv.put("lunch_calories", calories);
//                                } else if (group.name.equals("Ужин")) {
//                                    if (calories < 0)
//                                        cv.put("dinner_calories", 0);
//                                    else
//                                        cv.put("dinner_calories", calories);
//                                } else if (group.name.equals("Перекус")) {
//                                    if (calories < 0)
//                                        cv.put("snack_calories", 0);
//                                    else
//                                        cv.put("snack_calories", calories);
//                                }
//                                if (proteins < 0) {
//                                    cv.put("proteins", 0);
//                                } else {
//                                    cv.put("proteins", proteins);
//                                }
//                                if (fats < 0) {
//                                    cv.put("fats", 0);
//                                } else {
//                                    cv.put("fats", fats);
//                                }
//                                if (carbohydrates < 0) {
//                                    cv.put("carbohydrates", 0);
//                                } else {
//                                    cv.put("carbohydrates", carbohydrates);
//                                }
//                                db.update("UsersData", cv, "user_id=" + String.valueOf(0) + " and date=\'" + currentDate + "\'", null);
//                            }
//
//                            // удаление продукта из рациона питания
//                            {
//                                if (group.name.equals("Завтрак")) {
//                                    db.delete("UserRation", "user_id=" + String.valueOf(0) + " " +
//                                            "AND date=\'" + currentDate + "\' " + "AND ration_time=0 " +
//                                            "AND product_name=\'" + group.getProductsList().get(childPosition).getName() + "\'", null);
//                                } else if (group.name.equals("Обед")) {
//                                    db.delete("UserRation", "user_id=" + String.valueOf(0) + " " +
//                                            "AND date=\'" + currentDate + "\' " + "AND ration_time=1 " +
//                                            "AND product_name=\'" + group.getProductsList().get(childPosition).getName() + "\'", null);
//                                } else if (group.name.equals("Ужин")) {
//                                    db.delete("UserRation", "user_id=" + String.valueOf(0) + " " +
//                                            "AND date=\'" + currentDate + "\' " + "AND ration_time=2 " +
//                                            "AND product_name=\'" + group.getProductsList().get(childPosition).getName() + "\'", null);
//                                } else if (group.name.equals("Перекус")) {
//                                    db.delete("UserRation", "user_id=" + String.valueOf(0) + " " +
//                                            "AND date=\'" + currentDate + "\' " + "AND ration_time=3 " +
//                                            "AND product_name=\'" + group.getProductsList().get(childPosition).getName() + "\'", null);
//                                }
//                            }
//                            db.close();
//                        }


                        group.getProductsList().remove(childPosition);
                        notifyDataSetChanged();

                        dialogDeleteProduct.dismiss();
                    }
                });

                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogDeleteProduct.dismiss();
                    }
                });
//                builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.cancel();
//                    }
//                });
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();
                dialogDeleteProduct.show();
            }
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
