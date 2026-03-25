package gpg.finalapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Button login;
    EditText email,password;
    TextView signup;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    SQLiteDatabase db;
    SharedPreferences sp;

    ProgressDialog pd;

    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        sp = getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);

        db = openOrCreateDatabase("GpgApp.db",MODE_PRIVATE,null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),EMAIL VARCHAR(50),CONTACT BIGINT(10),PASSWORD VARCHAR(20),GENDER VARCHAR(10),CITY VARCHAR(20))";
        db.execSQL(tableQuery);

        email = findViewById(R.id.main_email);
        password = findViewById(R.id.main_password);
        login = findViewById(R.id.main_login);
        signup = findViewById(R.id.main_signup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString().trim().equals("")){
                    email.setError("Email Id Required");
                }
                /*else if(!email.getText().toString().trim().matches(emailPattern)){
                    email.setError("Valid Email Id Required");
                }*/
                else if(password.getText().toString().trim().equals("")){
                    password.setError("Password Required");
                }
                else if(password.getText().toString().trim().length()<6){
                    password.setError("Min. 6 Char Password Required");
                }
                else {

//                    String selectQuery = "SELECT * FROM USERS WHERE (EMAIL='"+email.getText().toString()+"' OR CONTACT='"+email.getText().toString()+"') AND PASSWORD='"+password.getText().toString()+"'";
//                    Cursor cursor = db.rawQuery(selectQuery,null);
//                    if(cursor.getCount()>0){
//                        /*System.out.println("Login Successfully");
//                        Log.d("LOGIN","Login Successfully");
//                        Log.e("LOGIN","Login Successfully");*/
//                        while (cursor.moveToNext()){
//                            String sUserId = cursor.getString(0);
//                            String sName = cursor.getString(1);
//                            String sEmail = cursor.getString(2);
//                            String sContact = cursor.getString(3);
//                            String sPassword = cursor.getString(4);
//                            String sGender = cursor.getString(5);
//                            String sCity = cursor.getString(6);
//
//                            sp.edit().putString(ConstantSp.USERID,sUserId).commit();
//                            sp.edit().putString(ConstantSp.NAME,sName).commit();
//                            sp.edit().putString(ConstantSp.EMAIL,sEmail).commit();
//                            sp.edit().putString(ConstantSp.CONTACT,sContact).commit();
//                            sp.edit().putString(ConstantSp.PASSWORD,sPassword).commit();
//                            sp.edit().putString(ConstantSp.GENDER,sGender).commit();
//                            sp.edit().putString(ConstantSp.CITY,sCity).commit();
//
//                        }
//                        Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_LONG).show();
//                        Snackbar.make(view, "Login Successfully", Snackbar.LENGTH_SHORT).show();
//
//                        Intent intent = new Intent(MainActivity.this,DashboardActivity.class);
//                        startActivity(intent);
//                    }
//                    else{
//                        Toast.makeText(MainActivity.this, "Login Unsuccessfully", Toast.LENGTH_SHORT).show();
//                    }



//                    if(new ConnectionDetector(MainActivity.this).isConnectingToInternet()){
//                        new LoginTask().execute();
//                    }
//                    else{
//                        new ConnectionDetector(MainActivity.this).connectiondetect();
//                    }




                    if(new ConnectionDetector(MainActivity.this).isConnectingToInternet()){
                        pd = new ProgressDialog(MainActivity.this);
                        pd.setMessage("Please Wait...");
                        pd.setCancelable(false);
                        pd.show();

                        doRetrofitLogin();
                    }
                    else{
                        new ConnectionDetector(MainActivity.this).connectiondetect();
                    }


                }
            }
        });

    }

    private void doRetrofitLogin() {
        Call<GetLoginData> call = apiInterface.getLoginData(
                email.getText().toString(),
                password.getText().toString()
        );
        call.enqueue(new Callback<GetLoginData>() {
            @Override
            public void onResponse(Call<GetLoginData> call, Response<GetLoginData> response) {
                pd.dismiss();
                if(response.code() == 200){
                    if(response.body().status) {

                        try {
                            GetLoginData.UserData user = response.body().userData.get(0);

                            String userid = user.userid;
                            String name = user.name;
                            String email = user.email;
                            String contact = user.contact;
                            String password = user.password;
                            String gender = user.gender;
                            String city = user.city;


                            sp.edit().putString(ConstantSp.USERID, userid).commit();
                            sp.edit().putString(ConstantSp.NAME, name).commit();
                            sp.edit().putString(ConstantSp.EMAIL, email).commit();
                            sp.edit().putString(ConstantSp.CONTACT, contact).commit();
                            sp.edit().putString(ConstantSp.PASSWORD, password).commit();
                            sp.edit().putString(ConstantSp.GENDER, gender).commit();
                            sp.edit().putString(ConstantSp.CITY, city).commit();


                            new CommonMethod(MainActivity.this, response.body().message);

                            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                            startActivity(intent);
                            finish();


                        }
                        catch (Exception e) {
                            new CommonMethod(MainActivity.this, e.getMessage());
                            Log.d("JSON_ERROR", e.getMessage());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<GetLoginData> call, Throwable t) {
                pd.dismiss();
                new CommonMethod(MainActivity.this,t.getMessage());
                Log.d("RESPONSE",t.getMessage());
            }

        });
    }

    private class LoginTask extends AsyncTask<Void, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(Void... voids) {
            HashMap<String, String> map = new HashMap<>();
            map.put("email", email.getText().toString());
            map.put("password", password.getText().toString());
            return new MakeServiceCall().MakeServiceCall(ConstantSp.URL+"login.php", MakeServiceCall.POST,map);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(pd != null & pd.isShowing()){
                pd.dismiss();
            }

            try {
                JSONObject object = new JSONObject(s);
                new CommonMethod(MainActivity.this, object.getString("Message"));
                if(object.getBoolean("Status")){
                    JSONArray jsonArray = object.getJSONArray("UserData");
                    JSONObject userObject = jsonArray.getJSONObject(0);

                    String userid = userObject.getString("userid");
                    String name = userObject.getString("name");
                    String email = userObject.getString("email");
                    String contact = userObject.getString("contact");
                    String password = userObject.getString("password");
                    String gender = userObject.getString("gender");
                    String city = userObject.getString("city");


                    sp.edit().putString(ConstantSp.USERID, userid).commit();
                    sp.edit().putString(ConstantSp.NAME, name).commit();
                    sp.edit().putString(ConstantSp.EMAIL, email).commit();
                    sp.edit().putString(ConstantSp.CONTACT, contact).commit();
                    sp.edit().putString(ConstantSp.PASSWORD, password).commit();
                    sp.edit().putString(ConstantSp.GENDER, gender).commit();
                    sp.edit().putString(ConstantSp.CITY, city).commit();

                    Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
            catch (JSONException e) {
                new CommonMethod(MainActivity.this, e.getMessage());
                Log.d("JSON_ERROR", e.getMessage());
            }
        }
    }



}