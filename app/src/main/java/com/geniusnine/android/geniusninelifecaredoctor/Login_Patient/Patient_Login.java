package com.geniusnine.android.geniusninelifecaredoctor.Login_Patient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.geniusnine.android.geniusninelifecaredoctor.Helper.Config;
import com.geniusnine.android.geniusninelifecaredoctor.Helper.DBHelper;
import com.geniusnine.android.geniusninelifecaredoctor.MainActivityDrawer;
import com.geniusnine.android.geniusninelifecaredoctor.Patient_Registration.Patient_Registration;
import com.geniusnine.android.geniusninelifecaredoctor.R;


/**
 * Created by Dev on 12-01-2017.
 */

public class Patient_Login extends Activity {
    DBHelper dbHelper;
    Cursor cursor;
    EditText edittextUsername,edittextPassword;
    TextView textViewforgetpassword;
    Button buttonsignIn,buttonsignUp;

    //boolean variable to check user is logged in or not
    //initially it is false
    private boolean loggedIn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_login);
        edittextUsername=(EditText)findViewById(R.id.editTextUserName);
        edittextPassword=(EditText)findViewById(R.id.editTextPassword);
        textViewforgetpassword=(TextView)findViewById(R.id.textViewforgetpasswordclick);
        buttonsignIn=(Button)findViewById(R.id.buttonSignIn);
        buttonsignUp=(Button)findViewById(R.id.buttonSignUp);
        buttonsignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edittextUsername.getText().toString().trim();
                String password = edittextPassword.getText().toString().trim();
                String MobileNumberpattern = "[0-9]{10}";
                String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String passpattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
                if(username.equals("") || username == null)
                {
                    Toast.makeText(Patient_Login.this, "Please enter User Name", Toast.LENGTH_SHORT).show();
                }
                else if(password.equals("") || password == null)
                {
                    Toast.makeText(Patient_Login.this, "Please enter your Password", Toast.LENGTH_SHORT).show();
                }else if(edittextUsername.getText().toString().trim().equals("")){
                    edittextUsername.setError("Mobile Number is Required");
                }else if(!edittextUsername.getText().toString().trim().matches(MobileNumberpattern)){
                    edittextUsername.setError("Please Enter Valid Mobile Number");
                } else if(edittextPassword.getText().toString().trim().equals("")){
                    edittextPassword.setError("Password is Required");
                } else if(!edittextPassword.getText().toString().trim().matches(passpattern)){
                    edittextPassword.setError("Password Contains One capital letter,One number,One symbol (@,$,%,#,)");
                }else if(!(edittextPassword.getText().toString().trim().length() ==10)){
                    edittextPassword.setError("Password size Should 10 Characters");
                }/* else if(edittextPatientemail.getText().toString().trim().equals("")){
                    edittextPatientemail.setError("Email id is Required");
                }else if(!edittextPatientemail.getText().toString().trim().matches(emailpattern)){
                    edittextPatientemail.setError("Please Enter Valid Email");
                }*/
                else
                {

                    boolean validLogin = validateLogin(username, password, Patient_Login.this);
                    if(validLogin)
                    {
                        //System.out.println("In Valid");
                        Toast.makeText(Patient_Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        //Creating a shared preference
                        SharedPreferences sharedPreferences = Patient_Login.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                        //Creating editor to store values to shared preferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        //Adding values to editor
                        editor.putBoolean(Config.PATIENT_LOGGEDIN_SHARED_PREF, true);
                        editor.putString(Config.PATIENT_MOBILE_NO_SHARED_PREF, username);

                        //Saving values to editor
                        editor.commit();
                        Intent in = new Intent(Patient_Login.this, MainActivityDrawer.class);
                        finish();
                      //  in.putExtra("UserName", username.getText().toString());
                        startActivity(in);

                    }
                }

            }
        });
        buttonsignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(Patient_Login.this, Patient_Registration.class);
                startActivity(i2);
            }
        });
        textViewforgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Patient_Login.this,"Forget Password",Toast.LENGTH_SHORT).show();

            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                /*Intent i=new Intent(this,Splash_Screen.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);*/
                finish();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private boolean validateLogin(String username, String password, Context baseContext)
    {
        dbHelper = new DBHelper(this);
        SQLiteDatabase db =dbHelper.getReadableDatabase();

        String[] columns = {"patient_id"};

        String selection = "patient_mobile=? AND patient_password=?";
        String[] selectionArgs = {username,password};

        Cursor cursor = null;
        try{

            cursor = db.query(DBHelper.TABLE_PATIENT_INFORMATION, columns, selection, selectionArgs, null, null, null);
            startManagingCursor(cursor);
        }
        catch(Exception e)

        {
            e.printStackTrace();
        }
        int numberOfRows = cursor.getCount();

        if(numberOfRows <= 0)
        {

            Toast.makeText(getApplicationContext(), "User Name and Password miss match..\nPlease Try Again", Toast.LENGTH_LONG).show();
            edittextUsername.setText(null);
            edittextPassword.setText(null);
            return false;
        }


        return true;

    }
    @Override
    protected void onResume() {
        super.onResume();
        //In onresume fetching value from sharedpreference
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);

        //Fetching the boolean value form sharedpreferences
        loggedIn = sharedPreferences.getBoolean(Config.PATIENT_LOGGEDIN_SHARED_PREF, false);

        //If we will get true
        if(loggedIn){
            //We will start the Profile Activity
            Intent intent = new Intent(Patient_Login.this, MainActivityDrawer.class);
            finish();
            startActivity(intent);
        }
    }
   /* public void onDestroy()
    {
        super.onDestroy();
        db.close();
    }*/

    }
