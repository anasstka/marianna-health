package com.example.mhapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * Фрагмент главного экрана h
 */
public class HomeFragment extends Fragment {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String APP_PREFERENCES = "settings";
    public static final String APP_PREFERENCES_DATE = "date";

    FirebaseAuth auth;
    FirebaseDatabase firebaseDB;
    DatabaseReference users;
    DatabaseReference userData;
    DatabaseReference userRation;

    String USER_ID = "";

    //    UserDBHelper helper;
//    SQLiteDatabase db;
    UserProfileData userProfileData;
    UserInfoData userInfoData;

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    // элементы главного экраана
    private TextView tv_date;
    private TextView tv_dailyKcalInChart;
    private TextView tv_remainingKcal;
    private TextView tv_recommendedKcal;
    private TextView tv_proteins;
    private TextView tv_fats;
    private TextView tv_carbohydrates;
    private TextView tv_water;

    private ImageView btnSwitchDayAhead;
    private ImageView btnSwitchDayBack;

    private ImageView btnAddWater;
    private ImageView btnDeleteWater;

    // переменные
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
    private static final DateFormat DATE_FORMAT_FOR_FIREBASE = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

    private Date currentDate;
    private Date dateSelectedByUser;

    private float dailyKcal = 0.0f;
    private float recommendedKcal = 0.0f;

    private float kcalForBreakfast = 0.0f;
    private float kcalForLunch = 0.0f;
    private float kcalForDinner = 0.0f;
    private float kcalForSnack = 0.0f;

    private float proteins = 0.0f;
    private float fats = 0.0f;
    private float carbohydrates = 0.0f;
    private float waterLiters = 0.0f;

    // первая диграмма с суточными калориями
    private PieChartView dailyCalorieChart;
    private PieChartData dataDailyCalorieChart;

    // вторая диаграмма с калориями по рационам
    private PieChartView calorieChartByDiet;
    private PieChartData dataCalorieChartByDiet;

    private LinearLayout kcalByDiet;

    public HomeFragment() {

    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        sharedPreferences = this.getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        System.out.println(USER_ID);

        auth = FirebaseAuth.getInstance();
        firebaseDB = FirebaseDatabase.getInstance();
        users = firebaseDB.getReference(FirebaseConst.USERS_DB);
        userData = firebaseDB.getReference(FirebaseConst.USER_DATA_DB).child(USER_ID);
        userRation = firebaseDB.getReference(FirebaseConst.USER_RATION_DB).child(USER_ID);

        userRation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String db_date = DATE_FORMAT_FOR_FIREBASE.format(dateSelectedByUser);
                System.out.println(!snapshot.child(db_date).exists() + " !");
                if (!snapshot.child(db_date).exists()) {
                    userRation = userRation.child(db_date);
                    userRation.child(FirebaseConst.BREAKFAST).setValue("");
                    userRation.child(FirebaseConst.DINNER).setValue("");
                    userRation.child(FirebaseConst.LUNCH).setValue("");
                    userRation.child(FirebaseConst.SNACK).setValue("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        userProfileData = new UserProfileData();

//        DatabaseReference user = users.child(USER_ID);
//        user.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                userProfileData = snapshot.getValue(UserProfileData.class);
//                System.out.println(userProfileData.getDateOfBorn());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });

//        helper = new UserDBHelper(getContext());
        Bundle arguments = getActivity().getIntent().getExtras();
        if (arguments != null) {
            userProfileData = (UserProfileData) arguments.getSerializable("user_data");
        }

        kcalByDiet = view.findViewById(R.id.ckal_by_diet);

        tv_date = view.findViewById(R.id.tv_date);
        tv_dailyKcalInChart = view.findViewById(R.id.tv_daily_kcal_in_chart);
        tv_remainingKcal = view.findViewById(R.id.tv_remaining_kcal);
        tv_recommendedKcal = view.findViewById(R.id.tv_recommended_kcal);
        tv_proteins = view.findViewById(R.id.tv_proteins);
        tv_fats = view.findViewById(R.id.tv_fats);
        tv_carbohydrates = view.findViewById(R.id.tv_carbohydrates);
        tv_water = view.findViewById(R.id.tv_water);

        dailyCalorieChart = view.findViewById(R.id.daily_calorie_chart);
        calorieChartByDiet = view.findViewById(R.id.calorie_chart_by_diet);

        setCurrentDate();

        userInfoData = new UserInfoData(DATE_FORMAT.format(currentDate));

        loadingDataPerDay(currentDate);

        btnSwitchDayAhead = view.findViewById(R.id.btn_switch_day_ahead);
        btnSwitchDayBack = view.findViewById(R.id.btn_switch_day_back);
        View.OnClickListener switchBetweenDates = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateSelectedByUser);
                switch (v.getId()) {
                    case R.id.btn_switch_day_ahead:
                        calendar.add(Calendar.DATE, +1);
                        break;
                    case R.id.btn_switch_day_back:
                        calendar.add(Calendar.DATE, -1);
                        break;
                }
                dateSelectedByUser = calendar.getTime();
                setDate(dateSelectedByUser);
                loadingDataPerDay(dateSelectedByUser);
            }
        };
        btnSwitchDayAhead.setOnClickListener(switchBetweenDates);
        btnSwitchDayBack.setOnClickListener(switchBetweenDates);

