package com.ngapainya.ngapainya.activity.owner;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ngapainya.ngapainya.R;
import com.ngapainya.ngapainya.fragment.owner.OwnerProfileFragment;
import com.ngapainya.ngapainya.fragment.owner.child.PostProgramFragment;
import com.ngapainya.ngapainya.fragment.volunteer.ExploreFragment;
import com.ngapainya.ngapainya.fragment.volunteer.HomeFragment;
import com.ngapainya.ngapainya.fragment.volunteer.NotificationFragment;
import com.ngapainya.ngapainya.fragment.volunteer.child.EditProfileFragment;
import com.ngapainya.ngapainya.fragment.volunteer.child.FindFriendFragment;
import com.ngapainya.ngapainya.helper.SessionManager;
import com.ngapainya.ngapainya.helper.TypefaceSpan;

import java.util.HashMap;

public class ContainerActivity extends ActionBarActivity {
    private Toolbar toolbar;
    private View create_bar;
    private RadioGroup actionBarRadioGroup;
    private RadioGroup createPostRadioGroup;
    //define fragment manager
    private FragmentManager manager;
    private FragmentTransaction transaction;

    //variable fragment
    private OwnerProfileFragment ownerProfileFragment;
    private HomeFragment homeFragment;
    private ExploreFragment exploreFragment;
    private NotificationFragment notificationFragment;

    private PostProgramFragment postProgramFragment;

    /**/

    private SessionManager sessionManager;
    private HashMap<String, String> user;

    public void onClick(View view){
        ownerProfileFragment.onClick(view);
    }

    public void homeTitleBar() {
        toolbar.setTitleTextAppearance(ContainerActivity.this, R.style.Toolbar_TitleText);
        SpannableString s = new SpannableString("Ngapain");
        s.setSpan(new TypefaceSpan(this, "Mission-Script.otf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
    }

    public void standardTitleBar(String title) {
        toolbar.setTitleTextAppearance(ContainerActivity.this, R.style.Toolbar_SmallTitleText);
        getSupportActionBar().setTitle(title);
    }

    public void changeActionbarStyle(boolean isProfile) {
        if (isProfile) {
            //This method is used to change the status bar color
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.ActionbarColorDark));
            }
            getSupportActionBar()
                    .setBackgroundDrawable
                            (new ColorDrawable(getResources().getColor(R.color.ActionbarColor)));

            getSupportActionBar()
                    .setTitle(user.get(SessionManager.KEY_FULLNAME));
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.ColorPrimaryDark));
            }
            getSupportActionBar()
                    .setBackgroundDrawable
                            (new ColorDrawable(getResources().getColor(R.color.ColorOwner)));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_owner);

        sessionManager = new SessionManager(this);
        user = sessionManager.getUserDetails();

        if(!user.get(sessionManager.KEY_STATUS).equals("owner")){
            Intent intent = new Intent(this, com.ngapainya.ngapainya.activity.volunteer.ContainerActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(this.toolbar);
        getSupportActionBar()
                .setBackgroundDrawable
                        (new ColorDrawable(getResources().getColor(R.color.ColorOwner)));

        //This method is used to change the status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.ColorOwnerDark));
        }


        /*Instantiate the Fragment*/
        homeFragment            = new HomeFragment();
        exploreFragment         = new ExploreFragment();
        notificationFragment    = new NotificationFragment();
        ownerProfileFragment    = new OwnerProfileFragment();

        postProgramFragment = new PostProgramFragment();

        create_bar = findViewById(R.id.create_post_bar); //use for make an animation

        //set home fragment for default
        RadioButton r1 = (RadioButton) findViewById(R.id.homeBtn);
        r1.setChecked(true);

        //use to call starting fragment
        if (savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();
            String string = "";
            if (bundle != null) {
                string = bundle.getString("extra");
                switch (string) {
                    case "find_friend":
                        FindFriendFragment findFriendFragment = new FindFriendFragment();
                        changeFragment(findFriendFragment);
                        break;
                    case "edit_profile":
                        EditProfileFragment editProfileFragment = new EditProfileFragment();
                        changeFragment(editProfileFragment);
                        break;
                }
            } else {
                changeFragment(homeFragment);
                homeTitleBar();
            }
        }

        actionBarRadioGroup = (RadioGroup) findViewById(R.id.actionRadioBtn);
        actionBarRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int pos;
                pos = actionBarRadioGroup.indexOfChild(findViewById(checkedId));

                switch (pos) {
                    case 0:
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        homeTitleBar();
                        changeFragment(homeFragment);
                        changeActionbarStyle(false);
                        break;
                    case 1:
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        standardTitleBar("Explore");
                        changeFragment(exploreFragment);
                        changeActionbarStyle(false);
                        break;
                    case 3:
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        standardTitleBar("Notification");
                        changeFragment(notificationFragment);
                        changeActionbarStyle(false);
                        break;
                    case 4:
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        standardTitleBar("Username");
                        changeFragment(ownerProfileFragment);
                        changeActionbarStyle(true);
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
                RadioButton r5 = (RadioButton) findViewById(R.id.programBtn);
                switch (pos)
                {
                    case 0 :
                        Toast.makeText(getBaseContext(), "You have Clicked RadioButton 3.1",
                                Toast.LENGTH_SHORT).show();
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
                    case 4 :
                        changeFragment(postProgramFragment);
                        r5.setChecked(false);
                        break;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();

            String packageName = "com.ngapainya.ngapainya.fragment.volunteer.";
            Fragment fragmentPopped = getSupportFragmentManager().findFragmentByTag(packageName + "MyProfileFragment");

            if (fragmentPopped != null && fragmentPopped.isVisible()) {
                changeActionbarStyle(true);
            } else {
                changeActionbarStyle(false);
            }
        }
    }

    public void createPost(View view) {
        final ToggleButton createStatusBtn = (ToggleButton) findViewById(R.id.createStatusBtn);

        RelativeLayout actionbar = (RelativeLayout) findViewById(R.id.actionbar);
        actionbar.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        final int height = actionbar.getMeasuredHeight();

        if (createStatusBtn.isChecked()) {
            //Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_in_anim);
            Animation slideUp = new TranslateAnimation(0, 0, 0, -height);
            //slideUp.setFillAfter(true);
            slideUp.setFillEnabled(true);
            slideUp.setDuration(500);

            create_bar.startAnimation(slideUp);
            slideUp.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    createStatusBtn.setClickable(false);
                }

                @Override
                public void onAnimationEnd(Animation arg0) {
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) create_bar.getLayoutParams();
                    lp.bottomMargin = height; // use topmargin for the y-property, left margin for the x-property of your view
                    create_bar.setLayoutParams(lp);
                    createStatusBtn.setClickable(true);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        } else {
            Animation slideDown = new TranslateAnimation(0, 0, 0, 100);
            slideDown.setDuration(300);

            create_bar.startAnimation(slideDown);
            slideDown.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    createStatusBtn.setClickable(false);
                }

                @Override
                public void onAnimationEnd(Animation arg0) {
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) create_bar.getLayoutParams();
                    lp.bottomMargin = 0; // use topmargin for the y-property, left margin for the x-property of your view
                    create_bar.setLayoutParams(lp);
                    createStatusBtn.setClickable(true);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    public void changeFragment(Fragment fragment) {

        //old code
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.replace(R.id.content_fragment, fragment);
        transaction.addToBackStack(fragment.getClass().getName());
        transaction.commit();

        //newest code
        /*String backStateName = fragment.getClass().getName();

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped) { //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.content_fragment, fragment);
            ft.addToBackStack(backStateName);
            ft.commit();
        }*/
    }
}
