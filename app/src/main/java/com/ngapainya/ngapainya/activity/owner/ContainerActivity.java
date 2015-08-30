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
import com.ngapainya.ngapainya.fragment.owner.OwnerProfileFragment;
import com.ngapainya.ngapainya.fragment.owner.child.PostProgramFragment;
import com.ngapainya.ngapainya.fragment.volunteer.ExploreFragment;
import com.ngapainya.ngapainya.fragment.volunteer.HomeFragment;
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

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ContainerActivity class in owner package use to hold all fragment
 * that belong to owner
 */
public class ContainerActivity extends ActionBarActivity {
    /**
     * Inject the view with butterknife
     */
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.create_post_bar)
    View create_bar;
    @Bind(R.id.actionRadioBtn)
    RadioGroup actionBarRadioGroup;
    @Bind(R.id.createPostRadioBtn)
    RadioGroup createPostRadioGroup;

    /**
     * Fragment object
     */
    private OwnerProfileFragment ownerProfileFragment;
    private HomeFragment homeFragment;
    private ExploreFragment exploreFragment;
    private NotificationFragment notificationFragment;

    /**
     * Child fragment, fragment inside the fragment
     */
    private PostStatusFragment postStatusFragment;
    private PostPhotoFragment postPhotoFragment;
    private PostUrlFragment postUrlFragment;
    private PostLocationFragment postLocationFragment;
    private PostProgramFragment postProgramFragment;

    /**
     * Create the activity and its views
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_owner);
        ButterKnife.bind(this);

        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();

        if (!user.get(SessionManager.KEY_STATUS).equals("owner")) {
            Intent intent = new Intent(this, com.ngapainya.ngapainya.activity.volunteer.ContainerActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

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
        homeFragment = new HomeFragment();
        exploreFragment = new ExploreFragment();
        notificationFragment = new NotificationFragment();
        ownerProfileFragment = new OwnerProfileFragment();

        postProgramFragment = new PostProgramFragment();
        postStatusFragment = new PostStatusFragment();
        postLocationFragment = new PostLocationFragment();
        postPhotoFragment = new PostPhotoFragment();
        postUrlFragment = new PostUrlFragment();

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
                homeTitleBar("Ngapain");
            }
        }

        actionBarRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int pos;
                pos = actionBarRadioGroup.indexOfChild(findViewById(checkedId));

                switch (pos) {
                    case 0:
                        changeFragment(homeFragment);
                        break;
                    case 1:
                        changeFragment(exploreFragment);
                        break;
                    case 3:
                        changeFragment(notificationFragment);
                        break;
                    case 4:
                        changeFragment(ownerProfileFragment);
                        break;
                    default:
                        //The default selection is RadioButton 1
                        Toast.makeText(getBaseContext(), "You have Clicked RadioButton 1",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        createPostRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int pos;
                pos = createPostRadioGroup.indexOfChild(findViewById(checkedId));
                RadioButton r1 = (RadioButton) findViewById(R.id.statusBtn);
                RadioButton r2 = (RadioButton) findViewById(R.id.locationBtn);
                RadioButton r3 = (RadioButton) findViewById(R.id.picBtn);
                RadioButton r4 = (RadioButton) findViewById(R.id.linkBtn);
                RadioButton r5 = (RadioButton) findViewById(R.id.programBtn);
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
                    case 4:
                        changeFragment(postProgramFragment);
                        r5.setChecked(false);
                        break;
                }
            }
        });
    }

    /**
     * Do something whenever back button pressed
     */
    @Override
    public void onBackPressed() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();

            String packageName = "com.ngapainya.ngapainya.fragment.owner.";
            Fragment fragmentPopped = getSupportFragmentManager().findFragmentByTag(packageName + "MyProfileFragment");

            if (fragmentPopped != null && fragmentPopped.isVisible()) {
                changeActionbarStyle(true);
            } else {
                changeActionbarStyle(false);
            }
        }
    }

    /**
     * Handle onClick listener from the views with 'onClick' parameter name
     * @param view
     */
    public void onClick(View view) {
        ownerProfileFragment.onClick(view);
    }

    /**
     * Change the actionbar style with home title bar style
     * @param ttl String use to change the toolbar label
     */
    public void homeTitleBar(String ttl) {
        String title = ttl;
        toolbar.setTitleTextAppearance(ContainerActivity.this, R.style.Toolbar_TitleText);
        SpannableString s = new SpannableString(title);
        s.setSpan(new TypefaceSpan(this, "Mission-Script.otf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
    }

    /**
     * Change the actionbar style with standard style
     * @param title
     */
    public void standardTitleBar(String title) {
       /* toolbar.setTitleTextAppearance(ContainerActivity.this, R.style.Toolbar_SmallTitleText);
        getSupportActionBar().setTitle(title);*/

        toolbar.setTitleTextAppearance(ContainerActivity.this, R.style.Toolbar_TitleText);
        SpannableString s = new SpannableString(title);
        s.setSpan(new TypefaceSpan(this, "Mission-Script.otf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
    }

    /**
     * Change actionbar style with profile style and change the ColorPrimaryDark color
     * @param isProfile
     */
    public void changeActionbarStyle(boolean isProfile) {
        if (isProfile) {
            Log.e("changeActionBar", "works");
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

            toolbar.setTitleTextAppearance(ContainerActivity.this, R.style.Toolbar_TitleText);
            SpannableString s = new SpannableString(user.get(SessionManager.KEY_FULLNAME));
            s.setSpan(new TypefaceSpan(this, "Mission-Script.otf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            getSupportActionBar().setTitle(s);

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.ColorOwnerDark));
            }
            getSupportActionBar()
                    .setBackgroundDrawable
                            (new ColorDrawable(getResources().getColor(R.color.ColorOwner)));
        }
    }

    /**
     * Handle the onClick listener from the view with 'createPost' parameter name
     * @param view
     */
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

    /**
     * When this method called, fragment in the ContainerActivity will change immedietly
     * @param fragment Work for Fragment v4
     */
    public void changeFragment(Fragment fragment) {

        //old code
        /*
      Define fragment manager
     */
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content_fragment, fragment, fragment.getClass().getName());
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
