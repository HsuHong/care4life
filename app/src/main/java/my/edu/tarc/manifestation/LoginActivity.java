package my.edu.tarc.manifestation;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import static my.edu.tarc.medicalknowledge.R.layout.activity_login;

public class LoginActivity extends AppCompatActivity {

    TextView textViewForgotPassword, textViewRegister;

 //  protected static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_login);

        this.setTitle("Login");

     //   context = getApplicationContext();

        textViewForgotPassword = (TextView)findViewById(R.id.textViewForgotPassword);
        textViewRegister = (TextView)findViewById(R.id.textViewRegister);

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



    }

    public void onClickRegister(View v){
        Intent intentRegister = new Intent(this, RegisterActivity.class);
        startActivity(intentRegister);
    }

    public void onClickForgotPassword(View v){
        Intent intentForgotPswd = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intentForgotPswd);
    }
}
