package com.ngapainya.ngapainya.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.ngapainya.ngapainya.R;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostProgramFragment extends Fragment implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener{
    private FragmentActivity myContext;
    private View myFragmentView;
    private Spinner language;
    private Calendar calendar;
    private DateFormat dateFormat;
    private EditText pjtStart;
    private EditText pjtEnd;

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calendar = Calendar.getInstance();
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_post_program, container, false);

        language = (Spinner) myFragmentView.findViewById(R.id.language);
        ArrayAdapter adapterSpinner = ArrayAdapter.createFromResource(myContext,
                R.array.language_array, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        language.setAdapter(adapterSpinner);
        language.setOnItemSelectedListener(this);
        pjtStart = (EditText) myFragmentView.findViewById(R.id.pjtStart);
        pjtEnd = (EditText) myFragmentView.findViewById(R.id.pjtEnd);

        return myFragmentView;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pjtStart:
                DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(myContext.getSupportFragmentManager(), "pjtStart");
                break;
            case R.id.pjtEnd:
                DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(myContext.getSupportFragmentManager(), "pjtEnd");
                break;
        }
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        calendar.set(year, month, day);
        //Toast.makeText(myContext, "date set "+ day, Toast.LENGTH_LONG).show();
        update();
    }

    private void update() {
        if (myContext.getSupportFragmentManager().findFragmentByTag("pjtStart") != null) {
            pjtStart.setText(dateFormat.format(calendar.getTime()));
            calendar.add(Calendar.DATE, 1);
            pjtEnd.setText(dateFormat.format(calendar.getTime()));
            //Toast.makeText(myContext, "date set ", Toast.LENGTH_LONG).show();
        }
        if (myContext.getSupportFragmentManager().findFragmentByTag("pjtEnd") != null) {
            pjtEnd.setText(dateFormat.format(calendar.getTime()));
            calendar.add(Calendar.DATE, 1);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch(adapterView.getId()){
            case R.id.language:
                //do something here
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
