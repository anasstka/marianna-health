package com.example.mhapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mhapp.services.NotificationService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase firebaseDB;
    DatabaseReference users;

    // массив доступных аватарок
    Drawable[] avatars = new Drawable[4];

    //диалоговое екно
    Dialog dialogChangeAvatar;

    UserProfileData userData;

    LinearLayout lv_back;
    LinearLayout lt_setting;
    LinearLayout lv_avatar;

    EditText et_weight;
    EditText et_tell;
    EditText et_name;
    EditText et_breakfast;
    EditText et_lunch;
    EditText et_dinner;
    EditText et_snack;
    EditText et_aim_weight;
    EditText et_water;

    Spinner sp_water;
    Spinner sp_eat;
    Spinner sp_kol_water;
    Spinner sp_activ;
    Spinner sp_aim;

    ImageView avatar_image;
    ImageView img_edit_set;
    ImageView img_edit_yved;
    ImageView img_edit_per_set;

    TextView tv_exit;
    TextView tv_set_cancel;
    TextView tv_set_true;
    TextView tv_set_per_true;
    TextView tv_set_per_cancel;
    TextView tv_yvem_true;
    TextView tv_yvem_cancel;
    int avatar_id = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        avatars[0] = getDrawable(R.drawable.avatar_man_1);
        avatars[1] = getDrawable(R.drawable.avatar_woman_1);
        avatars[2] = getDrawable(R.drawable.avatar_man_2);
        avatars[3] = getDrawable(R.drawable.avatar_woman_2);

        auth = FirebaseAuth.getInstance();
        firebaseDB = FirebaseDatabase.getInstance();
        users = firebaseDB.getReference(FirebaseConst.USERS_DB);

        // инициализация диалогового окна
        dialogChangeAvatar = new Dialog(this);

        // настройка интерфейса
        {
            lv_back = (LinearLayout) findViewById(R.id.lv_back);
            lt_setting = (LinearLayout) findViewById(R.id.lt_setting);
            lv_avatar = (LinearLayout) findViewById(R.id.lv_avatar);

            avatar_image = (ImageView) findViewById(R.id.avatar_image);
            lv_avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openDialogChangeAvatar();
                }
            });
            lv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                    intent.putExtra("user_data", userData);
                    startActivity(intent);
                }
            });

            et_weight = (EditText) findViewById(R.id.et_weight);
            et_weight.setEnabled(false);

            et_tell = (EditText) findViewById(R.id.et_tell);
            et_tell.setEnabled(false);

            et_name = (EditText) findViewById(R.id.et_name);
            et_name.setEnabled(false);

            et_breakfast = (EditText) findViewById(R.id.et_breakfast);
            et_breakfast.setEnabled(false);

            et_lunch = (EditText) findViewById(R.id.et_lunch);
            et_lunch.setEnabled(false);

            et_dinner = (EditText) findViewById(R.id.et_dinner);
            et_dinner.setEnabled(false);

            et_snack = (EditText) findViewById(R.id.et_snack);
            et_snack.setEnabled(false);

            et_aim_weight = (EditText) findViewById(R.id.et_aim_weight);
            et_aim_weight.setEnabled(false);

            et_water = (EditText) findViewById(R.id.et_water);
            et_water.setEnabled(false);

            sp_water = (Spinner) findViewById(R.id.sp_water);
            sp_eat = (Spinner) findViewById(R.id.sp_eat);
            sp_kol_water = (Spinner) findViewById(R.id.sp_kol_water);
            sp_activ = (Spinner) findViewById(R.id.sp_activ);
            sp_aim = (Spinner) findViewById(R.id.sp_aim);

            ArrayAdapter<String> water = new ArrayAdapter<>(SettingsActivity.this, R.layout.support_simple_spinner_dropdown_item, new String[]{
                    "Да", "Нет"
            });
            sp_water.setAdapter(water);
            sp_water.setEnabled(false);

            ArrayAdapter<String> eat = new ArrayAdapter<>(SettingsActivity.this, R.layout.support_simple_spinner_dropdown_item, new String[]{
                    "Да", "Нет"
            });
            sp_eat.setAdapter(eat);
            sp_eat.setEnabled(false);

            ArrayAdapter<String> kol_water = new ArrayAdapter<>(SettingsActivity.this, R.layout.support_simple_spinner_dropdown_item, new String[]{
                    "0.25", "0.5", "0.75", "1"
            });
            sp_kol_water.setAdapter(kol_water);
            sp_kol_water.setEnabled(false);

            ArrayAdapter<String> activ = new ArrayAdapter<>(SettingsActivity.this, R.layout.support_simple_spinner_dropdown_item, new String[]{
                    "Не активный", "Умеренные нагрузки", "Тренировки 3-5 раз в неделю", "Интенсивные нагрузки", "Профессиональный спортсмен"
            });
            sp_activ.setAdapter(activ);
            sp_activ.setEnabled(false);

            ArrayAdapter<String> aim = new ArrayAdapter<>(SettingsActivity.this, R.layout.support_simple_spinner_dropdown_item, new String[]{
                    "Сбросить вес", "Поддержать вес", "Набрать вес"
            });
            sp_aim.setAdapter(aim);
            sp_aim.setEnabled(false);

            img_edit_set = (ImageView) findViewById(R.id.im_edit_set);
            img_edit_yved = (ImageView) findViewById(R.id.im_edit_yved);
            img_edit_per_set = (ImageView) findViewById(R.id.im_edit_per_set);

            tv_exit = (TextView) findViewById(R.id.tv_exit);

            tv_set_cancel = (TextView) findViewById(R.id.tv_set_cancel);
            tv_set_cancel.setEnabled(false);

            tv_set_true = (TextView) findViewById(R.id.tv_set_true);
            tv_set_true.setEnabled(false);

            tv_set_per_cancel = (TextView) findViewById(R.id.tv_set_per_cancel);
            tv_set_per_cancel.setEnabled(false);

            tv_set_per_true = (TextView) findViewById(R.id.tv_set_per_true);
            tv_set_per_true.setEnabled(false);

            tv_yvem_cancel = (TextView) findViewById(R.id.tv_yvem_cancel);
            tv_yvem_cancel.setEnabled(false);

            tv_yvem_true = (TextView) findViewById(R.id.tv_yvem_true);
            tv_yvem_true.setEnabled(false);

            tv_exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(SettingsActivity.this, StartActivity.class);
                    startActivity(intent);
                }
            });

            img_edit_set.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    et_weight.setEnabled(true);
                    et_tell.setEnabled(true);
                    sp_aim.setEnabled(true);
                    et_name.setEnabled(true);
                    sp_activ.setEnabled(true);
                    et_aim_weight.setEnabled(true);

                    tv_set_cancel.setEnabled(true);
                    tv_set_true.setEnabled(true);
                }
            });

            tv_set_true.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    et_weight.setEnabled(false);
                    userData.setStartWeight(Float.parseFloat(et_weight.getText().toString()));
                    et_tell.setEnabled(false);
                    userData.setTell(Float.parseFloat(et_tell.getText().toString()));
                    et_aim_weight.setEnabled(false);
                    userData.setFinishWeight(Float.parseFloat(et_aim_weight.getText().toString()));
                    sp_aim.setEnabled(false);
                    switch (sp_aim.getSelectedItemPosition()) {
                        case 0:
                            userData.setPurpose(UserPurpose.SLIM_PURPOSE);
                            break;
                        case 1:
                            userData.setPurpose(UserPurpose.SUPPORT_PURPOSE);
                            break;
                        case 2:
                            userData.setPurpose(UserPurpose.GAIN_PURPOSE);
                            break;
                    }
                    et_name.setEnabled(false);
                    userData.setName(et_name.getText().toString());
                    sp_activ.setEnabled(false);
                    switch (sp_activ.getSelectedItemPosition()) {
                        case 0:
                            userData.setActivityValue(1.2f);
                            break;
                        case 1:
                            userData.setActivityValue(1.375f);
                            break;
                        case 2:
                            userData.setActivityValue(1.55f);
                            break;
                        case 3:
                            userData.setActivityValue(1.725f);
                            break;
                        case 4:
                            userData.setActivityValue(1.9f);
                            break;
                    }

                    tv_set_cancel.setEnabled(false);
                    tv_set_true.setEnabled(false);

                    // подключаемся к БД
                    users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(userData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(), "Изменения успешно сохранены!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Ощибка сохранения. " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });

            tv_set_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    et_weight.setEnabled(false);
                    et_tell.setEnabled(false);
                    sp_aim.setEnabled(false);
                    et_name.setEnabled(false);
                    sp_activ.setEnabled(false);

                    tv_set_cancel.setEnabled(false);
                    tv_set_true.setEnabled(false);
                    et_aim_weight.setEnabled(false);
                }
            });
            img_edit_yved.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sp_water.setEnabled(true);
                    sp_eat.setEnabled(true);

                    tv_yvem_cancel.setEnabled(true);
                    tv_yvem_true.setEnabled(true);
                }
            });

            tv_yvem_true.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sp_water.setEnabled(false);
                    switch (sp_water.getSelectedItemPosition()) {
                        case 0:
                            userData.setInfoAboutWater(true);
                            break;
                        case 1:
                            userData.setInfoAboutWater(false);
                            break;
                    }
                    sp_eat.setEnabled(false);
                    switch (sp_eat.getSelectedItemPosition()) {
                        case 0:
                            userData.setInfoAboutFood(true);
                            break;
                        case 1:
                            userData.setInfoAboutFood(false);
                            break;
                    }

                    // подключаемся к БД
                    users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(userData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(), "Изменения успешно сохранены!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Ощибка сохранения. " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                    tv_yvem_cancel.setEnabled(false);
                    tv_yvem_true.setEnabled(false);

                    stopService(new Intent(getApplicationContext(), NotificationService.class));
                    Intent intent = new Intent(getApplicationContext(), NotificationService.class);
                    intent.putExtra("user_data", userData);
                    startService(intent);
                }
            });

            tv_yvem_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sp_water.setEnabled(false);
                    sp_eat.setEnabled(false);

                    tv_yvem_cancel.setEnabled(false);
                    tv_yvem_true.setEnabled(false);
                }
            });

            img_edit_per_set.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sp_kol_water.setEnabled(true);
                    et_breakfast.setEnabled(true);
                    et_dinner.setEnabled(true);
                    et_lunch.setEnabled(true);
                    et_snack.setEnabled(true);
                    et_water.setEnabled(true);

                    tv_set_per_cancel.setEnabled(true);
                    tv_set_per_true.setEnabled(true);
                }
            });

            tv_set_per_true.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sp_kol_water.setEnabled(false);
                    switch (sp_kol_water.getSelectedItemPosition()) {
                        case 0:
                            userData.setNeedWaterCount(0.25f);
                            break;
                        case 1:
                            userData.setNeedWaterCount(0.5f);
                            break;
                        case 2:
                            userData.setNeedWaterCount(0.75f);
                            break;
                        case 3:
                            userData.setNeedWaterCount(1.0f);
                            break;
                    }
                    et_breakfast.setEnabled(false);
                    userData.setTimeOfBreakfast(et_breakfast.getText().toString());
                    et_lunch.setEnabled(false);
                    userData.setTimeOfLunch(et_lunch.getText().toString());
                    et_dinner.setEnabled(false);
                    userData.setTimeOfDinner(et_dinner.getText().toString());
                    et_snack.setEnabled(false);
                    userData.setTimeOfSnack(et_snack.getText().toString());
                    et_water.setEnabled(false);
                    userData.setTimeOfWater(et_water.getText().toString());

                    // подключаемся к БД
                    users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(userData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(), "Изменения успешно сохранены!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Ощибка сохранения. " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                    tv_set_per_cancel.setEnabled(false);
                    tv_set_per_true.setEnabled(false);

                    stopService(new Intent(getApplicationContext(), NotificationService.class));
                    Intent intent = new Intent(getApplicationContext(), NotificationService.class);
                    intent.putExtra("user_data", userData);
                    startService(intent);
                }
            });

            tv_set_per_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sp_kol_water.setEnabled(false);
                    et_breakfast.setEnabled(false);
                    et_dinner.setEnabled(false);
                    et_lunch.setEnabled(false);
                    et_snack.setEnabled(false);
                    et_water.setEnabled(false);

                    tv_set_per_cancel.setEnabled(false);
                    tv_set_per_true.setEnabled(false);
                }
            });
        }

        // заполнение данных из бд при открытии окна
        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            userData = (UserProfileData) arguments.getSerializable("user_data");
            avatar_image.setImageDrawable(avatars[userData.getAvatarId()]);

            et_weight.setText(String.valueOf(userData.getStartWeight()));
            et_tell.setText(String.valueOf(userData.getTell()));
            et_name.setText(userData.getName());
            et_aim_weight.setText(String.valueOf(userData.getFinishWeight()));
            switch (userData.getPurpose()) {
                case SLIM_PURPOSE:
                    sp_aim.setSelection(0);
                    break;
                case SUPPORT_PURPOSE:
                    sp_aim.setSelection(1);
                    break;
                case GAIN_PURPOSE:
                    sp_aim.setSelection(2);
                    break;
            }

            if (userData.getActivityValue() == 1.2f) {
                sp_activ.setSelection(0);
            } else if (userData.getActivityValue() == 1.375f) {
                sp_activ.setSelection(1);
            } else if (userData.getActivityValue() == 1.55f) {
                sp_activ.setSelection(2);
            } else if (userData.getActivityValue() == 1.725f) {
                sp_activ.setSelection(3);
            } else if (userData.getActivityValue() == 1.9f) {
                sp_activ.setSelection(4);
            }

            if (userData.isInfoAboutWater()) {
                sp_water.setSelection(0);
            } else {
                sp_water.setSelection(1);
            }

            if (userData.isInfoAboutFood()) {
                sp_eat.setSelection(0);
            } else {
                sp_eat.setSelection(0);
            }

            if (userData.getNeedWaterCount() == 0.25f) {
                sp_kol_water.setSelection(0);
            } else if (userData.getNeedWaterCount() == 0.5f) {
                sp_kol_water.setSelection(1);
            } else if (userData.getNeedWaterCount() == 0.75f) {
                sp_kol_water.setSelection(2);
            } else if (userData.getNeedWaterCount() == 1.0f) {
                sp_kol_water.setSelection(3);
            }

            et_breakfast.setText(userData.getTimeOfBreakfast());
            et_lunch.setText(userData.getTimeOfLunch());
            et_dinner.setText(userData.getTimeOfDinner());
            et_snack.setText(userData.getTimeOfSnack());
        }
        // сохранение данных в бд
    }

    private void openDialogChangeAvatar() {
        dialogChangeAvatar.setContentView(R.layout.dialog_change_avatar);
        dialogChangeAvatar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final ImageView iv_avatar1 = dialogChangeAvatar.findViewById(R.id.avatar1);
        final ImageView iv_avatar2 = dialogChangeAvatar.findViewById(R.id.avatar2);
        final ImageView iv_avatar3 = dialogChangeAvatar.findViewById(R.id.avatar3);
        final ImageView iv_avatar4 = dialogChangeAvatar.findViewById(R.id.avatar4);
        TextView btnChange = dialogChangeAvatar.findViewById(R.id.dialog_btn_change);
        TextView btnCancel = dialogChangeAvatar.findViewById(R.id.dialog_btn_cancel);

        // при открытии окна выделение уже установленной авы
        switch (userData.getAvatarId()) {
            case 0:
                iv_avatar1.setSelected(true);
                break;
            case 1:
                iv_avatar2.setSelected(true);
                break;
            case 2:
                iv_avatar3.setSelected(true);
                break;
            case 3:
                iv_avatar4.setSelected(true);
                break;
        }

        // смена авы
        View.OnClickListener onClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.dialog_btn_change:
                        userData.setAvatarId(avatar_id);
                        // подключаемся к БД
                        users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(userData)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(), "Изменения успешно сохранены!", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Ошибка сохранения. " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                        dialogChangeAvatar.dismiss();
                        break;
                    case R.id.dialog_btn_cancel:
                        dialogChangeAvatar.dismiss();
                        break;
                }
            }
        };

        btnChange.setOnClickListener(onClick);
        btnCancel.setOnClickListener(onClick);

        // переключение между авами
        View.OnClickListener onClickOnAvatar = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.avatar1:
                        iv_avatar1.setSelected(true);
                        iv_avatar2.setSelected(false);
                        iv_avatar3.setSelected(false);
                        iv_avatar4.setSelected(false);
                        avatar_id = 0;
                        break;
                    case R.id.avatar2:
                        iv_avatar1.setSelected(false);
                        iv_avatar2.setSelected(true);
                        iv_avatar3.setSelected(false);
                        iv_avatar4.setSelected(false);
                        avatar_id = 1;
                        break;
                    case R.id.avatar3:
                        iv_avatar1.setSelected(false);
                        iv_avatar2.setSelected(false);
                        iv_avatar3.setSelected(true);
                        iv_avatar4.setSelected(false);
                        avatar_id = 2;
                        break;
                    case R.id.avatar4:
                        iv_avatar1.setSelected(false);
                        iv_avatar2.setSelected(false);
                        iv_avatar3.setSelected(false);
                        iv_avatar4.setSelected(true);
                        avatar_id = 3;
                        break;
                }
            }
        };

        iv_avatar1.setOnClickListener(onClickOnAvatar);
        iv_avatar2.setOnClickListener(onClickOnAvatar);
        iv_avatar3.setOnClickListener(onClickOnAvatar);
        iv_avatar4.setOnClickListener(onClickOnAvatar);

        dialogChangeAvatar.show();
    }
}