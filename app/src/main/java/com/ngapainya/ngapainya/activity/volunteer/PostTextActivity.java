package com.ngapainya.ngapainya.activity.volunteer;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ngapainya.ngapainya.R;

public class PostTextActivity extends ActionBarActivity {
    private Toolbar toolbar;
    private RadioGroup actionBarRadioGroup;
    private RadioGroup createPostRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_text);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_icon_create_post_user);

        actionBarRadioGroup = (RadioGroup) findViewById(R.id.actionRadioBtn);
        actionBarRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int pos;
                pos = actionBarRadioGroup.indexOfChild(findViewById(checkedId));

                switch (pos) {
                    case 0:
                        //
                        break;
                    case 1:
                        //
                        break;
                    case 3:
                        //
                        break;
                    case 4:
                        //
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
                        //
                        break;
                    case 1:
                        //
                        break;
                    case 2:
                        //
                        break;
                    case 3:
                        //
                        break;
                }
            }
        });
    }

    public void createPost (View view){
        /*ToggleButton createStatusBtn = (ToggleButton) findViewById(R.id.createStatusBtn);
        View v = findViewById(R.id.create_post_bar);
        if(createStatusBtn.isChecked()){
            Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_in_anim);
            v.startAnimation(slideUp);
            v.setVisibility(View.VISIBLE);
        }else{
            Animation slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_out_anim);
            v.startAnimation(slideDown);
            v.setVisibility(View.INVISIBLE);
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post_text, menu);
        final MenuItem item = menu.findItem(R.id.action_done);
        item.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPost(v);
            }
        });
        return true;
    }

    public void doPost(View view){
        Toast.makeText(getBaseContext(), "You have Clicked a button",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_settings:
                //do something
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
