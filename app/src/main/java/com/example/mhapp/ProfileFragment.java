package com.example.mhapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

/**
 * Фрагмент экрана Профиль
 */
public class ProfileFragment extends Fragment {

    ImageView img;
    ImageView avatar;

    TextView tv_name;
    TextView tv_sex;
    TextView tv_weight;
    TextView tv_date;
    TextView tv_aim;
    TextView tv_kol;
    TextView tv_recommended;

    private UserProfileData userData;

    Drawable[] avatars = new Drawable[4];

    public ProfileFragment() {

    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        avatar = (ImageView) view.findViewById(R.id.btn_switch_day_ahead);

        avatars[0] = getActivity().getDrawable(R.drawable.avatar_man_1);
        avatars[1] = getActivity().getDrawable(R.drawable.avatar_woman_1);
        avatars[2] = getActivity().getDrawable(R.drawable.avatar_man_2);
        avatars[3] = getActivity().getDrawable(R.drawable.avatar_woman_2);

        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_sex = (TextView) view.findViewById(R.id.tv_sex);
        tv_weight = (TextView) view.findViewById(R.id.tv_weight);
        tv_date = (TextView) view.findViewById(R.id.tv_date_born);
        tv_aim = (TextView) view.findViewById(R.id.tv_aim);

        Bundle arguments = getActivity().getIntent().getExtras();
        if (arguments != null) {
            userData = (UserProfileData) arguments.getSerializable("user_data");

            switch (userData.getAvatarId()) {
                case 0:
                    avatar.setImageDrawable(avatars[0]);
                    break;
                case 1:
                    avatar.setImageDrawable(avatars[1]);
                    break;
                case 2:
                    avatar.setImageDrawable(avatars[2]);
                    break;
                case 3:
                    avatar.setImageDrawable(avatars[3]);
                    break;
            }
            tv_name.setText(userData.getName());
            switch (userData.getSex()) {
                case MEN:
                    tv_sex.setText("мужской");
                    break;
                case WOMEN:
                    tv_sex.setText("женский");
                    break;
            }
            tv_weight.setText(String.valueOf(userData.getStartWeight()));
            tv_date.setText(userData.getDateOfBorn());
            switch (userData.getPurpose()) {
                case SLIM_PURPOSE:
                    tv_aim.setText("сбросить вес");
                    break;
                case SUPPORT_PURPOSE:
                    tv_aim.setText("поддержать вес");
                    break;
                case GAIN_PURPOSE:
                    tv_aim.setText("набрать вес");
                    break;
            }
        }

        img=(ImageView) view.findViewById(R.id.btn_set);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity().getBaseContext(), SettingsActivity.class);
                intent.putExtra("user_data", userData);
                startActivity(intent);
            }
        });

        return view;
    }
}