        btnAddWater = view.findViewById(R.id.btn_add_water);
        btnDeleteWater = view.findViewById(R.id.btn_del_water);
        View.OnClickListener changeAmountWater = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cv = null;
                switch (v.getId()) {
                    case R.id.btn_add_water:
                        waterLiters += userProfileData.getNeedWaterCount();
                        userInfoData.setWaterCount(waterLiters);
                        break;
                    case R.id.btn_del_water:
                        if (waterLiters == 0)
                            return;
                        waterLiters -= userProfileData.getNeedWaterCount();
                        userInfoData.setWaterCount(waterLiters);
                        break;
                }
                tv_water.setText(String.valueOf(waterLiters));
//                usersData.child((DATE_FORMAT.format(dateSelectedByUser)).replaceAll("\\.", "")).setValue(userInfoData);
                userData.child(DATE_FORMAT_FOR_FIREBASE.format(dateSelectedByUser)).setValue(userInfoData);
            }
        };
        btnAddWater.setOnClickListener(changeAmountWater);
        btnDeleteWater.setOnClickListener(changeAmountWater);

        return view;
    }

    private void setCurrentDate() {
        currentDate = new Date();
        dateSelectedByUser = currentDate;
        setDate(currentDate);
    }

    private void setDate(Date date) {
        String dateText = DATE_FORMAT.format(date);
        editor.putString(APP_PREFERENCES_DATE, DATE_FORMAT_FOR_FIREBASE.format(date));
        editor.apply();
        tv_date.setText(dateText);
    }

    private void loadingDataPerDay(final Date date) {
        userData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String db_date = DATE_FORMAT_FOR_FIREBASE.format(date);
                if (snapshot.child(db_date).exists()) {
                    userInfoData = snapshot.child(db_date).getValue(UserInfoData.class);
                } else {
                    userInfoData = new UserInfoData(DATE_FORMAT.format(date));
                    userData.child(db_date).setValue(userInfoData);
                }

                updateUI();
            }

            private void updateUI() {
                proteins = userInfoData.getProteins();
                fats = userInfoData.getFats();
                carbohydrates = userInfoData.getCarbohydrates();
                waterLiters = userInfoData.getWaterCount();
                kcalForBreakfast = userInfoData.getBreakfastCalories();
                kcalForLunch = userInfoData.getLunchCalories();
                kcalForDinner = userInfoData.getDinnerCalories();
                kcalForSnack = userInfoData.getSnackCalories();

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                System.out.println(userProfileData.getDateOfBorn());
                LocalDate ld = LocalDate.parse(userProfileData.getDateOfBorn(), dtf);
                DateFormat df = new SimpleDateFormat("yyyy");
                long age = Long.parseLong(df.format(date)) - ld.getYear();

                System.out.println(userProfileData.getSex());

                float ccal = 0.0f;
                switch (userProfileData.getSex()) {
                    case WOMEN:
                        ccal = (655.1f + (9.563f * userProfileData.getStartWeight()) + (1.85f * userProfileData.getTell()) - (4.676f * age)) * userProfileData.getActivityValue();
                        System.out.println(userProfileData.getStartWeight() + " " + userProfileData.getTell() + " " + age + " " + userProfileData.getActivityValue() + " ---- " + ccal);
                        userProfileData.setNeedCalories(Math.round(ccal));
                        break;
                    case MEN:
                        ccal = (66.5f + (13.75f * userProfileData.getStartWeight()) + (5.003f * userProfileData.getTell()) - (6.775f * age)) * userProfileData.getActivityValue();
                        userProfileData.setNeedCalories(Math.round(ccal));
                }

                recommendedKcal = userProfileData.getNeedCalories();
                dailyKcal = kcalForBreakfast + kcalForLunch + kcalForDinner + kcalForSnack;

                tv_recommendedKcal.setText(DECIMAL_FORMAT.format(recommendedKcal));
                tv_dailyKcalInChart.setText(DECIMAL_FORMAT.format(dailyKcal));

                tv_proteins.setText(DECIMAL_FORMAT.format(proteins));
                tv_fats.setText(DECIMAL_FORMAT.format(fats));
                tv_carbohydrates.setText(DECIMAL_FORMAT.format(carbohydrates));
                tv_water.setText(DECIMAL_FORMAT.format(waterLiters));
                System.out.println("tb water - " + DECIMAL_FORMAT.format(waterLiters));

                if (recommendedKcal - dailyKcal > 0)
                    tv_remainingKcal.setText(DECIMAL_FORMAT.format(recommendedKcal - dailyKcal));
                else
                    tv_remainingKcal.setText(DECIMAL_FORMAT.format(0));

                createCalorieChartByDiet();

                createDailyCalorieChart();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        System.out.println("water - " + userInfoData.getWaterCount());
    }

    private void createDailyCalorieChart() {
        List<SliceValue> values = new ArrayList<SliceValue>();
        if (dailyKcal <= recommendedKcal) {
            values.add(new SliceValue((float) dailyKcal, Color.parseColor("#95D5B2")));
            values.add(new SliceValue((float) recommendedKcal - dailyKcal, Color.parseColor("#A3C5F5")));
        } else if (dailyKcal <= recommendedKcal * 2) {
            values.add(new SliceValue((float) dailyKcal - recommendedKcal, Color.parseColor("#184059")));
            values.add(new SliceValue((float) 2 * recommendedKcal - dailyKcal, Color.parseColor("#95D5B2")));
        } else {
            values.add(new SliceValue((float) 1, Color.parseColor("#184059")));
            values.add(new SliceValue((float) 0, Color.parseColor("#95D5B2")));
        }
        dataDailyCalorieChart = new PieChartData(values);
        dataDailyCalorieChart.setHasCenterCircle(true);
        dataDailyCalorieChart.setCenterCircleScale((float) .7);

        dailyCalorieChart.setPieChartData(dataDailyCalorieChart);
        dailyCalorieChart.setInteractive(false);
        dailyCalorieChart.setChartRotation(270, false);
    }

    private void createCalorieChartByDiet() {
        if (kcalForBreakfast == 0
                && kcalForDinner == 0
                && kcalForLunch == 0
                && kcalForSnack == 0) {
            kcalByDiet.setVisibility(View.GONE);
        }

        int[] colorChart = new int[]{
                Color.parseColor("#75C5FF"),
                Color.parseColor("#4059AD"),
                Color.parseColor("#119DA4"),
                Color.parseColor("#97D8C4")
        };
        List<SliceValue> values = new ArrayList<SliceValue>();
        values.add(new SliceValue((float) kcalForBreakfast, colorChart[0]));
        values.add(new SliceValue((float) kcalForLunch, colorChart[1]));
        values.add(new SliceValue((float) kcalForDinner, colorChart[2]));
        values.add(new SliceValue((float) kcalForSnack, colorChart[3]));

        dataCalorieChartByDiet = new PieChartData(values);
        dataCalorieChartByDiet.setHasLabels(true);
        dataCalorieChartByDiet.setValueLabelTypeface(ResourcesCompat.getFont(getContext(), R.font.montserrat_regular));
        dataCalorieChartByDiet.setValueLabelTextSize(12);
        dataCalorieChartByDiet.setValueLabelBackgroundEnabled(false);

        calorieChartByDiet.setPieChartData(dataCalorieChartByDiet);
        calorieChartByDiet.setInteractive(false);
    }
}