package my.edu.tarc.manifestation;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    ImageButton imageButtonCancel, imageButtonRegister;
    EditText editTextUsername, editTextPswd, editTextConfirmPswd, editTextName, editTextEmail, editTextDOB;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.setTitle("Register");

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPswd = (EditText) findViewById(R.id.editTextPswd);
        editTextConfirmPswd = (EditText) findViewById(R.id.editTextConfirmPswd);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextDOB = (EditText) findViewById(R.id.editTextDOB);

        editTextPswd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (editTextPswd.getText().toString().length() < 6) {
                    editTextPswd.setError("Password must be at least 6 digit. Please try again!");
                }
            }
        });

        editTextConfirmPswd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (editTextConfirmPswd.getText().toString().equals(editTextPswd.getText().toString())) {

                } else {
                    editTextConfirmPswd.setError("Password must be match. Please try again!");
                }
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

        imageButtonCancel = (ImageButton) findViewById(R.id.imageButtonCancel);
        imageButtonRegister = (ImageButton) findViewById(R.id.imageButtonRegister);

        imageButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCancel(v);
            }
        });

        imageButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRegister(v);
            }
        });

    }

    public void onClickCancel(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cancel Registration");
        builder.setMessage("Are you sure want to cancel your registration?");

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
        Intent intentCancel = new Intent(this, LoginActivity.class);
        startActivity(intentCancel);
    }

    public void onClickRegister(View v){
        String username = editTextUsername.getText().toString();
        String password = editTextPswd.getText().toString();
        String confirmPassword = editTextConfirmPswd.getText().toString();
        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        String dob = editTextDOB.getText().toString();

        if (username.matches("") || password.matches("") || confirmPassword.matches("") || name.matches("") || email.matches("") || dob.matches("")) {
            Toast.makeText(this, "Please enter all your personal details!.", Toast.LENGTH_SHORT).show();
        } else {

            showAlertDialogButtonClicked(v);
        }
    }

    public void showAlertDialogButtonClicked(View v){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Register");
        builder.setMessage("Are you sure want to register a new account?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id) {
                yesRegisterOnclick();
            }
        });
        builder.setNegativeButton("No", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void yesRegisterOnclick(){

        User user = new User();

        user.setUsername(editTextUsername.getText().toString());
        user.setPassword(editTextPswd.getText().toString());
        user.setName(editTextName.getText().toString());
        user.setEmail(editTextEmail.getText().toString());
        user.setDob(editTextDOB.getText().toString());

        progressDialog = new ProgressDialog(this);

        //Create a new customer in database
        try {
            makeServiceCall(this, "https://care4life.000webhostapp.com/insert_new_user.php", user);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void makeServiceCall(Context context, String url, final User user) {
        RequestQueue queue = Volley.newRequestQueue(context);

        //Send data
        try {

            //Setting up progress dialog
            if (!progressDialog.isShowing()) ;
            progressDialog.setMessage("Creating Account");
            progressDialog.show();

            StringRequest postRequest = new StringRequest(
                    Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            JSONObject jsonObject;
                            try {
                                jsonObject = new JSONObject(response);
                                int success = jsonObject.getInt("success");
                                String message = jsonObject.getString("message");
                                if (success == 0) {
                                    if (progressDialog.isShowing())
                                        progressDialog.dismiss();

                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                } else {
                                    if (progressDialog.isShowing())
                                        progressDialog.dismiss();

                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error. " + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();

                    // Put the parameters with specific values
                    params.put("username", user.getUsername());
                    params.put("password", user.getPassword());
                    params.put("name", user.getName());
                    params.put("email", user.getEmail());
                    params.put("dob", user.getDob());
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    return params;
                }
            };
            queue.add(postRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

