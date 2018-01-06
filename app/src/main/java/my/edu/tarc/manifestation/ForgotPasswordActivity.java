package my.edu.tarc.manifestation;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class ForgotPasswordActivity extends AppCompatActivity {

    ImageButton imageButtonSubmit;
    Button buttonSendCode;
    EditText editTextEmail, editTextVerificationCode, editTextConfirmPassword, editTextNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        this.setTitle("Forgot Password");

        imageButtonSubmit = (ImageButton) findViewById(R.id.imageButtonSubmit);
        buttonSendCode = (Button)findViewById(R.id.buttonSendCode);
        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        editTextVerificationCode = (EditText)findViewById(R.id.editTextVerificationCode);
        editTextConfirmPassword = (EditText)findViewById(R.id.editTextConfirmPassword);
        editTextNewPassword = (EditText)findViewById(R.id.editTextNewPassword);

        editTextVerificationCode.setEnabled(false);
        editTextConfirmPassword.setEnabled(false);
        editTextNewPassword.setEnabled(false);
        imageButtonSubmit.setEnabled(false);

        editTextNewPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (editTextNewPassword.getText().toString().length() < 6) {
                    editTextNewPassword.setError("Password must be at least 6 digit. Please try again!");
                }
            }
        });

        editTextConfirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (editTextConfirmPassword.getText().toString().equals(editTextNewPassword.getText().toString())) {

                } else {
                    editTextConfirmPassword.setError("Password must be match. Please try again!");
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

        buttonSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSendCode(v);
            }
        });

        imageButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSubmit(v);
            }
        });
    }

    private void onClickSendCode(View v) {
        Log.i("Send email", "");

        String[] TO = {"miniyangmi1986@gmail.com"};
        String[] CC = {"xyz@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email.", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ForgotPasswordActivity.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickSubmit(View v){
        Intent intentCancel = new Intent(this, MainActivity.class);
        startActivity(intentCancel);
    }
}
