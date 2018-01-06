package my.edu.tarc.manifestation;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

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

public class ViewProfileActivity extends AppCompatActivity {

    //Declare all variables
    ImageButton imageButtonCancel, imageButtonEdit, imageButtonSubmit;
    EditText editTextName, editTextEmail, editTextDOB;
    //Declare all global variables
    public static String editedName = "";
    public static String editedEmail = "";
    public static String editedDOB = "";
    public static String constantUsername = "";
    public static String constantPassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);



        this.setTitle("View Profile");

        imageButtonEdit = (ImageButton) findViewById(R.id.imageButtonEdit);
        imageButtonSubmit = (ImageButton) findViewById(R.id.imageButtonSubmit);
        imageButtonCancel = (ImageButton) findViewById(R.id.imageButtonCancel2);

        editTextName = (EditText) findViewById(R.id.editTextName2);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail2);
        editTextDOB = (EditText) findViewById(R.id.editTextDOB2);

        editTextName.setEnabled(false);
        editTextEmail.setEnabled(false);
        editTextDOB.setEnabled(false);
        imageButtonSubmit.setEnabled(false);
        //Declare all local variables
        String typeUserDetails = "retrieveUserDetails";
        String username = LoginActivity.LOGGED_IN_USER;
      //  String username = "lay123";


        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(typeUserDetails, username);

        imageButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    onClickEdit(v);
            }
        });

        imageButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSubmit(v);
            }
        });

        imageButtonCancel.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        onClickCancel(v);
                    }
                });

        editTextEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                String email = editTextEmail.getText().toString().trim();

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if (email.matches(emailPattern)) {
                } else {
                    editTextEmail.setError("Invalid email address. Please try again!");
                }
            }
        });

        editTextDOB.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                String dob = editTextDOB.getText().toString().trim();

                String DOBPattern = "\\d{4}/\\d{2}/\\d{2}";

                if (dob.matches(DOBPattern)) {
                } else {
                    editTextDOB.setError("Invalid date of birth format. Please try again!");
                }
            }
        });
    }

    private void onClickEdit(View v) {
        editTextName.setEnabled(true);
        editTextEmail.setEnabled(true);
        editTextDOB.setEnabled(true);
        imageButtonSubmit.setEnabled(true);
        imageButtonEdit.setEnabled(false);
    }

    private void onClickSubmit(View v) {
        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        String dob = editTextDOB.getText().toString();


        if (name.matches("") || email.matches("") || dob.matches("")) {
            Toast.makeText(this, "Please enter all your personal details!.", Toast.LENGTH_SHORT).show();
        } else {

            showAlertDialogButtonClicked(v);
            editedName = name;
            editedEmail = email;
            editedDOB = dob;
           constantUsername = LoginActivity.LOGGED_IN_USER;
            constantPassword = LoginActivity.LOGGED_IN_PSWD;

          //  constantUsername = "lay123";
          //  constantPassword = "lay123";
        }
    }

    public void showAlertDialogButtonClicked(View v){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Profile");
        builder.setMessage("Are you sure want to edit your profile details?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id) {
                okOnclick();
            }
        });
        builder.setNegativeButton("No", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void okOnclick() {

        editTextName.setEnabled(false);
        editTextEmail.setEnabled(false);
        editTextDOB.setEnabled(false);
        imageButtonSubmit.setEnabled(false);
        imageButtonEdit.setEnabled(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Notice");
        builder.setMessage("Profile updated. Back to main menu.");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                BackOnclick();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void BackOnclick() {

        Intent editProfileBackConfirmIntent = new Intent(this, MainActivity.class);
        //Intent editProfileBackConfirmIntent = new Intent(this, LoginActivity.class);
        startActivity(editProfileBackConfirmIntent);
    }

    private void onClickCancel(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cancel Edit");
        builder.setMessage("Are you sure want to cancel edit your profile details?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id) {
                YesCancelOnClick();
            }
        });

        builder.setNegativeButton("No", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void YesCancelOnClick(){
        Intent CancelIntent = new Intent(this, MainActivity.class);
       // Intent CancelIntent = new Intent(this, LoginActivity.class);
        startActivity(CancelIntent);
    }


    // Retrieve customer's personal information
    private class BackgroundWorker extends AsyncTask<String, Void, String> {

        Context context;
        String taskType;

        public BackgroundWorker(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String userURL = "https://care4life.000webhostapp.com/select_user.php";
            taskType = type;

            if (type == "retrieveUserDetails") {
                String username = params[1];

                try {
                    //Establish httpUrlConnection with POST method
                    URL url = new URL(userURL);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    //Set output stream
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");

                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    // Read the data
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
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

    }

    @Override
    protected void onPostExecute(String result) {

      //  AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
       // builder.setTitle("Register");
      //  builder.setMessage("Are you sure want to register a new account?");

      //  builder.setPositiveButton("BB", null);

      //  AlertDialog dialog = builder.create();
      //  dialog.show();


        // Create jsonArray, expecting more than 1 records
        try {
            JSONArray jsonArray = new JSONArray(result);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject userResponse = (JSONObject) jsonArray.get(i);

                String username1 = userResponse.getString("username");
                String password1 = userResponse.getString("password");
                String name1 = userResponse.getString("name");
                String email1 = userResponse.getString("email");
                String dob1 = userResponse.getString("dob");

                User user = new User(username1, password1, name1, email1, dob1);

                if(username1.equals(constantUsername)){
                    // Set up the initial output
                    //edit.setText(user.getUsername());
                   // password1.setText(user.getPassword());
                    editTextName.setText(editedName);
                    editTextEmail.setText(editedEmail);
                    editTextDOB.setText(editedDOB);
                }else{
                    // Set up the initial output
                    editTextName.setText(user.getName());
                    editTextEmail.setText(user.getEmail());
                    editTextDOB.setText(user.getDob());
                }
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error." + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

}
}

