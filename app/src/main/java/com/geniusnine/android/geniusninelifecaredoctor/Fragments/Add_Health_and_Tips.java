package com.geniusnine.android.geniusninelifecaredoctor.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.geniusnine.android.geniusninelifecaredoctor.Helper.DBHelper;
import com.geniusnine.android.geniusninelifecaredoctor.Helper.Utils;
import com.geniusnine.android.geniusninelifecaredoctor.MainActivityDrawer;
import com.geniusnine.android.geniusninelifecaredoctor.R;


import com.geniusnine.android.geniusninelifecaredoctor.R;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

;


/**
 * Created by Dev on 12-01-2017.
 */

public class Add_Health_and_Tips extends Fragment {
    DBHelper dbHelper;
    EditText name,description;
    Button button;
    Uri selectedImageUri;
    TextView textViewcurrentdate;
    final Calendar cal = Calendar.getInstance();
    String myFormat = "yyyy-MM-DD"; //In which you need put here
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    ImageView uploadhealth_and_tipsProfilepicture;
    private static final int SELECT_PICTURE = 100;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    String doctorhealth_and_tipsname,doctorhealth_and_tipsdecription,currentdate;
    private static final String TAG = "MainActivityDrawer";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_helth_and_tips, null);
        dbHelper = new DBHelper(getActivity());
        textViewcurrentdate=(TextView)v.findViewById(R.id.textViewcurrentdate);
        name=(EditText)v.findViewById(R.id.editTexttipsname);
        description=(EditText)v.findViewById(R.id.editTexttipsdescription);
        uploadhealth_and_tipsProfilepicture=(ImageView)v.findViewById(R.id.imageViewhealth_and_tipspicture);
        button=(Button)v.findViewById(R.id.buttonaddhealth_and_tipssubmit);
        dbHelper=new DBHelper(getActivity());
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
        uploadhealth_and_tipsProfilepicture.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doctorhealth_and_tipsname = name.getText().toString().trim();
                doctorhealth_and_tipsdecription = description.getText().toString().trim();
                currentdate=textViewcurrentdate.getText().toString().trim();
                if (null == selectedImageUri) {
                    Toast.makeText(getActivity(), "Please Select Image For health_and_tips...", Toast.LENGTH_LONG).show();
                }
                else if (name.getText().toString().trim().equals("")) {
                    name.setError("health_and_tips Name Required");
                }  else if (description.getText().toString().trim().equals("")) {
                    description.setError("health_and_tips Description Required");
                }
                else {
                    if (null != selectedImageUri) {

                        // Saving to Database...
                        if (saveImageInDB(selectedImageUri)) {
                            // showMessage("Image Saved in Database...");
                            Toast.makeText(getActivity(), "health_and_tips Saved in Database...", Toast.LENGTH_LONG).show();
                            Intent i=new Intent(getActivity(), MainActivityDrawer.class);
                            getActivity().finish();
                            getActivity().startActivity(i);
                        }

                    }


                }
            }

        });

        return v;
    }
    void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {

                selectedImageUri = data.getData();

                if (null != selectedImageUri) {
                    uploadhealth_and_tipsProfilepicture .setImageURI(selectedImageUri);

                }

            }
        }
    }


    // Save the
    Boolean saveImageInDB(Uri selectedImageUri) {
        verifyStoragePermissions(getActivity());
        try {
            dbHelper.open();
            InputStream iStream = getActivity().getContentResolver().openInputStream(selectedImageUri);
            byte[] inputData = Utils.getBytes(iStream);
            dbHelper.addhealth_and_tips(doctorhealth_and_tipsname,doctorhealth_and_tipsdecription,inputData,currentdate);
            dbHelper.close();
            return true;
        } catch (IOException ioe) {
            Log.e(TAG, "<saveImageInDB> Error : " + ioe.getLocalizedMessage());
            dbHelper.close();
            return false;
        }

    }


    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

}