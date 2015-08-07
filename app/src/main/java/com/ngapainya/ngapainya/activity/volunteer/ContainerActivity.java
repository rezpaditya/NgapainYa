package com.ngapainya.ngapainya.activity.volunteer;

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
import android.util.Log;
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
import com.ngapainya.ngapainya.activity.GreetingActivity;
import com.ngapainya.ngapainya.fragment.volunteer.ExploreFragment;
import com.ngapainya.ngapainya.fragment.volunteer.HomeFragment;
import com.ngapainya.ngapainya.fragment.volunteer.MyProfileFragment;
import com.ngapainya.ngapainya.fragment.volunteer.NotificationFragment;
import com.ngapainya.ngapainya.fragment.volunteer.child.EditProfileFragment;
import com.ngapainya.ngapainya.fragment.volunteer.child.FindFriendFragment;
import com.ngapainya.ngapainya.fragment.volunteer.child.post.PostLocationFragment;
import com.ngapainya.ngapainya.fragment.volunteer.child.post.PostPhotoFragment;
import com.ngapainya.ngapainya.fragment.volunteer.child.post.PostStatusFragment;
import com.ngapainya.ngapainya.fragment.volunteer.child.post.PostUrlFragment;
import com.ngapainya.ngapainya.helper.SessionManager;
import com.ngapainya.ngapainya.helper.TypefaceSpan;

import java.util.HashMap;


public class ContainerActivity extends ActionBarActivity {
    private Toolbar toolbar;
    private RadioGroup actionBarRadioGroup;
    private RadioGroup createPostRadioGroup;

    //variable main fragment
    private MyProfileFragment myProfileFragment;
    private HomeFragment homeFragment;
    private ExploreFragment exploreFragment;
    private NotificationFragment notificationFragment;

    //variable child fragment
    private PostStatusFragment postStatusFragment;
    private PostPhotoFragment postPhotoFragment;
    private PostUrlFragment postUrlFragment;
    private PostLocationFragment postLocationFragment;

    //define fragment manager
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private View create_bar;

    //manage session
    private SessionManager sessionManager;

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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        //check the login session
        sessionManager = new SessionManager(this);
        if (!sessionManager.checkLogin()) {
            Intent intent = new Intent(this, GreetingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        create_bar = findViewById(R.id.create_post_bar); //use for make an animation

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(this.toolbar);



        //set home fragment for default
        RadioButton r1 = (RadioButton) findViewById(R.id.homeBtn);
        r1.setChecked(true);

        /*Instantiate the Fragment*/
        homeFragment            = new HomeFragment();
        exploreFragment         = new ExploreFragment();
        notificationFragment    = new NotificationFragment();
        myProfileFragment       = new MyProfileFragment();

        postStatusFragment      = new PostStatusFragment();
        postPhotoFragment       = new PostPhotoFragment();
        postUrlFragment         = new PostUrlFragment();
        postLocationFragment    = new PostLocationFragment();


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
                        standardTitleBar("Find friend");
                        break;
                    case "edit_profile":
                        EditProfileFragment editProfileFragment = new EditProfileFragment();
                        changeFragment(editProfileFragment);
                        standardTitleBar("Edit profile");
                        break;
                }
            } else {
                changeFragment(homeFragment);
                homeTitleBar(); //use custom font to the title bar
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
                        changeFragment(myProfileFragment);
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
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int pos;
                pos = createPostRadioGroup.indexOfChild(findViewById(checkedId));
                RadioButton r1 = (RadioButton) findViewById(R.id.statusBtn);
                RadioButton r2 = (RadioButton) findViewById(R.id.locationBtn);
                RadioButton r3 = (RadioButton) findViewById(R.id.picBtn);
                RadioButton r4 = (RadioButton) findViewById(R.id.linkBtn);
                switch (pos) {
                    case 0:
                        changeFragment(postStatusFragment);
                        r1.setChecked(false);
                        break;
                    case 1:
                        changeFragment(postLocationFragment);
                        r2.setChecked(false);
                        break;
                    case 2:
                        changeFragment(postPhotoFragment);
                        r3.setChecked(false);
                        break;
                    case 3:
                        changeFragment(postUrlFragment);
                        r4.setChecked(false);
                        break;
                }
            }
        });
    }

    public void onClick(View v) {
        myProfileFragment.onClick(v);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

            SessionManager sessionManager;
            HashMap<String, String> user;

            sessionManager = new SessionManager(this);
            user = sessionManager.getUserDetails();

            getSupportActionBar()
                    .setTitle(user.get(SessionManager.KEY_NAME));
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.ColorPrimaryDark));
            }
            getSupportActionBar()
                    .setBackgroundDrawable
                            (new ColorDrawable(getResources().getColor(R.color.ColorPrimary)));
        }
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
        Log.e("height", String.valueOf(height));

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

        Log.e("name", fragment.getClass().getName());

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

