package com.geniusnine.android.geniusninelifecaredoctor.Adapter;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.geniusnine.android.geniusninelifecaredoctor.Helper.Config;
import com.geniusnine.android.geniusninelifecaredoctor.Helper.DBHelper;
import com.geniusnine.android.geniusninelifecaredoctor.MainActivityDrawer;
import com.geniusnine.android.geniusninelifecaredoctor.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Dev on 18-01-2017.
 */

public class DoctorListAdapter  extends RecyclerView.Adapter<DoctorListAdapter.ListViewHolder> {



    LayoutInflater inflater;
    ArrayList<String> doctorID;
    ArrayList<String> doctorName;
    ArrayList<String> doctorDegree;
    ArrayList<String> doctorSpecialization;
    ArrayList<String> doctorExperience;
    private ArrayList<Bitmap> bitmaps;
    byte[] categoryimage;
    EditText edittestpatientcauses,edittestpatientfrom,edittestpatientreason;
    Spinner spinnerPatienttimings;
    Button buttonsubmituser,buttonAppointmentDate;
    DBHelper dbHelper;
    public Calendar calender;
    private int day;
    private int month;
    private int year;
    private String patient_mobile_Number;
    String patient_id,doctorid,doctorname;
    Cursor cursor;

    Context context;
    final Calendar cal = Calendar.getInstance();
    String myFormat = "yyyy-MM-DD"; //In which you need put here
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    TextView textViewcurrentdate,textViewAppointmentDate;
 // public  TextView tv_name,textViewid,textViewDegree,textViewSpecicalizton,textViewExperience,textViewBookAppointment;

    Activity activity;

