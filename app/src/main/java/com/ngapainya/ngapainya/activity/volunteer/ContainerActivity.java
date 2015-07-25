package com.ngapainya.ngapainya.activity.volunteer;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ngapainya.ngapainya.R;
import com.ngapainya.ngapainya.fragment.ExploreFragment;
import com.ngapainya.ngapainya.fragment.HomeFragment;
import com.ngapainya.ngapainya.fragment.MyProfileFragment;
import com.ngapainya.ngapainya.fragment.NotificationFragment;


public class ContainerActivity extends ActionBarActivity {
    private Toolbar toolbar;
    private RadioGroup actionBarRadioGroup;
    private RadioGroup createPostRadioGroup;
    //variable fragment
    private MyProfileFragment myProfileFragment;
    private HomeFragment homeFragment;
    private ExploreFragment exploreFragment;
    private NotificationFragment notificationFragment;
    //define fragment manager
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private View create_bar;

    public void onClick(View v){
        myProfileFragment.onClick(v);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==0 && resultCode==1){
            new Handler().post(new Runnable() {
                public void run() {
                    homeFragment = new HomeFragment();
                    changeFragment(homeFragment, new ColorDrawable(getResources().getColor(R.color.ColorPrimary)));
                }
            });
        }
    }

    @Override
    public void onBackPressed(){
        if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            finish();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        create_bar = findViewById(R.id.create_post_bar); //use for make an animation

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(this.toolbar);

        //customize action bar font
        /*code here*/

        //set home fragment for default
        RadioButton r1 = (RadioButton) findViewById(R.id.homeBtn);
        r1.setChecked(true);

        //use to call starting fragment
        homeFragment = new HomeFragment();
        changeFragment(homeFragment, new ColorDrawable(getResources().getColor(R.color.ColorPrimary)));

        actionBarRadioGroup = (RadioGroup) findViewById(R.id.actionRadioBtn);
        actionBarRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int pos;
                pos = actionBarRadioGroup.indexOfChild(findViewById(checkedId));

                switch (pos) {
                    case 0:
                        homeFragment = new HomeFragment();
                        changeFragment(homeFragment, new ColorDrawable(getResources().getColor(R.color.ColorPrimary)));
                        break;
                    case 1:
                        exploreFragment = new ExploreFragment();
                        changeFragment(exploreFragment, new ColorDrawable(getResources().getColor(R.color.ColorPrimary)));
                        break;
                    case 3:
                        notificationFragment = new NotificationFragment();
                        changeFragment(notificationFragment, new ColorDrawable(getResources().getColor(R.color.ColorPrimary)));
                        break;
                    case 4:
                        myProfileFragment = new MyProfileFragment();
                        changeFragment(myProfileFragment, new ColorDrawable(getResources().getColor(R.color.ActionbarColor)));
                        break;
                    default:
                        //The default selection is RadioButton 1
                        Toast.makeText(getBaseContext(), "You have Clicked RadioButton 1",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        createPostRadioGroup = (RadioGroup) findViewById(R.id.createPostRadioBtn);
        createPostRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                int pos;
                pos = createPostRadioGroup.indexOfChild(findViewById(checkedId));
                RadioButton r1 = (RadioButton) findViewById(R.id.statusBtn);
                RadioButton r2 = (RadioButton) findViewById(R.id.locationBtn);
                RadioButton r3 = (RadioButton) findViewById(R.id.picBtn);
                RadioButton r4 = (RadioButton) findViewById(R.id.linkBtn);
                switch (pos)
                {
                    case 0 :
                        /*Intent intent = new Intent(ContainerActivity.this, PostTextActivity.class);
                        startActivity(intent);*/
                        Intent intent = new Intent(ContainerActivity.this, PostTextActivity.class);
                        startActivityForResult(intent,0);
                        r1.setChecked(false);
                        break;
                    case 1 :
                        Toast.makeText(getBaseContext(), "You have Clicked RadioButton 3.2",
                                Toast.LENGTH_SHORT).show();
                        r2.setChecked(false);
                        break;
                    case 2 :
                        Toast.makeText(getBaseContext(), "You have Clicked RadioButton 3.3",
                                Toast.LENGTH_SHORT).show();
                        r3.setChecked(false);
                        break;
                    case 3 :
                        Toast.makeText(getBaseContext(), "You have Clicked RadioButton 3.4",
                                Toast.LENGTH_SHORT).show();
                        r4.setChecked(false);
                        break;
                }
            }
        });
    }

    public void createPost (View view){
        ToggleButton createStatusBtn = (ToggleButton) findViewById(R.id.createStatusBtn);

        if(createStatusBtn.isChecked()){
            //Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_in_anim);
            Animation slideUp = new TranslateAnimation(0, 0, 0, -60);
            //slideUp.setFillAfter(true);
            slideUp.setFillEnabled(true);
            slideUp.setDuration(500);

            create_bar.startAnimation(slideUp);
            slideUp.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation arg0) {
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) create_bar.getLayoutParams();
                    lp.bottomMargin = 60; // use topmargin for the y-property, left margin for the x-property of your view
                    create_bar.setLayoutParams(lp);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }else{
            Animation slideDown = new TranslateAnimation(0, 0, 0, 100);
            slideDown.setDuration(300);

            create_bar.startAnimation(slideDown);
            slideDown.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation arg0) {
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) create_bar.getLayoutParams();
                    lp.bottomMargin = 0; // use topmargin for the y-property, left margin for the x-property of your view
                    create_bar.setLayoutParams(lp);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    public void changeFragment(Fragment fragment, ColorDrawable colorDrawable){
        Fragment newFrag = fragment;
        ColorDrawable newColor = colorDrawable;

        //old code
        /*manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.replace(R.id.content_fragment, newFrag);
        transaction.addToBackStack(null);
        transaction.commit();*/

        //newest code
        String backStateName = fragment.getClass().getName();
        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);
        if (!fragmentPopped){ //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.content_fragment, fragment);
            ft.addToBackStack(backStateName);
            ft.commit();
        }

        //to change the actionbar color
        getSupportActionBar().setBackgroundDrawable(newColor);
    }
}
