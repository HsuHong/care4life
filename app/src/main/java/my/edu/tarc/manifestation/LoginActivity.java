package my.edu.tarc.manifestation;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity {

    EditText editTextUsername, editTextPassword;
    TextView textViewForgotPassword, textViewRegister;
    ImageButton imageButtonLogin, imageButtonVisiblePswd;

    public  static  String LOGGED_IN_USER = "LOGGED_IN_USER";
    public  static  String LOGGED_IN_PSWD = "LOGGED_IN_PSWD";
    String userName, PassWord;
    ProgressDialog progressDialog;
    int test = 0;
    //  protected static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.setTitle("Login");

        //   context = getApplicationContext();

        editTextUsername = (EditText)findViewById(R.id.editTextUsername);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        textViewForgotPassword = (TextView)findViewById(R.id.textViewForgotPassword);
        textViewRegister = (TextView)findViewById(R.id.textViewRegister);
        imageButtonLogin = (ImageButton)findViewById(R.id.imageButtonLogin);
        imageButtonVisiblePswd = (ImageButton)findViewById(R.id.imageButtonVisiblePswd);

        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRegister(v);
            }
        });

        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickForgotPassword(v);
            }
        });

        imageButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLogin(v);
            }
        });

        imageButtonVisiblePswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickVisible(v);
            }
        });



    }

    public void onClickVisible(View v){
        if (test == 0){
            editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            test = 1;
        }else if (test == 1){
            editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            test = 0;
        }
    }

    public void onClickLogin(View v){
        userName = editTextUsername.getText().toString();
        PassWord = editTextPassword.getText().toString();
        String type = "login";

        if (editTextUsername.getText().toString().equals("") && editTextPassword.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter your username and password!", Toast.LENGTH_LONG).show();
        }else if (editTextUsername.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter your username!", Toast.LENGTH_LONG).show();
        }
        else if (editTextPassword.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter your password!", Toast.LENGTH_LONG).show();
        }else {
            progressDialog = new ProgressDialog(this);
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(type, userName, PassWord);
        }
    }

    private class BackgroundWorker extends AsyncTask<String, Void, String> {
        private Context context;
        AlertDialog alertDialog;

        public BackgroundWorker(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String loginURL = "https://care4life.000webhostapp.com/login_user.php";

            // If the type of the task = login
            if (type == "login") {

                String username = params[1];
                String password = params[2];

                try {

                    //Establish HttpURLConnection with POST method
                    URL url = new URL(loginURL);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    //Set output stream
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&"
                            + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    //Read the data
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"),8);
                    String result = "";
                    String line = "";

                    while ((line = bufferedReader.readLine()) != null) {
                        result += line;
                    }

                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {

            // Create an alert dialog and set it's title
            alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Login Status");

            if (!progressDialog.isShowing()) ;
            progressDialog.setMessage("Logging in");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {

            // If login failed
            if (result.equals("login not success")) {

                if (progressDialog.isShowing())
                    progressDialog.dismiss();

                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Login Failed.");
                builder.setMessage("Login Failed. Please check your username and password!");
                builder.setPositiveButton("Ok", null);

                android.support.v7.app.AlertDialog dialog = builder.create();
                dialog.show();
            }

            else if(result.equals("login success")){
                Intent Homeintent = new Intent(LoginActivity.this, MainActivity.class);
                LOGGED_IN_USER = userName;
                startActivity(Homeintent);
            }

            //else allow user to login and continue
            else {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    LOGGED_IN_USER = userName;
                    LOGGED_IN_PSWD = PassWord;
                }
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }

    public void onClickRegister(View v){
        Intent intentRegister = new Intent(this, RegisterActivity.class);
        startActivity(intentRegister);
    }

    public void onClickForgotPassword(View v){
        //Intent intentForgotPswd = new Intent(this, ForgotPasswordActivity.class);
        Intent intentForgotPswd = new Intent(this, ViewProfileActivity.class);
        startActivity(intentForgotPswd);
    }
}