    public DoctorListAdapter(Context context, ArrayList<String> id, ArrayList<String> name, ArrayList<String> degree, ArrayList<String> specialization, ArrayList<String> experience) {
        super();
        this.context= context;
        this.doctorID = id;
        this.doctorName =  name;
        this.doctorDegree = degree;
        this.doctorSpecialization =  specialization;
        this.doctorExperience =  experience;
       // this.bitmaps=bitm;
        // this.listener = context;
        inflater = LayoutInflater.from(context);


    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View convertView = inflater.inflate(R.layout.doctor_list_view, parent, false);
        ListViewHolder viewHolder = new ListViewHolder(convertView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {

        holder.textViewid.setText(doctorID.get(position));
        holder.tv_name.setText(doctorName.get(position));
        holder.textViewDegree.setText(doctorDegree.get(position));
        holder.textViewSpecicalizton.setText(doctorSpecialization.get(position));
        holder.textViewExperience.setText(doctorExperience.get(position));
        doctorid=holder.textViewid.getText().toString().trim();
        doctorname=holder.tv_name.getText().toString().trim();

        //holder.iv_delete.setImageBitmap(bitmaps.get(position));
        holder.textViewBookAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper=new DBHelper((Activity) context);
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View alertLayout = inflater.inflate(R.layout.doctor_book_appointmentdialog, null);
                textViewcurrentdate=(TextView)alertLayout.findViewById(R.id.textViewcurrentdate);
                textViewAppointmentDate=(TextView)alertLayout.findViewById(R.id.textViewAppointmentDate);
                spinnerPatienttimings = (Spinner)alertLayout. findViewById(R.id.spinnerpatienttimings);
                buttonsubmituser = (Button)alertLayout. findViewById(R.id.buttonsubmitpatient);
                buttonAppointmentDate = (Button)alertLayout. findViewById(R.id.buttonCalenderAppointmentdate);
                edittestpatientcauses = (EditText)alertLayout. findViewById(R.id.edittextpatientcauses);
                edittestpatientfrom = (EditText)alertLayout. findViewById(R.id.edittextpatientFrom);
                edittestpatientreason = (EditText)alertLayout. findViewById(R.id.edittextpatientReason);
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
                //fetching value from sharedpreference

               // SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
               SharedPreferences sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                //Fetching thepatient_mobile_Number value form sharedpreferences
                patient_mobile_Number = sharedPreferences.getString(Config.PATIENT_MOBILE_NO_SHARED_PREF,null);

                dbHelper.getPatientData(patient_mobile_Number);
                cursor = dbHelper.getPatientData(patient_mobile_Number);
                cursor.moveToFirst();
                if (cursor != null) {
                    patient_id = cursor.getString(cursor.getColumnIndex(dbHelper.COLUMN_PATIENT_ID));
                }

                List<String> timings = new ArrayList<String>();
                timings.add("11.00 am- 1.00 pm");
                timings.add("2.00 pm - 4.00 pm");
                timings.add("6.30 pm - 8.00 pm");

                buttonAppointmentDate.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // Process to get Current Date
                        calender = Calendar.getInstance();
                        day = cal.get(Calendar.DAY_OF_MONTH);
                        month = cal.get(Calendar.MONTH);
                        year = cal.get(Calendar.YEAR);
                        // Launch Date Picker Dialog
                        DatePickerDialog dpd = new DatePickerDialog(context,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        // Display Selected date in textbox
                                        textViewAppointmentDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                                    }
                                }, year, month, day);
                        dpd.show();
                    }


                });

                // Creating adapter for spinner
                ArrayAdapter<String> Adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, timings);

                // Drop down layout style - list view with radio button
                Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                spinnerPatienttimings.setAdapter(Adapter);
                buttonsubmituser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String appointment_status="Request sent",appointment_status_percent="10";
                        String patientcauses, appointment_from_days, patientreasons, appointmenttimings,currentdate;
                        patientcauses = edittestpatientcauses.getText().toString();
                        String appointmentdate=textViewAppointmentDate.getText().toString().trim();
                        appointment_from_days = edittestpatientfrom.getText().toString();
                        patientreasons = edittestpatientreason.getText().toString();
                        appointmenttimings = spinnerPatienttimings.getSelectedItem().toString().trim();
                        currentdate=textViewcurrentdate.getText().toString().trim();
                        if (patientcauses.equals("")) {
                            edittestpatientcauses.setError("Causes is Required");
                        } else if (appointment_from_days.equals("")) {
                            edittestpatientfrom.setError("Duration is Required");
                        } else if (patientreasons.equals("")) {
                            edittestpatientreason.setError("Reason is Required");
                        } else if (spinnerPatienttimings.getSelectedItem().toString().trim().equals("")) {
                            Toast.makeText(context, "Timings Required", Toast.LENGTH_LONG).show();

                        } else {
                            dbHelper.addBookAppointment(patient_id,currentdate,doctorid,doctorname,appointmentdate,appointmenttimings,patientcauses,patientreasons,appointment_from_days,appointment_status,appointment_status_percent);
                            Toast.makeText(context, "Appointment Booked Successfully", Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(context, MainActivityDrawer.class);
                            ((Activity) context).finish();
                            context.startActivity(intent);
                        }
                    }
                });
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                // alertDialogBuilder.setTitle("Medicines No:"+medicinesID.get(position));
                // this is set the view from XML inside AlertDialog
                alertDialogBuilder.setView(alertLayout);
                alertDialogBuilder.setPositiveButton("Close",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();



            }
        });

        animate(holder);
    }

    @Override
    public int getItemCount() {
        return doctorID.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
       TextView tv_name,textViewid,textViewDegree,textViewSpecicalizton,textViewExperience,textViewBookAppointment;
        ImageView iv_delete;

        public ListViewHolder(View itemView) {
            super(itemView);
            textViewid = (TextView) itemView.findViewById(R.id.doctorid);
            tv_name = (TextView) itemView.findViewById(R.id.doctorName);
            textViewDegree = (TextView) itemView.findViewById(R.id.doctorDegree);
            textViewSpecicalizton = (TextView) itemView.findViewById(R.id.doctorSpecialization);
            textViewExperience = (TextView) itemView.findViewById(R.id.doctorExperience);
            textViewBookAppointment = (TextView) itemView.findViewById(R.id.doctorBookAppointment);
           // iv_delete = (ImageView) itemView.findViewById(R.id.imageViewcategory);

        }
    }
    public void animate(RecyclerView.ViewHolder viewHolder) {
        final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(context, R.anim.bounce_interpolator);
        viewHolder.itemView.setAnimation(animAnticipateOvershoot);
    }
}
