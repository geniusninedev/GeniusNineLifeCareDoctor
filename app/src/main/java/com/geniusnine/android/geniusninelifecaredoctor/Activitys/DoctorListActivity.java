package com.geniusnine.android.geniusninelifecaredoctor.Activitys;


import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import com.geniusnine.android.geniusninelifecaredoctor.Adapter.DoctorListAdapter;
import com.geniusnine.android.geniusninelifecaredoctor.Helper.DBHelper;
import com.geniusnine.android.geniusninelifecaredoctor.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


/**
 * Created by Dev on 20-01-2017.
 */

public class DoctorListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;


Activity activity;
    ArrayList<String> ID_ArrayList = new ArrayList<String>();
    ArrayList<String> NAME_ArrayList = new ArrayList<String>();
    ArrayList<String> DOCTOR_DEGREE_ArrayList = new ArrayList<String>();
    ArrayList<String> DOCTOR_EXP_ArrayList = new ArrayList<String>();
    ArrayList<String> DOCTOR_SPECILIZATION_ArrayList = new ArrayList<String>();
    ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
    String catergory;
    Bitmap bitmap = null;
    DoctorListAdapter doctorListAdapter;
    EditText edittestpatientcauses,edittestpatientfrom,edittestpatientreason;
    Spinner spinnerPatienttimings;
    Button buttonsubmituser,buttonAppointmentDate;
    DBHelper dbHelper;
    public Calendar calender;
    private int day;
    private int month;
    private int year;
    private String patient_mobile_Number;
    String patient_id;
    final Calendar cal = Calendar.getInstance();
    String myFormat = "yyyy-MM-DD"; //In which you need put here
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    TextView textViewcurrentdate,textViewAppointmentDate;



    Cursor cursor;
    //  Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_list_activity);
        dbHelper=new DBHelper(this);
        //Initializing Views
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerViewdoctor);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
       catergory = getIntent().getStringExtra("CategoryName");
        loadHistoryData();


    }

    private void loadHistoryData() {
        cursor = dbHelper.getDoctorList(catergory);


        ID_ArrayList.clear();
        NAME_ArrayList.clear();
        DOCTOR_DEGREE_ArrayList.clear();
        DOCTOR_EXP_ArrayList.clear();
        DOCTOR_SPECILIZATION_ArrayList.clear();
       // bitmaps.clear();


        if (cursor.moveToFirst()) {
            do {
                ID_ArrayList.add(cursor.getString(cursor.getColumnIndex(dbHelper.COLUMN_DOCTOR_ID)));
                NAME_ArrayList.add(cursor.getString(cursor.getColumnIndex(dbHelper.COLUMN_DOCTOR_NAME)));
                DOCTOR_DEGREE_ArrayList.add(cursor.getString(cursor.getColumnIndex(dbHelper.COLUMN_DOCTOR_DEGREE)));
                DOCTOR_EXP_ArrayList.add(cursor.getString(cursor.getColumnIndex(dbHelper.COLUMN_DOCTOR_EXPERIENCE)));
                DOCTOR_SPECILIZATION_ArrayList.add(cursor.getString(cursor.getColumnIndex(dbHelper.COLUMN_DOCTOR_SPECILIZATION)));

               /* byte[] blob = cursor.getBlob(cursor.getColumnIndex(dbHelper.COLUMN_DOCTOR_PROFILE_PICTURE));
                bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                bitmaps.add(bitmap);*/

            } while (cursor.moveToNext());
        }

        doctorListAdapter = new DoctorListAdapter(this,
                ID_ArrayList,
                NAME_ArrayList,
                DOCTOR_DEGREE_ArrayList,
                DOCTOR_SPECILIZATION_ArrayList,
                DOCTOR_EXP_ArrayList
             // bitmaps

        );
        recyclerView.setAdapter(doctorListAdapter);
        cursor.close();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);

        }

        return super.onOptionsItemSelected(item);
    }
    }

