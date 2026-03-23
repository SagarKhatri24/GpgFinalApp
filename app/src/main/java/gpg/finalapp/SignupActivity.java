package gpg.finalapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    Button signup;
    EditText name,email,contact,password,confirmPassword;
    TextView login;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    RadioGroup gender;
    CheckBox terms;
    String sGender;

    Spinner spinner;
    //String[] cityArray = {"Select City","Ahmedabad","Vadodara","Surat","Rajkot"};
    ArrayList<String> cityArray;
    String sCity = ""; //Vadodara
    SQLiteDatabase db;

    ProgressDialog pd;

    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        db = openOrCreateDatabase("GpgApp.db",MODE_PRIVATE,null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),EMAIL VARCHAR(50),CONTACT BIGINT(10),PASSWORD VARCHAR(20),GENDER VARCHAR(10),CITY VARCHAR(20))";
        db.execSQL(tableQuery);

        name = findViewById(R.id.signup_name);
        email = findViewById(R.id.signup_email);
        contact = findViewById(R.id.signup_contact);
        password = findViewById(R.id.signup_password);
        confirmPassword = findViewById(R.id.signup_confirm_password);
        signup = findViewById(R.id.signup_sign_up);
        login = findViewById(R.id.signup_signin);
        terms = findViewById(R.id.signup_terms);

        spinner = findViewById(R.id.signup_city);

        cityArray = new ArrayList<>();
        cityArray.add("Ahmedabad");
        cityArray.add("Vadodara");
        cityArray.add("Demo");
        cityArray.add("Gandhinagar");
        cityArray.add("Rajkt");

        cityArray.remove(2);
        cityArray.set(3,"Rajkot");

        cityArray.add(0,"Select City");

        ArrayAdapter adapter = new ArrayAdapter(SignupActivity.this, android.R.layout.simple_list_item_1,cityArray);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    sCity = "";
                }
                else {
                    //sCity = cityArray[i];
                    sCity = cityArray.get(i);
                    Toast.makeText(SignupActivity.this, sCity, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        gender = findViewById(R.id.signup_gender);

        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull RadioGroup radioGroup, int i) {
                RadioButton radioButton = findViewById(i);
                sGender = radioButton.getText().toString();
                Toast.makeText(SignupActivity.this, sGender, Toast.LENGTH_SHORT).show();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString().trim().equals("")){
                    name.setError("Name Required");
                }
                else if(email.getText().toString().trim().equals("")){
                    email.setError("Email Id Required");
                }
                else if(!email.getText().toString().trim().matches(emailPattern)){
                    email.setError("Valid Email Id Required");
                }
                else if(contact.getText().toString().trim().equals("")){
                    contact.setError("Contact No. Required");
                }
                else if(contact.getText().toString().trim().length()<10){
                    contact.setError("Valid Contact No. Required");
                }
                else if(password.getText().toString().trim().equals("")){
                    password.setError("Password Required");
                }
                else if(password.getText().toString().trim().length()<6){
                    password.setError("Min. 6 Char Password Required");
                }
                else if(confirmPassword.getText().toString().trim().equals("")){
                    confirmPassword.setError("Confirm Password Required");
                }
                else if(confirmPassword.getText().toString().trim().length()<6){
                    confirmPassword.setError("Min. 6 Char Confirm Password Required");
                }
                else if(!password.getText().toString().trim().matches(confirmPassword.getText().toString().trim())){
                    confirmPassword.setError("Password Does Not Match");
                }
                else if(gender.getCheckedRadioButtonId() == -1){
                    Toast.makeText(SignupActivity.this, "Please Select Gender", Toast.LENGTH_SHORT).show();
                }
                else if(sCity == ""){
                    Toast.makeText(SignupActivity.this, "Please Select City", Toast.LENGTH_SHORT).show();
                }
                else if(!terms.isChecked()){
                    Toast.makeText(SignupActivity.this, "Please Accpet Terms & Conditions", Toast.LENGTH_SHORT).show();
                }
                else {

                    String selectQuery = "SELECT * FROM USERS WHERE EMAIL='"+email.getText().toString()+"' OR CONTACT='"+contact.getText().toString()+"'";
                    Cursor cursor = db.rawQuery(selectQuery,null);
                    if(cursor.getCount()>0){
                        Toast.makeText(SignupActivity.this, "User Already Exists", Toast.LENGTH_SHORT).show();
                    }
                    else{
//                        //String insertQuery = "INSERT INTO USERS VALUES (NULL,'John')";
//                        String insertQuery = "INSERT INTO USERS VALUES (NULL,'"+name.getText().toString()+"','"+email.getText().toString()+"','"+contact.getText().toString()+"','"+password.getText().toString()+"','"+sGender+"','"+sCity+"')";
//                        Log.d("INSERTQUERY",insertQuery);
//                        db.execSQL(insertQuery);
//
//                        System.out.println("Signup Successfully");
//                        Log.d("LOGIN", "Signup Successfully");
//                        Log.e("LOGIN", "Signup Successfully");
//                        Log.w("LOGIN", "Signup Successfully");
//
//                        Toast.makeText(SignupActivity.this, "Signup Successfully", Toast.LENGTH_SHORT).show();
//                        Snackbar.make(view, "Signup Successfully", Snackbar.LENGTH_LONG).show();
//
//                        onBackPressed();

//                        if(new ConnectionDetector(SignupActivity.this).isConnectingToInternet()){
//                            new SignupTask().execute();
//                        }


                        if(new ConnectionDetector(SignupActivity.this).isConnectingToInternet()){
                            pd = new ProgressDialog(SignupActivity.this);
                            pd.setMessage("Please Wait...");
                            pd.setCancelable(false);
                            pd.show();
                            doRetorfitSignup();
                        }
                        else{
                            new ConnectionDetector(SignupActivity.this).connectiondetect();
                        }
                    }
                }
            }
        });
    }

    private void doRetorfitSignup() {

        Call<GetSignupData> call = apiInterface.getSignupData(
                name.getText().toString(),
                email.getText().toString(),
                contact.getText().toString(),
                password.getText().toString(),
                sGender,
                sCity
        );

        call.enqueue(new Callback<GetSignupData>() {
            @Override
            public void onResponse(Call<GetSignupData> call, Response<GetSignupData> response) {
                pd.dismiss();
                if(response.code() == 200){
                    if(response.body().status){
                        new CommonMethod(SignupActivity.this,response.body().message);
                        onBackPressed();
                    }
                    else{
                        new CommonMethod(SignupActivity.this,response.body().message);
                    }
                }
                else{
                    new CommonMethod(SignupActivity.this,"Server Error Code : "+response.code());
                }
            }

            @Override
            public void onFailure(Call<GetSignupData> call, Throwable t) {
                pd.dismiss();
                new CommonMethod(SignupActivity.this,t.getMessage());
                Log.d("RESPONSE",t.getMessage());
            }
        });

    }

    private class SignupTask extends AsyncTask<Void, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(SignupActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(Void... voids) {
            HashMap<String, String> map = new HashMap<>();
            map.put("name", name.getText().toString());
            map.put("email", email.getText().toString());
            map.put("contact", contact.getText().toString());
            map.put("password", password.getText().toString());
            map.put("gender", sGender);
            map.put("city", sCity);
            return new MakeServiceCall().MakeServiceCall(ConstantSp.URL+"signup.php", MakeServiceCall.POST,map);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(pd != null & pd.isShowing()){
                pd.dismiss();
            }

            try {
                JSONObject object = new JSONObject(s);
                new CommonMethod(SignupActivity.this, object.getString("Message"));
                if(object.getBoolean("Status")){
                    onBackPressed();
                }
            }
            catch (JSONException e) {
                new CommonMethod(SignupActivity.this, e.getMessage());
                Log.d("JSON_ERROR", e.getMessage());
            }
        }
    }
}