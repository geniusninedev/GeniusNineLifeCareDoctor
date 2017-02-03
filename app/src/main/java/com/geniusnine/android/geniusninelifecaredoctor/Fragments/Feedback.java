package com.geniusnine.android.geniusninelifecaredoctor.Fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.geniusnine.android.geniusninelifecaredoctor.Helper.Config;
import com.geniusnine.android.geniusninelifecaredoctor.Helper.DBHelper;
import com.geniusnine.android.geniusninelifecaredoctor.Helper.Utils;
import com.geniusnine.android.geniusninelifecaredoctor.MainActivityDrawer;
import com.geniusnine.android.geniusninelifecaredoctor.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

;


/**
 * Created by Dev on 12-01-2017.
 */

public class Feedback extends Fragment {
    TextView textViewratingstatus;
    EditText editTextmessage,editTextcheck,editTextsuggestion;
    Button buttonsubmit;
    private RatingBar ratingBar;
    private String patient_mobile_Number;
    DBHelper dbHelper;
    Cursor cursor;
    String patient_id;
    TextView textViewcurrentdate;
    final Calendar cal = Calendar.getInstance();
    String myFormat = "yyyy-MM-DD"; //In which you need put here
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.feedback, null);
        dbHelper = new DBHelper(getActivity());
        textViewcurrentdate=(TextView)v.findViewById(R.id.textViewcurrentdate);
        editTextmessage = (EditText) v.findViewById(R.id.edittextmessage);
        editTextcheck = (EditText) v.findViewById(R.id.edittextchecking);
        ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);
        textViewratingstatus = (TextView) v.findViewById(R.id.textViewratingstatus);
        editTextsuggestion = (EditText) v.findViewById(R.id.edittextsuggestion);
        buttonsubmit = (Button) v.findViewById(R.id.buttonsubmit);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, monthOfYear);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            }

        };
        textViewcurrentdate.setText(sdf.format(cal.getTime()));
        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                if (rating == 5) {
                    textViewratingstatus.setText("Awesome App");
                } else if (rating >= 3) {
                    textViewratingstatus.setText("Good App");
                } else if (rating >= 2) {
                    textViewratingstatus.setText("Average App");
                } else if (rating < 1) {
                    textViewratingstatus.setText("Bad App");
                }


            }
        });

        //fetching value from sharedpreference
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        //Fetching thepatient_mobile_Number value form sharedpreferences
        patient_mobile_Number = sharedPreferences.getString(Config.PATIENT_MOBILE_NO_SHARED_PREF, null);
        dbHelper.getPatientData(patient_mobile_Number);
        cursor = dbHelper.getPatientData(patient_mobile_Number);
        cursor.moveToFirst();
        if (cursor != null) {
            patient_id = cursor.getString(cursor.getColumnIndex(dbHelper.COLUMN_PATIENT_ID));
        }
            buttonsubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String message, checking, rating, suggestion,currentdate;
                    message=editTextmessage.getText().toString().trim();
                    checking=editTextcheck.getText().toString().trim();
                    rating= String.valueOf(ratingBar.getRating());
                    suggestion=editTextsuggestion.getText().toString().trim();
                    currentdate=textViewcurrentdate.getText().toString().trim();
                    dbHelper.submitfeedback(patient_id,message, checking, rating, suggestion,currentdate);
                    Toast.makeText(getActivity(),"Feedback Submitted Successfully",Toast.LENGTH_LONG).show();
                    Intent i=new Intent(getActivity(), MainActivityDrawer.class);
                    getActivity().finish();
                    getActivity().startActivity(i);

                }

            });
            return v;
        }

}